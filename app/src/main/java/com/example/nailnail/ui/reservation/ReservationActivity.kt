package com.example.nailnail.ui.reservation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
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
    private lateinit var shopName: TextView
    private lateinit var reservationTime: TextView
    private lateinit var reservationDate: TextView
    private lateinit var shopLocation: TextView
    private lateinit var serviceOption: TextView
    private lateinit var reservationPrice: TextView
    private lateinit var nailDesignImage: ImageView

    private lateinit var visitNoticeTitle: TextView
    private lateinit var visitNotice: TextView
    private lateinit var pastReservationNotice: TextView
    private lateinit var manageReservationButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reservation)

        applyWindowInsets()
        initViews()
        initClickListeners()

        val shouldShowPastTab =
            intent.getBooleanExtra(EXTRA_SHOW_PAST_TAB, false)

        if (shouldShowPastTab) {
            showPastReservation()
        } else {
            showConfirmedReservation()
        }
    }

    // 시스템 상태바와 내비게이션바 영역 반영
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.reservation_root)
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

    // 화면에서 사용할 View 연결
    private fun initViews() {
        confirmedTab = findViewById(R.id.tv_confirmed_tab)
        pastTab = findViewById(R.id.tv_past_tab)

        confirmedIndicator = findViewById(R.id.view_confirmed_indicator)
        pastIndicator = findViewById(R.id.view_past_indicator)

        reservationStatus = findViewById(R.id.tv_reservation_status)
        shopName = findViewById(R.id.tv_shop_name)
        reservationTime = findViewById(R.id.tv_reservation_time)
        reservationDate = findViewById(R.id.tv_reservation_date)
        shopLocation = findViewById(R.id.tv_shop_location)
        serviceOption = findViewById(R.id.tv_service_option)
        reservationPrice = findViewById(R.id.tv_reservation_price)
        nailDesignImage = findViewById(R.id.iv_nail_design)

        visitNoticeTitle = findViewById(R.id.tv_visit_notice_title)
        visitNotice = findViewById(R.id.tv_visit_notice)
        pastReservationNotice =
            findViewById(R.id.tv_past_reservation_notice)

        manageReservationButton =
            findViewById(R.id.tv_manage_reservation)
    }

    // 버튼과 탭 클릭 이벤트 설정
    private fun initClickListeners() {
        confirmedTab.setOnClickListener {
            showConfirmedReservation()
        }

        pastTab.setOnClickListener {
            showPastReservation()
        }

        findViewById<MaterialButton>(R.id.btn_find_route)
            .setOnClickListener {
                copyToClipboard(
                    label = "매장 주소",
                    text = SHOP_ADDRESS,
                    message = "매장 주소가 복사되었습니다."
                )
            }

        findViewById<MaterialButton>(R.id.btn_contact_shop)
            .setOnClickListener {
                copyToClipboard(
                    label = "매장 전화번호",
                    text = SHOP_PHONE_NUMBER,
                    message = "전화번호가 복사되었습니다."
                )
            }

        manageReservationButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ReservationManageActivity::class.java
                )
            )
        }
    }

    // 확정된 예약 탭 표시
    private fun showConfirmedReservation() {
        confirmedTab.setTextColor(
            Color.parseColor(COLOR_PINK)
        )
        confirmedTab.setTypeface(null, Typeface.BOLD)

        pastTab.setTextColor(
            Color.parseColor(COLOR_TAB_GRAY)
        )
        pastTab.setTypeface(null, Typeface.NORMAL)

        confirmedIndicator.visibility = View.VISIBLE
        pastIndicator.visibility = View.INVISIBLE

        reservationStatus.text = "D - 1"
        reservationStatus.setTextColor(
            Color.parseColor(COLOR_STATUS_PINK)
        )

        shopName.setTextColor(
            Color.parseColor(COLOR_BLACK)
        )
        reservationTime.setTextColor(
            Color.parseColor(COLOR_BLACK)
        )
        reservationDate.setTextColor(
            Color.parseColor(COLOR_TEXT_GRAY)
        )
        shopLocation.setTextColor(
            Color.parseColor(COLOR_TEXT_GRAY)
        )
        serviceOption.setTextColor(
            Color.parseColor(COLOR_TEXT_GRAY)
        )
        reservationPrice.setTextColor(
            Color.parseColor(COLOR_BLACK)
        )

        // 네일 사진 원래 색상 복구
        nailDesignImage.clearColorFilter()

        visitNoticeTitle.visibility = View.VISIBLE
        visitNotice.visibility = View.VISIBLE
        pastReservationNotice.visibility = View.GONE

        manageReservationButton.visibility = View.VISIBLE
    }

    // 지난 예약 탭 표시
    private fun showPastReservation() {
        confirmedTab.setTextColor(
            Color.parseColor(COLOR_TAB_GRAY)
        )
        confirmedTab.setTypeface(null, Typeface.NORMAL)

        pastTab.setTextColor(
            Color.parseColor(COLOR_PINK)
        )
        pastTab.setTypeface(null, Typeface.BOLD)

        confirmedIndicator.visibility = View.INVISIBLE
        pastIndicator.visibility = View.VISIBLE

        reservationStatus.text = "완료된 예약"
        reservationStatus.setTextColor(
            Color.parseColor(COLOR_TEXT_GRAY)
        )

        // 지난 예약 카드의 글자 회색 처리
        shopName.setTextColor(
            Color.parseColor(COLOR_PAST_TEXT)
        )
        reservationTime.setTextColor(
            Color.parseColor(COLOR_PAST_TEXT)
        )
        reservationDate.setTextColor(
            Color.parseColor(COLOR_PAST_TEXT)
        )
        shopLocation.setTextColor(
            Color.parseColor(COLOR_PAST_TEXT)
        )
        serviceOption.setTextColor(
            Color.parseColor(COLOR_PAST_TEXT)
        )
        reservationPrice.setTextColor(
            Color.parseColor(COLOR_PAST_TEXT)
        )

        // 네일 사진 흑백 처리
        applyGrayScaleToNailImage()

        visitNoticeTitle.visibility = View.GONE
        visitNotice.visibility = View.GONE
        pastReservationNotice.visibility = View.VISIBLE

        manageReservationButton.visibility = View.VISIBLE
    }

    // 지난 예약 네일 이미지를 흑백으로 표시
    private fun applyGrayScaleToNailImage() {
        val colorMatrix = ColorMatrix().apply {
            setSaturation(0f)
        }

        nailDesignImage.colorFilter =
            ColorMatrixColorFilter(colorMatrix)
    }

    // 주소 또는 전화번호를 클립보드에 복사
    private fun copyToClipboard(
        label: String,
        text: String,
        message: String
    ) {
        val clipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE)
                    as ClipboardManager

        val clipData =
            ClipData.newPlainText(label, text)

        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val EXTRA_SHOW_PAST_TAB =
            "extra_show_past_tab"

        private const val SHOP_ADDRESS =
            "서울특별시 성동구 성수동 369"

        private const val SHOP_PHONE_NUMBER =
            "02-1234-5678"

        private const val COLOR_PINK = "#FF5F8F"
        private const val COLOR_STATUS_PINK = "#FF747D"
        private const val COLOR_BLACK = "#111111"
        private const val COLOR_TEXT_GRAY = "#7D7D7D"
        private const val COLOR_TAB_GRAY = "#999999"
        private const val COLOR_PAST_TEXT = "#777777"
    }
}