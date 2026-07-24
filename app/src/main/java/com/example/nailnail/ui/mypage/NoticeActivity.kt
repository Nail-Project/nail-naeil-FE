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

class NoticeActivity : AppCompatActivity() {

    private lateinit var noticeRecyclerView:
            RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notice)

        applyWindowInsets()
        initViews()
        initRecyclerView()
        initClickListeners()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.notice_root)
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
        noticeRecyclerView =
            findViewById(R.id.rv_notice)
    }

    private fun initRecyclerView() {
        val noticeItems =
            listOf(
                NoticeItem(
                    title =
                        "[공지] 네일내일 서비스 이용약관 개정 안내",
                    date =
                        "2026년 07월 01일",
                    isHighlighted = true
                ),
                NoticeItem(
                    title =
                        "[공지] 네일내일 서비스 이용약관 개정 안내",
                    date =
                        "2026년 07월 01일"
                ),
                NoticeItem(
                    title =
                        "[공지] 네일내일 서비스 이용약관 개정 안내",
                    date =
                        "2026년 07월 01일"
                ),
                NoticeItem(
                    title =
                        "[공지] 네일내일 서비스 이용약관 개정 안내",
                    date =
                        "2026년 07월 01일"
                ),
                NoticeItem(
                    title =
                        "[공지] 네일내일 서비스 이용약관 개정 안내",
                    date =
                        "2026년 07월 01일"
                ),
                NoticeItem(
                    title =
                        "[공지] 네일내일 서비스 이용약관 개정 안내",
                    date =
                        "2026년 07월 01일"
                ),
                NoticeItem(
                    title =
                        "[공지] 네일내일 서비스 이용약관 개정 안내",
                    date =
                        "2026년 07월 01일"
                ),
                NoticeItem(
                    title =
                        "[공지] 네일내일 서비스 이용약관 개정 안내",
                    date =
                        "2026년 07월 01일"
                ),
                NoticeItem(
                    title =
                        "[공지] 네일내일 서비스 이용약관 개정 안내",
                    date =
                        "2026년 07월 01일"
                )
            )

        noticeRecyclerView.layoutManager =
            LinearLayoutManager(this)

        noticeRecyclerView.adapter =
            NoticeAdapter(
                items = noticeItems,
                onItemClick = { selectedNotice ->
                    Toast.makeText(
                        this,
                        selectedNotice.title,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

        noticeRecyclerView.setHasFixedSize(
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