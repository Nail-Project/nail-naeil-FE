package com.example.nailnail.ui.reservation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nailnail.R
import com.google.android.material.button.MaterialButton

class ReservationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reservation_detail)

        applyWindowInsets()
        initClickListeners()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.reservation_detail_root)
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

    private fun initClickListeners() {
        // 뒤로가기
        findViewById<ImageView>(
            R.id.iv_detail_back
        ).setOnClickListener {
            finish()
        }

        // 위치 영역
        val showLocationMessage =
            View.OnClickListener {
                Toast.makeText(
                    this,
                    "매장 위치 화면은 추후 연결됩니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        findViewById<View>(
            R.id.layout_detail_location
        ).setOnClickListener(
            showLocationMessage
        )

        findViewById<View>(
            R.id.layout_detail_open_map
        ).setOnClickListener(
            showLocationMessage
        )

        // 예약 변경 중앙 팝업
        findViewById<MaterialButton>(
            R.id.btn_detail_change_reservation
        ).setOnClickListener {
            ReservationChangeDialogFragment()
                .show(
                    supportFragmentManager,
                    "ReservationChangeDialog"
                )
        }

        findViewById<MaterialButton>(
            R.id.btn_detail_cancel_reservation
        ).setOnClickListener {
            ReservationCancelConfirmBottomSheet()
                .show(
                    supportFragmentManager,
                    "ReservationCancelConfirmBottomSheet"
                )
        }
    }
}