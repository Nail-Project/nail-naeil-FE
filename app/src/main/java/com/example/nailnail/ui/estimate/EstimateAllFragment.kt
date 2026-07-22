package com.example.nailnail.ui.estimate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nailnail.R
import com.example.nailnail.adapter.EstimateAdapter
import com.example.nailnail.model.EstimateItem

class EstimateAllFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_estimate_list, container, false) // 탭별 fragment xml 이름에 맞춰 확인!

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 화면 테스트를 위한 가짜 데이터(Dummy Data) 생성
        val dummyList = listOf(
            EstimateItem(
                status = "응답중",
                replyStatus = "5개 중 2개 답변 도착",
                thumbnailUrl = "",
                styleKeywords = "무채색톤 | 프렌치 | 열손아트",
                options = "손 젤·젤 제거/파츠제거·근처 10km",
                dateTime = "07.04 (토) 오전",
                dateExtra = "외 2",
                shopName = "유네일",
                shopLocation = "서울 동작구",
                price = "66,500원"
            ),
            EstimateItem(
                status = "응답중",
                replyStatus = "5개 중 5개 답변 완료",
                thumbnailUrl = "",
                styleKeywords = "시럽톤 | 원컬러 | 파츠",
                options = "손 젤제거·근처 5km",
                dateTime = "07.05 (일) 오후",
                dateExtra = "외 1",
                shopName = "하루네일",
                shopLocation = "서울 관악구",
                price = "55,000원"
            )
        )

        // 어댑터 연결
        val adapter = EstimateAdapter(dummyList)
        recyclerView.adapter = adapter

        return view
    }
}