package com.example.nailnail.ui.reservation

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nailnail.R
import com.google.android.material.card.MaterialCardView
import android.content.Intent

class ReservationManageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reservation_manage)

        applyWindowInsets()
        initClickListeners()
    }

    // 시스템 상태바와 내비게이션바 영역 반영
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.reservation_manage_root)
        ) { view, insets ->

            val systemBars = insets.getInsets(
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

    // 화면 클릭 이벤트 설정
    private fun initClickListeners() {
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        findViewById<MaterialCardView>(R.id.card_change_schedule)
            .setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        ReservationScheduleGuideActivity::class.java
                    )
                )
            }

        findViewById<MaterialCardView>(R.id.card_cancel_reservation)
            .setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        ReservationCancelReasonActivity::class.java
                    )
                )
            }
    }
}