package com.example.nailnail.ui.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nailnail.R

class FavoriteShopAdapter(
    private val items: List<FavoriteShopItem>,
    private val onItemClick: (FavoriteShopItem) -> Unit
) : RecyclerView.Adapter<FavoriteShopAdapter.FavoriteShopViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteShopViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_favorite_shop,
                    parent,
                    false
                )

        return FavoriteShopViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: FavoriteShopViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =
        items.size

    inner class FavoriteShopViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val popularBadge: TextView =
            itemView.findViewById(
                R.id.tv_shop_popular_badge
            )

        private val shopName: TextView =
            itemView.findViewById(
                R.id.tv_favorite_shop_name
            )

        private val rating: TextView =
            itemView.findViewById(
                R.id.tv_favorite_shop_rating
            )

        private val review: TextView =
            itemView.findViewById(
                R.id.tv_favorite_shop_review
            )

        private val location: TextView =
            itemView.findViewById(
                R.id.tv_favorite_shop_location
            )

        private val firstImage: ImageView =
            itemView.findViewById(
                R.id.iv_favorite_shop_first
            )

        private val secondImage: ImageView =
            itemView.findViewById(
                R.id.iv_favorite_shop_second
            )

        private val thirdImage: ImageView =
            itemView.findViewById(
                R.id.iv_favorite_shop_third
            )

        fun bind(item: FavoriteShopItem) {
            shopName.text =
                item.shopName

            rating.text =
                item.rating

            review.text =
                item.reviewCount

            location.text =
                item.location

            firstImage.setImageResource(
                item.firstImageResource
            )

            secondImage.setImageResource(
                item.secondImageResource
            )

            thirdImage.setImageResource(
                item.thirdImageResource
            )

            popularBadge.visibility =
                if (item.isPopular) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}