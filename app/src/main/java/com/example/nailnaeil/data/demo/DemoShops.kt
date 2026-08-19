package com.example.nailnaeil.data.demo

/**
 * 데모데이 시연용으로 고정 노출되는 매장 10곳.
 * phoneNumber는 실제 매장 번호가 아니라 팀에서 관리하는 안드로이드 기기 번호를 넣어야 한다.
 * TODO(팀): 데모 전에 name/address/phoneNumber를 실제 시연 매장 정보로 교체할 것.
 */
data class DemoShop(
    val id: Long,
    val name: String,
    val phoneNumber: String,
    val address: String,
    val addressDetail: String?,
    val latitude: Double,
    val longitude: Double,
    val rating: Double,
    val reviewCount: Int,
    val thumbnailImageUrl: String?,
    /** 데모 견적 응답 가격을 생성할 때 기준이 되는 가격대 */
    val basePrice: Int
)

object DemoShops {
    const val ID_BASE = 900_000_000L

    /** 서울 강남/신사 일대 좌표로 클러스터링한 데모 매장 10곳. */
    val all: List<DemoShop> = listOf(
        DemoShop(
            id = ID_BASE + 1, name = "글로우네일 강남점",
            phoneNumber = "010-0000-0001", // TODO(팀): 실제 관리 기기 번호로 교체
            address = "서울 강남구 강남대로 396", addressDetail = "2층",
            latitude = 37.4980, longitude = 127.0276,
            rating = 4.8, reviewCount = 312, thumbnailImageUrl = null, basePrice = 75_000
        ),
        DemoShop(
            id = ID_BASE + 2, name = "블링네일 신사점",
            phoneNumber = "010-0000-0002", // TODO(팀): 실제 관리 기기 번호로 교체
            address = "서울 강남구 도산대로 156", addressDetail = "3층",
            latitude = 37.5185, longitude = 127.0202,
            rating = 4.6, reviewCount = 204, thumbnailImageUrl = null, basePrice = 82_000
        ),
        DemoShop(
            id = ID_BASE + 3, name = "메리네일 청담점",
            phoneNumber = "010-0000-0003", // TODO(팀): 실제 관리 기기 번호로 교체
            address = "서울 강남구 압구정로 60길 11", addressDetail = null,
            latitude = 37.5245, longitude = 127.0473,
            rating = 4.9, reviewCount = 521, thumbnailImageUrl = null, basePrice = 95_000
        ),
        DemoShop(
            id = ID_BASE + 4, name = "소소네일 논현점",
            phoneNumber = "010-0000-0004", // TODO(팀): 실제 관리 기기 번호로 교체
            address = "서울 강남구 논현로 566", addressDetail = "1층",
            latitude = 37.5115, longitude = 127.0296,
            rating = 4.5, reviewCount = 158, thumbnailImageUrl = null, basePrice = 68_000
        ),
        DemoShop(
            id = ID_BASE + 5, name = "핑크젤 역삼점",
            phoneNumber = "010-0000-0005", // TODO(팀): 실제 관리 기기 번호로 교체
            address = "서울 강남구 테헤란로 152", addressDetail = "4층",
            latitude = 37.5006, longitude = 127.0365,
            rating = 4.7, reviewCount = 276, thumbnailImageUrl = null, basePrice = 78_000
        ),
        DemoShop(
            id = ID_BASE + 6, name = "라라네일 삼성점",
            phoneNumber = "010-0000-0006", // TODO(팀): 실제 관리 기기 번호로 교체
            address = "서울 강남구 봉은사로 429", addressDetail = "2층",
            latitude = 37.5089, longitude = 127.0632,
            rating = 4.4, reviewCount = 133, thumbnailImageUrl = null, basePrice = 65_000
        ),
        DemoShop(
            id = ID_BASE + 7, name = "데이지네일 서초점",
            phoneNumber = "010-0000-0007", // TODO(팀): 실제 관리 기기 번호로 교체
            address = "서울 서초구 서초대로 398", addressDetail = "5층",
            latitude = 37.4933, longitude = 127.0146,
            rating = 4.6, reviewCount = 189, thumbnailImageUrl = null, basePrice = 72_000
        ),
        DemoShop(
            id = ID_BASE + 8, name = "모아네일 압구정점",
            phoneNumber = "010-0000-0008", // TODO(팀): 실제 관리 기기 번호로 교체
            address = "서울 강남구 압구정로 30길 8", addressDetail = null,
            latitude = 37.5274, longitude = 127.0286,
            rating = 4.9, reviewCount = 402, thumbnailImageUrl = null, basePrice = 105_000
        ),
        DemoShop(
            id = ID_BASE + 9, name = "쥬얼리네일 선릉점",
            phoneNumber = "010-0000-0009", // TODO(팀): 실제 관리 기기 번호로 교체
            address = "서울 강남구 선릉로 428", addressDetail = "3층",
            latitude = 37.5045, longitude = 127.0490,
            rating = 4.5, reviewCount = 167, thumbnailImageUrl = null, basePrice = 70_000
        ),
        DemoShop(
            id = ID_BASE + 10, name = "코코네일 대치점",
            phoneNumber = "010-0000-0010", // TODO(팀): 실제 관리 기기 번호로 교체
            address = "서울 강남구 남부순환로 2921", addressDetail = "2층",
            latitude = 37.4944, longitude = 127.0630,
            rating = 4.7, reviewCount = 245, thumbnailImageUrl = null, basePrice = 80_000
        )
    )

    fun byId(id: Long): DemoShop? = all.find { it.id == id }

    fun isDemoShopId(id: Long): Boolean = id in ID_BASE + 1..ID_BASE + all.size
}
