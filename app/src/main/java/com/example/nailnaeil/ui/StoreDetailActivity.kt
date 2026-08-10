package com.example.nailnaeil.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nailnail.databinding.ActivityStoreDetailBinding

class StoreDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreDetailBinding
    private lateinit var designAdapter: StoreDesignAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
    }

    private fun setupRecyclerView() {
        // 더미 데이터 (임시 이미지 URL 대신 더미 문자열 사용)
        val dummyDesigns = listOf("img1", "img2", "img3", "img4", "img5")

        designAdapter = StoreDesignAdapter(dummyDesigns)
        // 가로 스크롤 설정
        binding.rvStoreDesign.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvStoreDesign.adapter = designAdapter
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnCancel.setOnClickListener { finish() }

        binding.btnSave.setOnClickListener {
            val comment = binding.etShopComment.text.toString()
            Toast.makeText(this, "매장 정보가 저장되었어!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}