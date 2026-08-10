package com.example.nailnaeil.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nailnail.R
import com.example.nailnail.databinding.ActivityMagazineListBinding

class MagazineListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMagazineListBinding
    private lateinit var adapter: MagazineListAdapter
    // 데이터를 추가/삭제하기 위해 MutableList 사용
    private val magazineData = mutableListOf<MagazineItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMagazineListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTabs()
        setupRecyclerView()
        setupListeners()
    }

    private fun setupTabs() {
        val categories = listOf("전체", "인기", "심플", "마그넷", "프렌치", "파츠", "아트")
        for (category in categories) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(category))
        }
    }

    private fun setupRecyclerView() {
        // 초기 더미 데이터 세팅 (인덱스 0번은 항상 등록하기 버튼)
        magazineData.apply {
            add(MagazineItem(isAddButton = true))
            add(MagazineItem(title = "화이트 그라데이션 네일", views = 1204, likes = 312))
            add(MagazineItem(title = "도트 프렌치 네일", views = 7539, likes = 501))
            add(MagazineItem(title = "블루 웨이브 네일", views = 452, likes = 89))
            add(MagazineItem(title = "글리터 자석 네일", views = 892, likes = 120))
        }

        adapter = MagazineListAdapter(magazineData)
        binding.rvMagazineList.layoutManager = GridLayoutManager(this, 2)
        binding.rvMagazineList.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }

        // 1. 우측 상단 '선택' 버튼 클릭 시 모드 전환
        binding.tvSelectMode.setOnClickListener {
            adapter.isSelectionMode = !adapter.isSelectionMode

            if (adapter.isSelectionMode) {
                binding.tvSelectMode.text = "취소"
                binding.layoutBottomDelete.visibility = View.VISIBLE
            } else {
                binding.tvSelectMode.text = "선택"
                binding.layoutBottomDelete.visibility = View.GONE
                // 모든 선택 초기화
                magazineData.forEach { it.isSelected = false }
                adapter.notifyDataSetChanged() // 전체 리스트 갱신
                binding.tvSelectedCount.text = "0개의 매거진 선택됨"
            }
        }

        // 2. 어댑터에서 선택 개수가 바뀔 때 하단 바 텍스트 업데이트
        adapter.onSelectionChanged = { count ->
            binding.tvSelectedCount.text = "${count}개의 매거진 선택됨"
        }

        // 3. 하단 바의 휴지통 아이콘 클릭 시 커스텀 다이얼로그 띄우기
        binding.btnDelete.setOnClickListener {
            val selectedItems = magazineData.filter { it.isSelected }
            if (selectedItems.isEmpty()) return@setOnClickListener

            showDeleteDialog(selectedItems)
        }
    }

    private fun showDeleteDialog(selectedItems: List<MagazineItem>) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_confirm, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // 팝업 배경 투명하게 (커스텀 둥근 테두리 적용을 위해)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvMessage = dialogView.findViewById<TextView>(R.id.tv_dialog_message)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btn_confirm)

        // 텍스트 동적 세팅 (예: '도트 프렌치 네일' 외 1건 매거진을 삭제할까요?)
        val firstItemName = selectedItems[0].title
        val extraCount = selectedItems.size - 1

        tvMessage.text = if (extraCount > 0) {
            "'$firstItemName' 외 ${extraCount}건\n매거진을 삭제할까요?"
        } else {
            "'$firstItemName'\n매거진을 삭제할까요?"
        }

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnConfirm.setOnClickListener {
            // 실제 삭제 로직 처리
            magazineData.removeAll(selectedItems)
            adapter.notifyDataSetChanged() // 삭제된 후 리스트 화면 갱신

            dialog.dismiss()

            // 삭제 후 선택 모드 종료 (취소 버튼을 누른 것과 동일하게 처리)
            binding.tvSelectMode.performClick()
        }

        dialog.show()
    }
}