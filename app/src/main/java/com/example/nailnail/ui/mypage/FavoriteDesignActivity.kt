package com.example.nailnail.ui.mypage

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nailnail.R

class FavoriteDesignActivity : AppCompatActivity() {

    private lateinit var favoriteDesignRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorite_design)

        applyWindowInsets()
        initViews()
        initRecyclerView()
        initClickListeners()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.favorite_design_root)
        ) { view, insets ->

            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

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
        favoriteDesignRecyclerView =
            findViewById(R.id.rv_favorite_design)
    }

    private fun initRecyclerView() {
        val favoriteDesignItems = listOf(
            FavoriteDesignItem(
                imageResource = R.drawable.img_nail01,
                shopName = "영찬 네일 강남점",
                rating = "4.8",
                reviewCount = "(124)",
                distance = "0.8km"
            ),
            FavoriteDesignItem(
                imageResource = R.drawable.img_nail02,
                shopName = "영찬 네일 강남점",
                rating = "4.8",
                reviewCount = "(124)",
                distance = "0.8km"
            ),
            FavoriteDesignItem(
                imageResource = R.drawable.img_nail03,
                shopName = "영찬 네일 강남점",
                rating = "4.8",
                reviewCount = "(124)",
                distance = "0.8km"
            ),
            FavoriteDesignItem(
                imageResource = R.drawable.img_nail04,
                shopName = "영찬 네일 강남점",
                rating = "4.8",
                reviewCount = "(124)",
                distance = "0.8km"
            ),

            // 피그마처럼 스크롤 목록을 보여주기 위해 반복
            FavoriteDesignItem(
                imageResource = R.drawable.img_nail01,
                shopName = "영찬 네일 강남점",
                rating = "4.8",
                reviewCount = "(124)",
                distance = "0.8km"
            ),
            FavoriteDesignItem(
                imageResource = R.drawable.img_nail02,
                shopName = "영찬 네일 강남점",
                rating = "4.8",
                reviewCount = "(124)",
                distance = "0.8km"
            ),
            FavoriteDesignItem(
                imageResource = R.drawable.img_nail03,
                shopName = "영찬 네일 강남점",
                rating = "4.8",
                reviewCount = "(124)",
                distance = "0.8km"
            ),
            FavoriteDesignItem(
                imageResource = R.drawable.img_nail04,
                shopName = "영찬 네일 강남점",
                rating = "4.8",
                reviewCount = "(124)",
                distance = "0.8km"
            )
        )

        favoriteDesignRecyclerView.layoutManager =
            GridLayoutManager(
                this,
                2
            )

        favoriteDesignRecyclerView.adapter =
            FavoriteDesignAdapter(
                items = favoriteDesignItems,
                onItemClick = { selectedItem ->
                    Toast.makeText(
                        this,
                        "${selectedItem.shopName} 디자인을 선택했습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

        favoriteDesignRecyclerView.setHasFixedSize(true)
    }

    private fun initClickListeners() {
        findViewById<ImageView>(R.id.iv_back)
            .setOnClickListener {
                finish()
            }
    }
}