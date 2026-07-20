package com.example.nailnail.ui.reservation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nailnail.R
import com.google.android.material.button.MaterialButton

class ReservationScheduleGuideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reservation_schedule_guide)

        applyWindowInsets()
        initClickListeners()
    }

    // 시스템 상태바와 내비게이션바 영역 반영
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.reservation_schedule_guide_root)
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

        findViewById<MaterialButton>(R.id.btn_go_back).setOnClickListener {
            finish()
        }

        findViewById<MaterialButton>(R.id.btn_copy_shop_contact)
            .setOnClickListener {
                copyToClipboard(
                    label = "샵 연락처",
                    text = SHOP_PHONE_NUMBER,
                    message = "샵 연락처가 복사되었습니다."
                )
            }
    }

    // 텍스트를 클립보드에 복사
    private fun copyToClipboard(
        label: String,
        text: String,
        message: String
    ) {
        val clipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val clipData = ClipData.newPlainText(
            label,
            text
        )

        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        // 추후 서버에서 받은 실제 네일샵 연락처로 변경
        private const val SHOP_PHONE_NUMBER = "02-1234-5678"
    }
}