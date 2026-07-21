package com.example.nailnail.ui.reservation

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
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
import android.content.Intent

class ReservationActivity : AppCompatActivity() {

    private lateinit var confirmedTab: TextView
    private lateinit var pastTab: TextView

    private lateinit var confirmedIndicator: View
    private lateinit var pastIndicator: View

    private lateinit var emptyStateLayout: View
    private lateinit var confirmedReservationScroll: View
    private lateinit var pastReservationScroll: View

    private lateinit var confirmedSortText: TextView
    private lateinit var pastSortText: TextView

    private var hasConfirmedReservation = true
    private var confirmedSortAscending = true
    private var pastSortAscending = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reservation)

        applyWindowInsets()
        initViews()
        applyCancelledReservationStyle()
        initClickListeners()

        hasConfirmedReservation =
            !intent.getBooleanExtra(
                EXTRA_SHOW_EMPTY_STATE,
                false
            )

        val shouldShowPastTab =
            intent.getBooleanExtra(
                EXTRA_SHOW_PAST_TAB,
                false
            )

        if (shouldShowPastTab) {
            showPastReservation()
        } else {
            showConfirmedReservation()
        }
    }

    // 상태바와 내비게이션바 영역 반영
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.reservation_root)
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
        confirmedTab =
            findViewById(R.id.tv_confirmed_tab)

        pastTab =
            findViewById(R.id.tv_past_tab)

        confirmedIndicator =
            findViewById(R.id.view_confirmed_indicator)

        pastIndicator =
            findViewById(R.id.view_past_indicator)

        emptyStateLayout =
            findViewById(R.id.layout_empty_state)

        confirmedReservationScroll =
            findViewById(R.id.scroll_confirmed_reservation)

        pastReservationScroll =
            findViewById(R.id.scroll_past_reservation)

        confirmedSortText =
            findViewById(R.id.tv_confirmed_sort)

        pastSortText =
            findViewById(R.id.tv_past_sort)
    }

    private fun initClickListeners() {
        confirmedTab.setOnClickListener {
            showConfirmedReservation()
        }

        pastTab.setOnClickListener {
            showPastReservation()
        }

        confirmedSortText.setOnClickListener {
            confirmedSortAscending =
                !confirmedSortAscending

            updateConfirmedSortText()

            Toast.makeText(
                this,
                "방문일자 순서가 변경되었습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

        pastSortText.setOnClickListener {
            pastSortAscending =
                !pastSortAscending

            updatePastSortText()

            Toast.makeText(
                this,
                "방문일자 순서가 변경되었습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

        findViewById<MaterialButton>(
            R.id.btn_design_estimate
        ).setOnClickListener {
            Toast.makeText(
                this,
                "디자인 견적 화면은 추후 연결됩니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

        findViewById<MaterialButton>(
            R.id.btn_reservation_detail
        ).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ReservationDetailActivity::class.java
                )
            )
        }

        findViewById<MaterialButton>(
            R.id.btn_reservation_manage
        ).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ReservationDetailActivity::class.java
                )
            )
        }

        findViewById<View>(
            R.id.layout_tab_home
        ).setOnClickListener {
            showPreparingMessage("홈")
        }

        findViewById<View>(
            R.id.layout_tab_estimate
        ).setOnClickListener {
            showPreparingMessage("견적함")
        }

        findViewById<View>(
            R.id.layout_tab_my
        ).setOnClickListener {
            showPreparingMessage("마이페이지")
        }
    }

    // 확정된 예약 탭
    private fun showConfirmedReservation() {
        confirmedTab.setTextColor(
            Color.parseColor(COLOR_BLACK)
        )
        confirmedTab.setTypeface(
            null,
            Typeface.BOLD
        )

        pastTab.setTextColor(
            Color.parseColor(COLOR_TAB_GRAY)
        )
        pastTab.setTypeface(
            null,
            Typeface.NORMAL
        )

        confirmedIndicator.visibility =
            View.VISIBLE

        pastIndicator.visibility =
            View.INVISIBLE

        pastReservationScroll.visibility =
            View.GONE

        if (hasConfirmedReservation) {
            confirmedReservationScroll.visibility =
                View.VISIBLE

            emptyStateLayout.visibility =
                View.GONE
        } else {
            confirmedReservationScroll.visibility =
                View.GONE

            emptyStateLayout.visibility =
                View.VISIBLE
        }
    }

    // 지난 예약 탭
    private fun showPastReservation() {
        confirmedTab.setTextColor(
            Color.parseColor(COLOR_TAB_GRAY)
        )
        confirmedTab.setTypeface(
            null,
            Typeface.NORMAL
        )

        pastTab.setTextColor(
            Color.parseColor(COLOR_BLACK)
        )
        pastTab.setTypeface(
            null,
            Typeface.BOLD
        )

        confirmedIndicator.visibility =
            View.INVISIBLE

        pastIndicator.visibility =
            View.VISIBLE

        emptyStateLayout.visibility =
            View.GONE

        confirmedReservationScroll.visibility =
            View.GONE

        pastReservationScroll.visibility =
            View.VISIBLE
    }

    private fun updateConfirmedSortText() {
        confirmedSortText.text =
            if (confirmedSortAscending) {
                "방문일자 ↑"
            } else {
                "방문일자 ↓"
            }
    }

    private fun updatePastSortText() {
        pastSortText.text =
            if (pastSortAscending) {
                "방문일자 ↑"
            } else {
                "방문일자 ↓"
            }
    }

    // 취소 예약 텍스트 취소선 적용
    private fun applyCancelledReservationStyle() {
        val cancelledTextViews = listOf(
            R.id.tv_cancelled_date,
            R.id.tv_cancelled_shop,
            R.id.tv_cancelled_design,
            R.id.tv_cancelled_option
        )

        cancelledTextViews.forEach { textViewId ->
            val textView =
                findViewById<TextView>(textViewId)

            textView.paintFlags =
                textView.paintFlags or
                        Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun showPreparingMessage(
        menuName: String
    ) {
        Toast.makeText(
            this,
            "$menuName 화면은 추후 연결됩니다.",
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val EXTRA_SHOW_PAST_TAB =
            "extra_show_past_tab"

        const val EXTRA_SHOW_EMPTY_STATE =
            "extra_show_empty_state"

        private const val COLOR_BLACK =
            "#1E1E1E"

        private const val COLOR_TAB_GRAY =
            "#716D6B"
    }
}