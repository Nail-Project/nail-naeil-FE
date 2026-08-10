package com.example.nailnaeil.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nailnail.databinding.ItemMagazineAddBinding
import com.example.nailnail.databinding.ItemMagazineBinding

// 데이터 모델
data class MagazineItem(
    val isAddButton: Boolean = false,
    val title: String = "",
    val views: Int = 0,
    val likes: Int = 0,
    var isSelected: Boolean = false
)

class MagazineListAdapter(private val itemList: MutableList<MagazineItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isSelectionMode = false // 다중 선택 모드 활성화 여부
    var onSelectionChanged: ((Int) -> Unit)? = null // 선택된 개수 전달용 콜백

    companion object {
        const val VIEW_TYPE_ADD = 0
        const val VIEW_TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].isAddButton) VIEW_TYPE_ADD else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_ADD) {
            val binding = ItemMagazineAddBinding.inflate(inflater, parent, false)
            AddViewHolder(binding)
        } else {
            val binding = ItemMagazineBinding.inflate(inflater, parent, false)
            MagazineViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]

        if (holder is MagazineViewHolder) {
            holder.bind(item)

            // 선택 모드 UI 업데이트 (어두운 배경 및 체크 아이콘 표시)
            holder.binding.viewSelectionOverlay.visibility = if (item.isSelected) View.VISIBLE else View.GONE
            holder.binding.ivCheck.visibility = if (item.isSelected) View.VISIBLE else View.GONE

            // 아이템 클릭 이벤트
            holder.itemView.setOnClickListener {
                if (isSelectionMode) {
                    item.isSelected = !item.isSelected // 상태 반전
                    notifyItemChanged(position) // 클릭한 아이템만 갱신

                    // 몇 개 선택됐는지 계산해서 액티비티로 전달
                    val selectedCount = itemList.count { it.isSelected }
                    onSelectionChanged?.invoke(selectedCount)
                } else {
                    // 일반 모드: 매거진 상세 페이지로 이동
                    val intent = Intent(holder.itemView.context, MagazineDetailActivity::class.java)
                    holder.itemView.context.startActivity(intent)
                }
            }
        } else if (holder is AddViewHolder) {
            // 등록하기 버튼 클릭 이벤트
            holder.itemView.setOnClickListener {
                if (!isSelectionMode) {
                    // 일반 모드: 매거진 등록 액티비티(상세 레이아웃 재활용)로 이동
                    val intent = Intent(holder.itemView.context, MagazineDetailActivity::class.java)
                    holder.itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    inner class MagazineViewHolder(val binding: ItemMagazineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MagazineItem) {
            binding.tvTitle.text = item.title
            binding.tvViewsLikes.text = "조회수 ${item.views} · 좋아요 ${item.likes}"
        }
    }

    inner class AddViewHolder(binding: ItemMagazineAddBinding) :
        RecyclerView.ViewHolder(binding.root)
}