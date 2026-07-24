package com.example.nailnail.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nailnail.MainActivity
import com.example.nailnail.R

class MyPageActivity : AppCompatActivity() {

    /*
     * false: 무료 이용 중 화면
     * true: N플러스 이용 중 화면
     */
    private val isNPlusMember = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_page)

        applyWindowInsets()
        initMenuClickListeners()
        initMembershipClickListeners()
        initBottomNavigation()
        updateMembershipCard()
    }

    override fun onResume() {
        super.onResume()
        updateProfileInformation()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.my_page_root)
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

    private fun initMenuClickListeners() {
        // 프로필 영역
        findViewById<View>(
            R.id.layout_profile
        ).setOnClickListener {
            openEditProfileScreen()
        }

        // 수정 버튼
        findViewById<View>(
            R.id.btn_profile_edit
        ).setOnClickListener {
            openEditProfileScreen()
        }

        // 찜한 디자인
        findViewById<View>(
            R.id.menu_favorite_design
        ).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    FavoriteDesignActivity::class.java
                )
            )
        }

        // 찜한 매장
        findViewById<View>(
            R.id.menu_favorite_shop
        ).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    FavoriteShopActivity::class.java
                )
            )
        }

        // 내 정보
        findViewById<View>(
            R.id.menu_my_info
        ).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MyInfoActivity::class.java
                )
            )
        }

        // 알림 설정
        findViewById<View>(
            R.id.menu_notification_setting
        ).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    NotificationSettingActivity::class.java
                )
            )
        }
        // 공지사항
        findViewById<View>(
            R.id.menu_notice
        ).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    NoticeActivity::class.java
                )
            )
        }

        // 약관 및 정책
        findViewById<View>(
            R.id.menu_terms_policy
        ).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    TermsPolicyActivity::class.java
                )
            )
        }
    }

    private fun openEditProfileScreen() {
        startActivity(
            Intent(
                this,
                EditProfileActivity::class.java
            )
        )
    }

    private fun initMembershipClickListeners() {
        findViewById<View>(
            R.id.tv_free_payment_history
        ).setOnClickListener {
            showPreparingMessage("결제내역")
        }

        findViewById<View>(
            R.id.tv_nplus_payment_history
        ).setOnClickListener {
            showPreparingMessage("결제내역")
        }

        findViewById<View>(
            R.id.btn_more_benefits
        ).setOnClickListener {
            showPreparingMessage("N 플러스 혜택")
        }
    }

    private fun updateMembershipCard() {
        val freeMembershipLayout =
            findViewById<View>(
                R.id.layout_free_membership
            )

        val nPlusMembershipLayout =
            findViewById<View>(
                R.id.layout_nplus_membership
            )

        if (isNPlusMember) {
            freeMembershipLayout.visibility =
                View.GONE

            nPlusMembershipLayout.visibility =
                View.VISIBLE
        } else {
            freeMembershipLayout.visibility =
                View.VISIBLE

            nPlusMembershipLayout.visibility =
                View.GONE
        }
    }

    private fun initBottomNavigation() {
        findViewById<View>(
            R.id.nav_home
        ).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }

        findViewById<View>(
            R.id.nav_estimate
        ).setOnClickListener {
            showPreparingMessage("견적함")
        }

        findViewById<View>(
            R.id.nav_reservation
        ).setOnClickListener {
            showPreparingMessage("예약")
        }
    }

    private fun updateProfileInformation() {
        val preferences =
            getSharedPreferences(
                EditProfileActivity.PREFS_NAME,
                MODE_PRIVATE
            )

        val nickname =
            preferences.getString(
                KEY_NICKNAME,
                DEFAULT_NICKNAME
            ) ?: DEFAULT_NICKNAME

        findViewById<TextView>(
            R.id.tv_user_name
        ).text = nickname
    }

    private fun showPreparingMessage(
        menuName: String
    ) {
        Toast.makeText(
            this,
            "$menuName 화면은 다음 단계에서 연결합니다.",
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val KEY_NICKNAME =
            "nickname"

        private const val DEFAULT_NICKNAME =
            "네일내일"
    }
}