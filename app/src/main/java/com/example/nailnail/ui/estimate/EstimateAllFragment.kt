package com.example.nailnail.ui.estimate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nailnail.R
import com.example.nailnail.model.EstimateItem

class EstimateAllFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var estimateAdapter: EstimateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_estimate_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 와이어프레임에 맞춘 더미 데이터 생성
        val dummyList = listOf(
            EstimateItem(
                id = "1",
                status = "응답예정",
                replyStatus = "5개 답변 예정",
                thumbnailUrl = "",
                styleKeywords = "민트도트 | 크롬하츠 | 피아상골",
                options = "손 젤·젤 제거/파츠제거·근처 3km",
                dateTime = "07.04 (토) 오전",
                dateExtra = "외 2",
                hasLowestPrice = false
            ),
            EstimateItem(
                id = "2",
                status = "응답중",
                replyStatus = "5개 중 2개 답변 도착",
                thumbnailUrl = "",
                styleKeywords = "무채색톤 | 프렌치 | 열손아트",
                options = "손 젤·젤 제거/파츠제거·근처 10km",
                dateTime = "07.04 (토) 오전",
                dateExtra = "외 2",
                hasLowestPrice = true,
                shopName = "유네일",
                shopLocation = "서울 동작구",
                priceText = "66,500원"
            ),
            EstimateItem(
                id = "3",
                status = "시술완료",
                replyStatus = "5개 중 5개 답변 도착",
                thumbnailUrl = "",
                styleKeywords = "아이보리 베이스 | 레이스 아트",
                options = "손 젤·젤 제거·근처 8km",
                dateTime = "07.04 (토) 오전",
                dateExtra = "외 2",
                hasLowestPrice = true,
                shopName = "하루네일",
                shopLocation = "서울 동작구",
                priceText = "65,000원",
                isCompleted = true
            )
        )

        // 어댑터 연결 및 클릭 이벤트 처리 (상세 화면으로 이동 등)
        estimateAdapter = EstimateAdapter(dummyList) { item ->
            Toast.makeText(requireContext(), "${item.styleKeywords} 선택됨!", Toast.LENGTH_SHORT).show()
            // TODO: 나중에 4.2 상세 화면(DetailActivity 또는 Fragment)으로 전환하는 코드 연결
        }

        recyclerView.adapter = estimateAdapter

        return view
    }
}