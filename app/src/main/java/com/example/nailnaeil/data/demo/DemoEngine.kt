package com.example.nailnaeil.data.demo

import androidx.compose.runtime.mutableStateOf
import com.example.nailnaeil.data.remote.dto.EstimateImage
import com.example.nailnaeil.data.remote.dto.EstimateListItem
import com.example.nailnaeil.data.remote.dto.EstimateLowestPriceShop
import com.example.nailnaeil.data.remote.dto.EstimatePrice
import com.example.nailnaeil.data.remote.dto.EstimateProposalDetailResponse
import com.example.nailnaeil.data.remote.dto.EstimateProposalShop
import com.example.nailnaeil.data.remote.dto.EstimateProposalTime
import com.example.nailnaeil.data.remote.dto.EstimateProposalTimesResponse
import com.example.nailnaeil.data.remote.dto.EstimateRequest
import com.example.nailnaeil.data.remote.dto.EstimateResponse
import com.example.nailnaeil.data.remote.dto.EstimateResultResponse
import com.example.nailnaeil.data.remote.dto.EstimateResultResponseItem
import com.example.nailnaeil.data.remote.dto.EstimateResultShop
import com.example.nailnaeil.data.remote.dto.EstimateSchedule
import com.example.nailnaeil.data.remote.dto.EstimateWaitingShop
import com.example.nailnaeil.data.remote.dto.NailType
import com.example.nailnaeil.data.remote.dto.ReservationDetailResponse
import com.example.nailnaeil.data.remote.dto.ReservationListItem
import com.example.nailnaeil.data.remote.dto.ReservationResponse
import com.example.nailnaeil.data.remote.dto.VisitTimeSlot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

data class DemoAdminRequestSummary(
    val requestId: Long,
    val title: String,
    val createdAt: String,
    val totalShops: Int,
    val respondedCount: Int,
    val waitingCount: Int
)

/**
 * 데모데이 시연을 위한 클라이언트 전용 견적/예약 시뮬레이터.
 * 실제 서버를 전혀 호출하지 않고, Repository 계층에서 데모 ID 범위(9억대)를 가로채
 * 이 object가 만든 데이터를 대신 돌려준다. 앱 프로세스가 살아있는 동안만 유지되는 메모리 상태다.
 */
object DemoEngine {
    private const val ESTIMATE_ID_BASE = 900_000_000L
    private const val PROPOSAL_ID_BASE = 900_100_000L
    private const val PROPOSAL_TIME_ID_BASE = 900_200_000L
    private const val RESERVATION_ID_BASE = 900_300_000L

    /** 매장 탐색을 데모용 10개 매장으로 대체할지 여부. Admin 설정 화면에서 즉시 끌 수 있는 안전판. */
    val demoModeEnabled = mutableStateOf(true)

    private val estimateIdCounter = AtomicLong(ESTIMATE_ID_BASE)
    private val proposalIdCounter = AtomicLong(PROPOSAL_ID_BASE)
    private val proposalTimeIdCounter = AtomicLong(PROPOSAL_TIME_ID_BASE)
    private val reservationIdCounter = AtomicLong(RESERVATION_ID_BASE)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val deliveryJobs = mutableMapOf<Long, Job>()

    private data class RequestRecord(
        val requestId: Long,
        val request: EstimateRequest,
        val response: EstimateResponse,
        val allShops: List<DemoShop>
    )

    private data class ProposalRecord(
        val item: EstimateResultResponseItem,
        val shop: DemoShop,
        val requestId: Long,
        val memo: String,
        val times: List<EstimateProposalTime>
    )

    private val requestRecords = mutableMapOf<Long, RequestRecord>()
    private val resultFlows = mutableMapOf<Long, MutableStateFlow<EstimateResultResponse>>()
    private val proposals = mutableMapOf<Long, ProposalRecord>()
    private val reservations = mutableMapOf<Long, ReservationDetailResponse>()
    private val reservationProposalIds = mutableMapOf<Long, Long>()

    private val _activeRequests = MutableStateFlow<List<DemoAdminRequestSummary>>(emptyList())
    val activeRequests: StateFlow<List<DemoAdminRequestSummary>> = _activeRequests.asStateFlow()

    fun isDemoEstimateId(id: Long?): Boolean = id != null && id in ESTIMATE_ID_BASE until PROPOSAL_ID_BASE
    fun isDemoProposalId(id: Long?): Boolean = id != null && id in PROPOSAL_ID_BASE until PROPOSAL_TIME_ID_BASE
    fun isDemoReservationId(id: Long?): Boolean = id != null && id >= RESERVATION_ID_BASE

