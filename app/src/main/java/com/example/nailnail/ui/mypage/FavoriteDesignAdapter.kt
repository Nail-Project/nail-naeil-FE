package com.example.nailnail.ui.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nailnail.R

data class FavoriteDesignItem(
    val imageResource: Int,
    val shopName: String,
    val rating: String,
    val reviewCount: String,
    val distance: String
)

class FavoriteDesignAdapter(
    private val items: List<FavoriteDesignItem>,
    private val onItemClick: (FavoriteDesignItem) -> Unit
) : RecyclerView.Adapter<FavoriteDesignAdapter.DesignViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DesignViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_favorite_design,
            parent,
            false
        )

        return DesignViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DesignViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class DesignViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val designImage: ImageView =
            itemView.findViewById(R.id.iv_design_image)

        private val shopName: TextView =
            itemView.findViewById(R.id.tv_shop_name)

        private val rating: TextView =
            itemView.findViewById(R.id.tv_rating)

        private val reviewCount: TextView =
            itemView.findViewById(R.id.tv_review_count)

        private val distance: TextView =
            itemView.findViewById(R.id.tv_distance)

        fun bind(item: FavoriteDesignItem) {
            designImage.setImageResource(item.imageResource)
            shopName.text = item.shopName
            rating.text = item.rating
            reviewCount.text = item.reviewCount
            distance.text = item.distance

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}