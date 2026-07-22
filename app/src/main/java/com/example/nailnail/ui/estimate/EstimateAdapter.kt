package com.example.nailnail.ui.estimate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nailnail.R
import com.example.nailnail.model.EstimateItem
import com.google.android.material.button.MaterialButton

class EstimateAdapter(
    private val items: List<EstimateItem>,
    private val onItemClick: (EstimateItem) -> Unit
) : RecyclerView.Adapter<EstimateAdapter.EstimateViewHolder>() {

    class EstimateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivStatusBadge: ImageView = view.findViewById(R.id.ivStatusBadge)
        val tvReplyStatus: TextView = view.findViewById(R.id.tvReplyStatus)
        val ivThumbnail: ImageView = view.findViewById(R.id.ivThumbnail)
        val tvStyleKeywords: TextView = view.findViewById(R.id.tvStyleKeywords)
        val tvOptions: TextView = view.findViewById(R.id.tvOptions)
        val tvDateTime: TextView = view.findViewById(R.id.tvDateTime)
        val tvDateExtra: TextView = view.findViewById(R.id.tvDateExtra)

        // 최저가 및 샵 정보 영역
        val tvLowestPriceLabel: TextView = view.findViewById(R.id.tvLowestPriceLabel)
        val tvShopName: TextView = view.findViewById(R.id.tvShopName)
        val tvShopLocation: TextView = view.findViewById(R.id.tvShopLocation)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val divider: View = view.findViewById(R.id.divider)

        val btnViewDetails: MaterialButton = view.findViewById(R.id.btnViewDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstimateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estimate_card, parent, false)
        return EstimateViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstimateViewHolder, position: Int) {
        val item = items[position]

        // 새 EstimateItem 모델 프로퍼티에 맞게 바인딩
        holder.tvReplyStatus.text = item.replyStatus
        holder.tvStyleKeywords.text = item.styleKeywords
        holder.tvOptions.text = item.options
        holder.tvDateTime.text = item.dateTime
        holder.tvDateExtra.text = item.dateExtra

        // TODO: thumbnailUrl은 나중에 Glide나 Coil 같은 이미지 라이브러리로 바인딩하면 돼!
        // 예: Glide.with(holder.itemView.context).load(item.thumbnailUrl).into(holder.ivThumbnail)

        // 최저가 샵 정보가 있는 경우와 없는 경우 분기 처리
        if (item.hasLowestPrice) {
            holder.tvLowestPriceLabel.visibility = View.VISIBLE
            holder.tvShopName.visibility = View.VISIBLE
            holder.tvShopLocation.visibility = View.VISIBLE
            holder.tvPrice.visibility = View.VISIBLE
            holder.divider.visibility = View.VISIBLE

            holder.tvShopName.text = item.shopName
            holder.tvShopLocation.text = item.shopLocation
            holder.tvPrice.text = item.priceText
        } else {
            holder.tvLowestPriceLabel.visibility = View.GONE
            holder.tvShopName.visibility = View.GONE
            holder.tvShopLocation.visibility = View.GONE
            holder.tvPrice.visibility = View.GONE
            holder.divider.visibility = View.GONE
        }

        // 시술 완료 상태 여부에 따른 버튼 스타일 분기
        if (item.isCompleted) {
            holder.btnViewDetails.text = "시술이 완료된 견적입니다"
            holder.btnViewDetails.isEnabled = false
        } else {
            holder.btnViewDetails.text = "자세히 보기"
            holder.btnViewDetails.isEnabled = true
        }

        // 상세 화면으로 넘어가는 클릭 이벤트
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
        holder.btnViewDetails.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}