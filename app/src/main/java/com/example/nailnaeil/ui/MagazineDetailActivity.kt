package com.example.nailnaeil.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nailnail.databinding.ActivityMagazineDetailBinding
import com.example.nailnail.databinding.ItemRecentCaseBinding
import com.example.nailnail.databinding.ItemSimilarDesignBinding

// --- 데이터 모델 ---
data class RecentCase(val shopName: String, val price: String)
data class SimilarDesign(val title: String)

// --- 어댑터 1: 최근 전체 사례 ---
class RecentCaseAdapter(private val list: List<RecentCase>) :
    RecyclerView.Adapter<RecentCaseAdapter.CaseViewHolder>() {

    inner class CaseViewHolder(val binding: ItemRecentCaseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecentCase) {
            binding.tvShopName.text = item.shopName
            binding.tvPrice.text = item.price
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val binding = ItemRecentCaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}

// --- 어댑터 2: 비슷한 디자인 ---
class SimilarDesignAdapter(private val list: List<SimilarDesign>) :
    RecyclerView.Adapter<SimilarDesignAdapter.DesignViewHolder>() {

    inner class DesignViewHolder(val binding: ItemSimilarDesignBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SimilarDesign) {
            binding.tvDesignTitle.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DesignViewHolder {
        val binding = ItemSimilarDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DesignViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DesignViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}

// --- 메인 액티비티 ---
class MagazineDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMagazineDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMagazineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        setupListeners()
    }

    private fun setupRecyclerViews() {
        // 1. 최근 전체 사례 세팅 (세로 방향)
        val caseData = listOf(
            RecentCase("네일 봄", "₩ 68,500"),
            RecentCase("4픽네일", "₩ 73,000")
        )
        binding.rvRecentCases.layoutManager = LinearLayoutManager(this)
        binding.rvRecentCases.adapter = RecentCaseAdapter(caseData)

        // 2. 비슷한 디자인 세팅 (가로 방향)
        val designData = listOf(
            SimilarDesign("모래 모래"),
            SimilarDesign("얼음 프렌치 네일"),
            SimilarDesign("연두 도트 퐁당"),
            SimilarDesign("심플 프렌치")
        )
        binding.rvSimilarDesigns.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSimilarDesigns.adapter = SimilarDesignAdapter(designData)
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnClose.setOnClickListener { finish() }
        binding.btnCancel.setOnClickListener { finish() }

        binding.btnSave.setOnClickListener {
            Toast.makeText(this, "성공적으로 저장되었어!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}