    /** 요청받은 견적을 즉시 생성하고, ~10초 뒤부터 매장 응답을 순차적으로 채워 넣는 백그라운드 딜리버리를 시작한다. */
    fun createEstimate(request: EstimateRequest): EstimateResponse {
        val requestId = estimateIdCounter.incrementAndGet()
        val shops = (request.shopIds.orEmpty()).mapNotNull { DemoShops.byId(it) }.ifEmpty { DemoShops.all }
        val createdAt = nowIso()
        val title = buildTitle(request)

        val response = EstimateResponse(
            estimateId = requestId,
            title = title,
            nailType = request.nailType,
            removalTypes = request.removalTypes,
            schedules = request.schedules,
            recommendType = request.recommendType,
            description = request.description,
            status = "MATCHING",
            designId = null,
            images = request.images.orEmpty().mapIndexed { index, url -> EstimateImage(imageId = -(index + 1).toLong(), imageUrl = url) },
            createdAt = createdAt,
            smsPreview = buildSmsPreview(request)
        )

        requestRecords[requestId] = RequestRecord(requestId, request, response, shops)
        val waitingShops = shops.map {
            EstimateWaitingShop(shopId = it.id, name = it.name, averageResponseMinutes = 30, expectedResponseMinutes = 20)
        }
        resultFlows[requestId] = MutableStateFlow(
            EstimateResultResponse(requestId = requestId, title = title, responses = emptyList(), waitingShops = waitingShops)
        )
        updateAdminSummaries()
        startDeliveryJob(requestId, shops)
        return response
    }

    /** 실시간 구독용. EstimateComparisonViewModel이 이 flow를 collect하면 응답이 도착할 때마다 화면이 갱신된다. */
    fun resultFlow(requestId: Long): StateFlow<EstimateResultResponse>? = resultFlows[requestId]?.asStateFlow()

    fun getEstimateResult(requestId: Long): EstimateResultResponse? = resultFlows[requestId]?.value

    fun getProposalDetail(proposalId: Long): EstimateProposalDetailResponse? {
        val record = proposals[proposalId] ?: return null
        val item = record.item
        return EstimateProposalDetailResponse(
            id = item.id,
            requestId = record.requestId,
            title = requestRecords[record.requestId]?.response?.title,
            shop = EstimateProposalShop(
                id = record.shop.id,
                name = record.shop.name,
                phoneNumber = record.shop.phoneNumber,
                address = record.shop.address,
                addressDetail = record.shop.addressDetail,
                latitude = record.shop.latitude,
                longitude = record.shop.longitude,
                rating = record.shop.rating,
                reviewCount = record.shop.reviewCount,
                businessHours = null,
                closedDays = null
            ),
            price = EstimatePrice(
                totalPrice = item.totalPrice,
                basePrice = item.totalPrice,
                removalPrice = item.removalPrice,
                designExtraPrice = null,
                optionExtraPrice = null
            ),
            memo = record.memo,
            estimatedDurationMinutes = item.estimatedDurationMinutes,
            canProvideService = item.canProvideService,
            isRemovalIncluded = item.isRemovalIncluded,
            status = item.status,
            proposalDateTimes = item.proposalDateTimes,
            createdAt = item.createdAt
        )
    }

    fun getProposalTimes(proposalId: Long): EstimateProposalTimesResponse? {
        val record = proposals[proposalId] ?: return null
        return EstimateProposalTimesResponse(estimateResponseId = proposalId, proposalTimes = record.times)
    }

