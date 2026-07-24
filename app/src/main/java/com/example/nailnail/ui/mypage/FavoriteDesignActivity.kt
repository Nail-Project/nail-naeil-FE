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

    private lateinit var favoriteDesignRecyclerView:
            RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(
            R.layout.activity_favorite_design
        )

        applyWindowInsets()
        initViews()
        initRecyclerView()
        initClickListeners()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(
                R.id.favorite_design_root
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
        favoriteDesignRecyclerView =
            findViewById(
                R.id.rv_favorite_design
            )
    }

    private fun initRecyclerView() {
        val favoriteDesignItems =
            listOf(
                FavoriteDesignItem(
                    imageResource =
                        R.drawable.img_nail_01,
                    designName =
                        "도트 프렌치 네일",
                    isPopular = true
                ),
                FavoriteDesignItem(
                    imageResource =
                        R.drawable.img_nail_02,
                    designName =
                        "민트 핑크 아트 네일"
                ),
                FavoriteDesignItem(
                    imageResource =
                        R.drawable.img_nail_03,
                    designName =
                        "레몬 포인트 네일"
                ),
                FavoriteDesignItem(
                    imageResource =
                        R.drawable.img_nail_04,
                    designName =
                        "프렌치 스팽글 네일"
                ),
                FavoriteDesignItem(
                    imageResource =
                        R.drawable.img_nail_05,
                    designName =
                        "반반 아트 네일"
                ),
                FavoriteDesignItem(
                    imageResource =
                        R.drawable.img_nail_06,
                    designName =
                        "프렌치 아트 네일"
                ),
                FavoriteDesignItem(
                    imageResource =
                        R.drawable.img_nail_07,
                    designName =
                        "레드 도트 네일"
                ),
                FavoriteDesignItem(
                    imageResource =
                        R.drawable.img_nail_08,
                    designName =
                        "블랙 시럽 네일",
                    isPopular = true
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
                        "${selectedItem.designName}을 선택했습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                },

                onHeartClick = { selectedItem ->
                    Toast.makeText(
                        this,
                        "${selectedItem.designName}의 찜을 해제했습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

        favoriteDesignRecyclerView.setHasFixedSize(
            true
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