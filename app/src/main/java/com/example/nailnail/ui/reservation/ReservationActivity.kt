package com.example.nailnail.ui.reservation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nailnail.R
import com.google.android.material.button.MaterialButton

class ReservationActivity : AppCompatActivity() {

    private lateinit var confirmedTab: TextView
    private lateinit var pastTab: TextView
    private lateinit var confirmedIndicator: View
    private lateinit var pastIndicator: View

    private lateinit var reservationStatus: TextView
    private lateinit var visitNoticeTitle: TextView
    private lateinit var visitNotice: TextView
    private lateinit var pastReservationNotice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reservation)

        applyWindowInsets()
        initViews()
        initClickListeners()
        showConfirmedReservation()
    }

    // 시스템 상태바와 내비게이션 영역을 화면에서 피하도록 설정
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reservation_root)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

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
        confirmedTab = findViewById(R.id.tv_confirmed_tab)
        pastTab = findViewById(R.id.tv_past_tab)

        confirmedIndicator = findViewById(R.id.view_confirmed_indicator)
        pastIndicator = findViewById(R.id.view_past_indicator)

        reservationStatus = findViewById(R.id.tv_reservation_status)
        visitNoticeTitle = findViewById(R.id.tv_visit_notice_title)
        visitNotice = findViewById(R.id.tv_visit_notice)
        pastReservationNotice = findViewById(R.id.tv_past_reservation_notice)
    }

    // 버튼과 탭 클릭 이벤트 설정
    private fun initClickListeners() {
        confirmedTab.setOnClickListener {
            showConfirmedReservation()
        }

        pastTab.setOnClickListener {
            showPastReservation()
        }

        findViewById<MaterialButton>(R.id.btn_find_route).setOnClickListener {
            copyToClipboard(
                label = "매장 주소",
                text = SHOP_ADDRESS,
                message = "매장 주소가 복사되었습니다."
            )
        }

        findViewById<MaterialButton>(R.id.btn_contact_shop).setOnClickListener {
            copyToClipboard(
                label = "매장 전화번호",
                text = SHOP_PHONE_NUMBER,
                message = "전화번호가 복사되었습니다."
            )
        }
    }

    // 확정된 예약 탭 표시
    private fun showConfirmedReservation() {
        confirmedTab.setTextColor(Color.parseColor("#FF5F8F"))
        confirmedTab.setTypeface(null, android.graphics.Typeface.BOLD)

        pastTab.setTextColor(Color.parseColor("#999999"))
        pastTab.setTypeface(null, android.graphics.Typeface.NORMAL)

        confirmedIndicator.visibility = View.VISIBLE
        pastIndicator.visibility = View.INVISIBLE

        reservationStatus.text = "D - 1"
        reservationStatus.setTextColor(Color.parseColor("#FF747D"))

        visitNoticeTitle.visibility = View.VISIBLE
        visitNotice.visibility = View.VISIBLE
        pastReservationNotice.visibility = View.GONE
    }

    // 지난 예약 탭 표시
    private fun showPastReservation() {
        confirmedTab.setTextColor(Color.parseColor("#999999"))
        confirmedTab.setTypeface(null, android.graphics.Typeface.NORMAL)

        pastTab.setTextColor(Color.parseColor("#FF5F8F"))
        pastTab.setTypeface(null, android.graphics.Typeface.BOLD)

        confirmedIndicator.visibility = View.INVISIBLE
        pastIndicator.visibility = View.VISIBLE

        reservationStatus.text = "완료된 예약"
        reservationStatus.setTextColor(Color.parseColor("#777777"))

        visitNoticeTitle.visibility = View.GONE
        visitNotice.visibility = View.GONE
        pastReservationNotice.visibility = View.VISIBLE
    }

    // 주소 또는 전화번호를 클립보드에 복사
    private fun copyToClipboard(
        label: String,
        text: String,
        message: String
    ) {
        val clipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val clipData = ClipData.newPlainText(label, text)
        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        // 추후 서버에서 받은 실제 매장 정보로 변경
        private const val SHOP_ADDRESS = "서울특별시 성동구 성수동 369"
        private const val SHOP_PHONE_NUMBER = "02-1234-5678"
    }
}