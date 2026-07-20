package com.example.nailnail.ui.reservation

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nailnail.R
import com.google.android.material.button.MaterialButton

class ReservationCancelReasonActivity : AppCompatActivity() {

    private lateinit var reasonOptions: List<TextView>
    private lateinit var nextStepButton: MaterialButton

    private var selectedReason: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reservation_cancel_reason)

        applyWindowInsets()
        initViews()
        initClickListeners()
    }

    // 시스템 상태바와 내비게이션바 영역 반영
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.reservation_cancel_reason_root)
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

    // 화면에서 사용할 View 연결
    private fun initViews() {
        reasonOptions = listOf(
            findViewById(R.id.option_personal),
            findViewById(R.id.option_other_shop),
            findViewById(R.id.option_wrong_time),
            findViewById(R.id.option_service_changed),
            findViewById(R.id.option_other)
        )

        nextStepButton = findViewById(R.id.btn_next_step)
    }

    // 클릭 이벤트 설정
    private fun initClickListeners() {
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        findViewById<MaterialButton>(R.id.btn_cancel_and_back)
            .setOnClickListener {
                finish()
            }

        reasonOptions.forEach { option ->
            option.setOnClickListener {
                selectReason(option)
            }
        }

        nextStepButton.setOnClickListener {
            if (selectedReason == null) {
                return@setOnClickListener
            }

            val intent = Intent(
                this,
                ReservationCancelConfirmActivity::class.java
            )

            intent.putExtra(
                EXTRA_CANCEL_REASON,
                selectedReason
            )

            startActivity(intent)
        }
    }

    // 취소 사유 한 개 선택
    private fun selectReason(selectedOption: TextView) {
        reasonOptions.forEach { option ->
            option.setBackgroundResource(
                R.drawable.bg_cancel_reason_default
            )
        }

        selectedOption.setBackgroundResource(
            R.drawable.bg_cancel_reason_selected
        )

        selectedReason = selectedOption.text.toString()

        activateNextButton()
    }

    // 다음 단계 버튼 활성화
    private fun activateNextButton() {
        nextStepButton.isEnabled = true

        nextStepButton.backgroundTintList =
            ColorStateList.valueOf(
                Color.parseColor("#878787")
            )
    }

    companion object {
        const val EXTRA_CANCEL_REASON = "extra_cancel_reason"
    }
}