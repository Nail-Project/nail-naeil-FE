package com.example.nailnaeil.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nailnail.R
import com.example.nailnail.databinding.ActivityStoreListBinding

class StoreListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreListBinding
    private lateinit var adapter: StoreListAdapter
    private val storeData = mutableListOf<StoreItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
    }

    private fun setupRecyclerView() {
        storeData.apply {
            add(StoreItem("유네일", "서울 동작구 장승배기로10길 100", 4.8, 312))
            add(StoreItem("빈벨네일", "서울 동작구 상도로30길 39", 4.2, 56))
            add(StoreItem("하밍네일", "서울 동작구 신대방동 724", 4.1, 129))
            add(StoreItem("루나문뷰티", "보라매로5길 51 롯데타워 지하 1층", 4.9, 17))
            add(StoreItem("네일앤제이", "서울 동작구 사당로26길 81", 4.6, 72))
        }

        adapter = StoreListAdapter(storeData)
        binding.rvStoreList.layoutManager = LinearLayoutManager(this)
        binding.rvStoreList.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }

        // 1. 선택 버튼 클릭 시 다중 선택 모드 토글
        binding.tvSelectMode.setOnClickListener {
            adapter.isSelectionMode = !adapter.isSelectionMode

            if (adapter.isSelectionMode) {
                binding.tvSelectMode.text = "취소"
                binding.layoutBottomDelete.visibility = View.VISIBLE
            } else {
                binding.tvSelectMode.text = "선택"
                binding.layoutBottomDelete.visibility = View.GONE
                storeData.forEach { it.isSelected = false }
                binding.tvSelectedCount.text = "0개의 매장 선택됨"
            }
            adapter.notifyDataSetChanged()
        }

        // 2. 선택 개수 업데이트 콜백
        adapter.onSelectionChanged = { count ->
            binding.tvSelectedCount.text = "${count}개의 매장 선택됨"
        }

        // 3. 삭제(휴지통) 버튼 클릭
        binding.btnDelete.setOnClickListener {
            val selectedItems = storeData.filter { it.isSelected }
            if (selectedItems.isEmpty()) return@setOnClickListener

            showDeleteDialog(selectedItems)
        }
    }

    private fun showDeleteDialog(selectedItems: List<StoreItem>) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_confirm, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvMessage = dialogView.findViewById<TextView>(R.id.tv_dialog_message)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btn_confirm)

        val firstItemName = selectedItems[0].name
        val extraCount = selectedItems.size - 1

        // 기획안(image_456bcb.jpg)처럼 텍스트 세팅
        tvMessage.text = if (extraCount > 0) {
            "'$firstItemName' 외 ${extraCount}건\n매장을 삭제할까요?"
        } else {
            "'$firstItemName'\n매장을 삭제할까요?"
        }

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnConfirm.setOnClickListener {
            // 실제 리스트에서 삭제 처리
            storeData.removeAll(selectedItems)
            adapter.notifyDataSetChanged()
            dialog.dismiss()
            binding.tvSelectMode.performClick() // 삭제 후 선택 모드 해제
        }

        dialog.show()
    }
}