    fun createReservation(proposalId: Long, timeId: Long): ReservationResponse? {
        val record = proposals[proposalId] ?: return null
        val time = record.times.find { it.id == timeId } ?: record.times.firstOrNull() ?: return null
        val requestRecord = requestRecords[record.requestId]
        val reservationId = reservationIdCounter.incrementAndGet()
        val totalPrice = record.item.totalPrice ?: 0
        val detail = ReservationDetailResponse(
            reservationId = reservationId,
            shopName = record.shop.name,
            shopPhoneNumber = record.shop.phoneNumber,
            shopRating = record.shop.rating,
            shopReviewCount = record.shop.reviewCount,
            shopClosedDays = emptyList(),
            distanceMeters = record.item.distanceMeters?.toDouble() ?: 0.0,
            address = record.shop.address,
            addressDetail = record.shop.addressDetail,
            reservedAt = time.proposalDateTime,
            basePrice = totalPrice,
            extraPrice = 0,
            optionPrice = 0,
            discountPrice = 0,
            totalPrice = totalPrice,
            memo = record.memo,
            nailType = requestRecord?.request?.nailType?.name ?: NailType.HAND.name,
            removalType = requestRecord?.request?.removalTypes?.firstOrNull()?.name ?: "NONE",
            images = requestRecord?.request?.images.orEmpty(),
            matchScore = 90,
            matchedImageUrls = emptyList(),
            recentReviews = emptyList(),
            status = "CONFIRMED",
            designName = null
        )
        reservations[reservationId] = detail
        reservationProposalIds[reservationId] = proposalId
        updateAdminSummaries()
        return ReservationResponse(
            reservationId = reservationId,
            shopName = record.shop.name,
            reservedAt = time.proposalDateTime,
            totalPrice = totalPrice,
            status = "CONFIRMED"
        )
    }

    fun getReservationDetail(reservationId: Long): ReservationDetailResponse? = reservations[reservationId]

    fun cancelReservation(reservationId: Long) {
        reservations[reservationId]?.let { current ->
            reservations[reservationId] = current.copy(status = "CANCELLED")
        }
    }

    /** 관리자 강제 응답: 대기 중인 코루틴을 취소하고 남은 매장 응답을 즉시 전부 반영한다. */
    fun forceRespondNow(requestId: Long) {
        deliveryJobs.remove(requestId)?.cancel()
        val flow = resultFlows[requestId] ?: return
        val requestRecord = requestRecords[requestId] ?: return
        val remaining = flow.value.waitingShops.mapNotNull { waiting -> requestRecord.allShops.find { it.id == waiting.shopId } }
        remaining.forEach { shop -> deliverResponse(requestId, shop) }
    }

    fun listItemsForEstimateList(): List<EstimateListItem> = requestRecords.values.sortedByDescending { it.requestId }.map { record ->
        val result = resultFlows[record.requestId]?.value
        val responses = result?.responses.orEmpty()
        val minPrice = responses.mapNotNull { it.totalPrice }.minOrNull()
        val lowest = responses.firstOrNull { it.totalPrice != null && it.totalPrice == minPrice }
        EstimateListItem(
            estimateId = record.requestId,
            title = record.response.title,
            images = record.response.images,
            nailType = record.response.nailType,
            removalTypes = record.response.removalTypes,
            schedules = record.response.schedules,
            createdAt = record.response.createdAt,
            status = if (result?.waitingShops.isNullOrEmpty()) "COMPLETED" else "MATCHING",
            proposalCount = responses.size,
            submittedShopCount = record.allShops.size,
            minPrice = minPrice,
            lowestPriceShop = lowest?.let { EstimateLowestPriceShop(shopId = it.shop.id, name = it.shop.name, address = it.shop.address) },
            radiusMeters = record.request.radiusMeters
        )
    }

    fun listItemsForReservationList(): List<ReservationListItem> = reservations.values.sortedByDescending { it.reservationId }.map { r ->
        ReservationListItem(
            reservationId = r.reservationId,
            proposalId = reservationProposalIds[r.reservationId] ?: 0L,
            reservedAt = r.reservedAt,
            status = r.status,
            shopName = r.shopName,
            shopThumbnailUrl = null,
            totalPrice = r.totalPrice,
            nailType = r.nailType,
            removalType = r.removalType
        )
    }

    private fun startDeliveryJob(requestId: Long, shops: List<DemoShop>) {
        val job = scope.launch {
            delay(10_000)
            for (shop in shops) {
                if (!isActive) return@launch
                deliverResponse(requestId, shop)
                delay(Random.nextLong(2_000, 4_500))
            }
            deliveryJobs.remove(requestId)
        }
        deliveryJobs[requestId] = job
    }

    private fun deliverResponse(requestId: Long, shop: DemoShop) {
        val requestRecord = requestRecords[requestId] ?: return
        val flow = resultFlows[requestId] ?: return
        val proposal = buildProposal(requestRecord, shop)
        proposals[proposal.item.id] = proposal
        flow.update { current ->
            val merged = (current.responses + proposal.item)
            val minPrice = merged.mapNotNull { it.totalPrice }.minOrNull()
            val recalculated = merged.map { it.copy(isLowestPrice = it.totalPrice != null && it.totalPrice == minPrice) }
            current.copy(
                responses = recalculated,
                waitingShops = current.waitingShops.filterNot { it.shopId == shop.id }
            )
        }
        updateAdminSummaries()
    }

