package com.example.nailnail.ui.estimate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nailnail.databinding.ItemShopEstimateBinding
import com.example.nailnail.model.EstimateShop
import java.text.DecimalFormat

class EstimateShopAdapter(
    private val itemList: List<EstimateShop>,
    private val onReserveClick: (EstimateShop) -> Unit, // 예약하기 클릭 콜백
    private val onDetailClick: (EstimateShop) -> Unit   // 상세보기 클릭 콜백
) : RecyclerView.Adapter<EstimateShopAdapter.ShopViewHolder>() {

    private val priceFormat = DecimalFormat("#,###")

    inner class ShopViewHolder(private val binding: ItemShopEstimateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EstimateShop) {
            if (item.isResponded) {
                // ==========================================
                // 1. 견적 도착 완료 상태 (유네일 스타일)
                // ==========================================
                binding.groupWaiting.visibility = View.GONE
                binding.groupResponded.visibility = View.VISIBLE

                binding.tvRespShopName.text = item.shopName
                binding.tvRespPrice.text = "${priceFormat.format(item.price)}원"
                binding.tvRespMeta.text = "★ ${item.rating} (${item.reviewCount}) · 내 위치에서 ${item.distance}km"
                binding.tvShopComment.text = item.shopComment

                // 최저가 뱃지 표시 여부
                binding.tvRespTopBadge.visibility = if (item.isLowestPrice) View.VISIBLE else View.GONE

                // 예약하기 버튼 클릭 이벤트
                binding.btnReserve.setOnClickListener {
                    onReserveClick(item)
                }

                // 상세보기 버튼 클릭 이벤트
                binding.btnDetail.setOnClickListener {
                    onDetailClick(item)
                }

            } else {
                // ==========================================
                // 2. 응답 대기중 상태 (나나네일 스타일)
                // ==========================================
                binding.groupResponded.visibility = View.GONE
                binding.groupWaiting.visibility = View.VISIBLE

                binding.tvWaitShopName.text = item.shopName
                binding.tvWaitMeta.text = "★ ${item.rating} (${item.reviewCount})"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val binding = ItemShopEstimateBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ShopViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}