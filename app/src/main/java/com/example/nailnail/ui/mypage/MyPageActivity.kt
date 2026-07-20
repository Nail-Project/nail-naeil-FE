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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_page)

        applyWindowInsets()
        initMenuClickListeners()
        initBottomNavigation()
    }

    // 프로필 수정 화면에서 돌아오면
    // 저장된 이름과 이메일을 다시 표시
    override fun onResume() {
        super.onResume()
        updateProfileInformation()
    }

    // 상태바와 내비게이션바 영역 반영
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

    // 마이페이지 메뉴 클릭 이벤트
    private fun initMenuClickListeners() {
        // 프로필 수정
        findViewById<View>(R.id.layout_profile)
            .setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        EditProfileActivity::class.java
                    )
                )
            }

        // 찜한 디자인
        findViewById<View>(R.id.menu_favorite_design)
            .setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        FavoriteDesignActivity::class.java
                    )
                )
            }

        // 찜한 매장
        findViewById<View>(R.id.menu_favorite_shop)
            .setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        FavoriteShopActivity::class.java
                    )
                )
            }

        // 아직 구현하지 않은 메뉴
        findViewById<View>(R.id.menu_notification_setting)
            .setOnClickListener {
                showPreparingMessage("알림 설정")
            }

        findViewById<View>(R.id.menu_faq)
            .setOnClickListener {
                showPreparingMessage("자주 묻는 질문")
            }

        findViewById<View>(R.id.menu_environment_setting)
            .setOnClickListener {
                showPreparingMessage("환경 설정")
            }

        findViewById<View>(R.id.menu_terms_policy)
            .setOnClickListener {
                showPreparingMessage("약관 및 정책")
            }
    }

    // 하단 내비게이션 클릭 이벤트
    private fun initBottomNavigation() {
        findViewById<View>(R.id.nav_home)
            .setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    )
                )
            }

        findViewById<View>(R.id.nav_estimate)
            .setOnClickListener {
                showPreparingMessage("견적함")
            }

        findViewById<View>(R.id.nav_reservation)
            .setOnClickListener {
                showPreparingMessage("예약")
            }
    }

    // SharedPreferences에 저장된 프로필 정보 불러오기
    private fun updateProfileInformation() {
        val preferences =
            getSharedPreferences(
                EditProfileActivity.PREFS_NAME,
                MODE_PRIVATE
            )

        val name =
            preferences.getString(
                EditProfileActivity.KEY_NAME,
                EditProfileActivity.DEFAULT_NAME
            ) ?: EditProfileActivity.DEFAULT_NAME

        val email =
            preferences.getString(
                EditProfileActivity.KEY_EMAIL,
                EditProfileActivity.DEFAULT_EMAIL
            ) ?: EditProfileActivity.DEFAULT_EMAIL

        findViewById<TextView>(R.id.tv_user_name).text =
            "$name 님"

        findViewById<TextView>(R.id.tv_user_email).text =
            email
    }

    // 아직 구현되지 않은 메뉴 안내
    private fun showPreparingMessage(menuName: String) {
        Toast.makeText(
            this,
            "$menuName 화면은 다음 단계에서 연결합니다.",
            Toast.LENGTH_SHORT
        ).show()
    }
}