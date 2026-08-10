package com.example.nailnaeil.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nailnail.databinding.ActivityAdminMainBinding

class AdminMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.btnBack.setOnClickListener { finish() }

        // 1. 매거진 관리 리스트 화면으로 이동
        binding.cvMagazineManage.setOnClickListener {
            val intent = Intent(this, MagazineListActivity::class.java)
            startActivity(intent)
        }

        // 2. 매장 관리 리스트 화면으로 이동
        binding.cvStoreManage.setOnClickListener {
            val intent = Intent(this, StoreListActivity::class.java)
            startActivity(intent)
        }
    }
}