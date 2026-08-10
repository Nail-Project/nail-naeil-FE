package com.example.nailnaeil.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nailnail.databinding.ItemStoreDesignBinding

class StoreDesignAdapter(private val designList: List<String>) :
    RecyclerView.Adapter<StoreDesignAdapter.DesignViewHolder>() {

    inner class DesignViewHolder(val binding: ItemStoreDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            // TODO: 나중에 Glide 같은 라이브러리로 imageUrl을 ivDesignImage에 로드해주면 돼!
            // Glide.with(itemView.context).load(imageUrl).into(binding.ivDesignImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DesignViewHolder {
        val binding = ItemStoreDesignBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DesignViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DesignViewHolder, position: Int) {
        holder.bind(designList[position])
    }

    override fun getItemCount(): Int = designList.size
}