    private fun buildProposal(requestRecord: RequestRecord, shop: DemoShop): ProposalRecord {
        val proposalId = proposalIdCounter.incrementAndGet()
        val priceMin = requestRecord.request.priceMin
        val priceMax = requestRecord.request.priceMax
        val totalPrice = if (priceMin != null && priceMax != null && priceMin <= priceMax) {
            Random.nextInt(priceMin, priceMax + 1)
        } else {
            (shop.basePrice + Random.nextInt(-8_000, 12_000)).coerceAtLeast(30_000)
        }
        val distance = DemoShops.all.indexOf(shop) * 260 + Random.nextInt(200, 500)
        val removalIncluded = requestRecord.request.removalTypes.any { it.name != "NONE" }
        val candidateTimes = proposalDateTimeCandidates(requestRecord.request.schedules)
        val chosenTimes = candidateTimes.shuffled().take(if (candidateTimes.size > 1) 2 else 1)
        val createdAt = nowIso()

        val item = EstimateResultResponseItem(
            id = proposalId,
            shop = EstimateResultShop(
                id = shop.id,
                name = shop.name,
                address = shop.address,
                latitude = shop.latitude,
                longitude = shop.longitude,
                rating = shop.rating,
                reviewCount = shop.reviewCount
            ),
            totalPrice = totalPrice,
            distanceMeters = distance,
            isLowestPrice = false,
            isRemovalIncluded = removalIncluded,
            removalPrice = null,
            estimatedDurationMinutes = 60 + Random.nextInt(0, 4) * 15,
            canProvideService = true,
            status = "SUBMITTED",
            proposalDateTimes = chosenTimes,
            createdAt = createdAt
        )
        val times = chosenTimes.mapIndexed { index, dateTime ->
            EstimateProposalTime(id = proposalTimeIdCounter.incrementAndGet(), proposalDateTime = dateTime, isSelected = index == 0)
        }
        return ProposalRecord(item = item, shop = shop, requestId = requestRecord.requestId, memo = memoFor(shop), times = times)
    }

    private fun updateAdminSummaries() {
        _activeRequests.value = requestRecords.values.sortedByDescending { it.requestId }.map { record ->
            val result = resultFlows[record.requestId]?.value
            DemoAdminRequestSummary(
                requestId = record.requestId,
                title = record.response.title ?: "데모 견적 #${record.requestId}",
                createdAt = record.response.createdAt,
                totalShops = record.allShops.size,
                respondedCount = result?.responses?.size ?: 0,
                waitingCount = result?.waitingShops?.size ?: 0
            )
        }
    }

    private fun buildTitle(request: EstimateRequest): String {
        val earliestDate = request.schedules.minByOrNull { it.date }?.date
        val typeLabel = when (request.nailType) {
            NailType.HAND -> "손"
            NailType.PEDICURE -> "발"
            NailType.BOTH -> "손+발"
        }
        return if (earliestDate != null) "$earliestDate $typeLabel 견적" else "$typeLabel 견적"
    }

    private fun buildSmsPreview(request: EstimateRequest): String {
        val removalLabel = request.removalTypes.joinToString(", ") { it.name }
        val dateLabel = request.schedules.joinToString(" / ") { it.date }
        return "[네일내일] 새로운 견적 요청이 도착했어요.\n" +
            "시술부위: ${request.nailType.name} · 제거: $removalLabel\n" +
            "희망일정: $dateLabel\n" +
            "답장으로 견적을 보내주시면 고객님께 바로 전달돼요."
    }

    private fun memoFor(shop: DemoShop): String = "${shop.name}입니다! 예약 전 궁금하신 점은 편하게 문의해주세요 :)"

    private fun proposalDateTimeCandidates(schedules: List<EstimateSchedule>): List<String> {
        val hourFor = mapOf(
            VisitTimeSlot.AM to "10:00",
            VisitTimeSlot.PM to "14:00",
            VisitTimeSlot.EVENING to "19:00",
            VisitTimeSlot.ANY to "11:00"
        )
        val candidates = schedules.flatMap { schedule -> schedule.times.map { slot -> "${schedule.date}T${hourFor[slot]}:00" } }
        return candidates.ifEmpty { listOf("${nowIso().substring(0, 10)}T11:00:00") }
    }

    private fun nowIso(): String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA).format(Date())
}
