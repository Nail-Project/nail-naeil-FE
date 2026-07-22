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

class EstimateInProgressFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var estimateAdapter: EstimateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_estimate_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 진행 중인 항목들만 필터링해서 더미 데이터 구성 (isCompleted = false 인 것들)
        val inProgressList = listOf(
            EstimateItem(
                id = "1",
                status = "응답예정",
                replyStatus = "5개 답변 예정",
                thumbnailUrl = "",
                styleKeywords = "민트도트 | 크롬하츠 | 피아상골",
                options = "손 젤·젤 제거/파츠제거·근처 3km",
                dateTime = "07.04 (토) 오전",
                dateExtra = "외 2",
                hasLowestPrice = false,
                isCompleted = false
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
                priceText = "66,500원",
                isCompleted = false
            )
        )

        estimateAdapter = EstimateAdapter(inProgressList) { item ->
            Toast.makeText(requireContext(), "${item.styleKeywords} 선택됨!", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = estimateAdapter

        return view
    }
}