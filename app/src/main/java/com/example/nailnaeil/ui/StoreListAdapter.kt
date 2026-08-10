package com.example.nailnaeil.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nailnail.databinding.ItemStoreListBinding

// 데이터 모델
data class StoreItem(
    val name: String,
    val address: String,
    val rating: Double,
    val reviewCount: Int,
    var isSelected: Boolean = false
)

class StoreListAdapter(private var storeList: MutableList<StoreItem>) :
    RecyclerView.Adapter<StoreListAdapter.StoreViewHolder>() {

    var isSelectionMode = false // 다중 선택 모드 활성화 여부
    var onSelectionChanged: ((Int) -> Unit)? = null // 선택된 개수 전달용 콜백

    inner class StoreViewHolder(val binding: ItemStoreListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StoreItem) {
            binding.tvStoreName.text = item.name
            binding.tvStoreAddress.text = item.address
            binding.tvRatingReview.text = "${item.rating} (${item.reviewCount})"

            // 선택 모드 UI 업데이트
            binding.viewSelectionOverlay.visibility = if (item.isSelected) View.VISIBLE else View.GONE
            binding.ivCheck.visibility = if (item.isSelected) View.VISIBLE else View.GONE
            // 선택 모드일 땐 기본 우측 화살표 숨기기
            binding.ivArrow.visibility = if (isSelectionMode) View.INVISIBLE else View.VISIBLE

            // 아이템 클릭 이벤트
            itemView.setOnClickListener {
                if (isSelectionMode) {
                    item.isSelected = !item.isSelected
                    notifyItemChanged(adapterPosition)

                    val selectedCount = storeList.count { it.isSelected }
                    onSelectionChanged?.invoke(selectedCount)
                } else {
                    // 일반 모드: 매장 상세 페이지로 이동
                    val intent = Intent(itemView.context, StoreDetailActivity::class.java)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding = ItemStoreListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bind(storeList[position])
    }

    override fun getItemCount(): Int = storeList.size

    // 외부에서 리스트를 통째로 갱신할 때 사용하는 함수 (검색 등)
    fun updateList(newList: MutableList<StoreItem>) {
        storeList = newList
        notifyDataSetChanged()
    }
}