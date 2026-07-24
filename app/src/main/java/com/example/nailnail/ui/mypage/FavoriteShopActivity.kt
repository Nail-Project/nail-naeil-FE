package com.example.nailnail.ui.mypage

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nailnail.R

class FavoriteShopActivity : AppCompatActivity() {

    private lateinit var favoriteShopRecyclerView:
            RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(
            R.layout.activity_favorite_shop
        )

        applyWindowInsets()
        initViews()
        initRecyclerView()
        initClickListeners()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(
                R.id.favorite_shop_root
            )
        ) { view, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }
    }

    private fun initViews() {
        favoriteShopRecyclerView =
            findViewById(
                R.id.rv_favorite_shop
            )
    }

    private fun initRecyclerView() {
        val favoriteShopItems =
            listOf(
                FavoriteShopItem(
                    shopName = "유네일",
                    rating = "4.8",
                    reviewCount = "(312)",
                    location = "상도동",
                    firstImageResource =
                        R.drawable.nailshop1_1,
                    secondImageResource =
                        R.drawable.nailshop1_2,
                    thirdImageResource =
                        R.drawable.nailshop1_3,
                    isPopular = true
                ),

                FavoriteShopItem(
                    shopName = "빈벨네일",
                    rating = "4.2",
                    reviewCount = "(12)",
                    location = "상도동",
                    firstImageResource =
                        R.drawable.nailshop2_1,
                    secondImageResource =
                        R.drawable.nailshop2_2,
                    thirdImageResource =
                        R.drawable.nailshop2_3
                ),

                FavoriteShopItem(
                    shopName = "하밍네일",
                    rating = "4.9",
                    reviewCount = "(67)",
                    location = "신대방동",
                    firstImageResource =
                        R.drawable.nailshop3_1,
                    secondImageResource =
                        R.drawable.nailshop3_2,
                    thirdImageResource =
                        R.drawable.nailshop3_3
                )
            )

        favoriteShopRecyclerView.layoutManager =
            LinearLayoutManager(this)

        favoriteShopRecyclerView.adapter =
            FavoriteShopAdapter(
                items = favoriteShopItems,
                onItemClick = { selectedShop ->
                    Toast.makeText(
                        this,
                        "${selectedShop.shopName}을 선택했습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

        favoriteShopRecyclerView.setHasFixedSize(
            false
        )
    }

    private fun initClickListeners() {
        findViewById<ImageView>(
            R.id.iv_back
        ).setOnClickListener {
            finish()
        }
    }
}