package com.example.nailnail.ui.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nailnail.R

class FavoriteDesignAdapter(
    private val items: List<FavoriteDesignItem>,
    private val onItemClick: (FavoriteDesignItem) -> Unit,
    private val onHeartClick: (FavoriteDesignItem) -> Unit
) : RecyclerView.Adapter<FavoriteDesignAdapter.FavoriteDesignViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteDesignViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_favorite_design,
                    parent,
                    false
                )

        return FavoriteDesignViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: FavoriteDesignViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =
        items.size

    inner class FavoriteDesignViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val designImage: ImageView =
            itemView.findViewById(
                R.id.iv_design_image
            )

        private val designName: TextView =
            itemView.findViewById(
                R.id.tv_design_name
            )

        private val popularBadge: TextView =
            itemView.findViewById(
                R.id.tv_popular_badge
            )

        private val favoriteHeart: ImageView =
            itemView.findViewById(
                R.id.iv_favorite_heart
            )

        fun bind(item: FavoriteDesignItem) {
            designImage.setImageResource(
                item.imageResource
            )

            designName.text =
                item.designName

            popularBadge.visibility =
                if (item.isPopular) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

            itemView.setOnClickListener {
                onItemClick(item)
            }

            favoriteHeart.setOnClickListener {
                onHeartClick(item)
            }
        }
    }
}