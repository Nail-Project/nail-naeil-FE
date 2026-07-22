package com.example.nailnail.ui.estimate

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class EstimatePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    // 탭 개수 (전체, 진행중 -> 총 2개)
    override fun getItemCount(): Int = 2

    // 포지션에 따라 보여줄 프래그먼트 설정
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EstimateAllFragment()        // 0번 탭: '전체' 화면
            1 -> EstimateInProgressFragment() // 1번 탭: '진행중' 화면
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}