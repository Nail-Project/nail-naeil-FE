package com.example.nailnail.ui.reservation

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nailnail.R
import com.google.android.material.button.MaterialButton

class ReservationCancelConfirmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reservation_cancel_confirm)

        applyWindowInsets()
        showSelectedReason()
        initClickListeners()
    }

    // 시스템 상태바와 내비게이션바 영역 반영
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.reservation_cancel_confirm_root)
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

    // 이전 화면에서 전달받은 취소 사유 표시
    private fun showSelectedReason() {
        val selectedReason = intent.getStringExtra(
            ReservationCancelReasonActivity.EXTRA_CANCEL_REASON
        ) ?: "기타"

        findViewById<TextView>(R.id.tv_selected_reason).text =
            selectedReason
    }

    // 클릭 이벤트 설정
    private fun initClickListeners() {
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        findViewById<MaterialButton>(R.id.btn_go_back).setOnClickListener {
            finish()
        }

        findViewById<MaterialButton>(R.id.btn_confirm_cancel)
            .setOnClickListener {

                // 추후 예약 취소 API 호출로 교체
                Toast.makeText(
                    this,
                    "예약이 취소되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(
                    this,
                    ReservationActivity::class.java
                ).apply {
                    putExtra(
                        ReservationActivity.EXTRA_SHOW_PAST_TAB,
                        true
                    )

                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }

                startActivity(intent)
                finish()
            }
    }
}