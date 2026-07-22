package com.example.nailnail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.example.nailnail.R
import com.example.nailnail.model.EstimateItem

class EstimateAdapter(private val items: List<EstimateItem>) :
    RecyclerView.Adapter<EstimateAdapter.EstimateViewHolder>() {

    class EstimateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvReplyStatus: TextView = view.findViewById(R.id.tvReplyStatus)
        val ivThumbnail: ShapeableImageView = view.findViewById(R.id.ivThumbnail)
        val tvStyleKeywords: TextView = view.findViewById(R.id.tvStyleKeywords)
        val tvOptions: TextView = view.findViewById(R.id.tvOptions)
        val tvDateTime: TextView = view.findViewById(R.id.tvDateTime)
        val tvDateExtra: TextView = view.findViewById(R.id.tvDateExtra)
        val tvShopName: TextView = view.findViewById(R.id.tvShopName)
        val tvShopLocation: TextView = view.findViewById(R.id.tvShopLocation)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val btnViewDetails: View = view.findViewById(R.id.btnViewDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstimateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estimate_card, parent, false)
        return EstimateViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstimateViewHolder, position: Int) {
        val item = items[position]

        holder.tvReplyStatus.text = item.replyStatus
        holder.tvStyleKeywords.text = item.styleKeywords
        holder.tvOptions.text = item.options
        holder.tvDateTime.text = item.dateTime
        holder.tvDateExtra.text = item.dateExtra
        holder.tvShopName.text = item.shopName
        holder.tvShopLocation.text = item.shopLocation
        holder.tvPrice.text = item.price

        // TODO: 이미지 로딩 라이브러리(Glide나 Glide/Coil 등) 쓰게 되면 ivThumbnail에 썸네일 세팅해주면 돼!

        // 상세보기 버튼 클릭 이벤트 (나중에 4.2 상세 화면으로 넘어갈 때 여기에 리스너 달면 됨!)
        holder.btnViewDetails.setOnClickListener {
            // 상세 페이지로 이동하는 로직 연결 예정
        }
    }

    override fun getItemCount(): Int = items.size
}