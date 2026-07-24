package com.example.nailnail.ui.mypage

import android.content.Intent
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

class MyInfoActivity : AppCompatActivity() {

    private lateinit var nicknameValue: TextView
    private lateinit var nameValue: TextView
    private lateinit var phoneValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_info)

        applyWindowInsets()
        initViews()
        initClickListeners()
    }

    override fun onResume() {
        super.onResume()

        updateUserInformation()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.my_info_root)
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
        nicknameValue =
            findViewById(R.id.tv_nickname_value)

        nameValue =
            findViewById(R.id.tv_name_value)

        phoneValue =
            findViewById(R.id.tv_phone_value)
    }

    private fun initClickListeners() {
        // 뒤로가기
        findViewById<ImageView>(
            R.id.iv_back
        ).setOnClickListener {
            finish()
        }

        // 닉네임 수정
        findViewById<View>(
            R.id.menu_nickname
        ).setOnClickListener {
            openEditProfileScreen()
        }

        // 이름 수정
        findViewById<View>(
            R.id.menu_name
        ).setOnClickListener {
            openEditProfileScreen()
        }

        // 휴대폰 번호 수정
        findViewById<View>(
            R.id.menu_phone
        ).setOnClickListener {
            openEditProfileScreen()
        }

        // 간편 로그인
        findViewById<View>(
            R.id.menu_simple_login
        ).setOnClickListener {
            Toast.makeText(
                this,
                "카카오 간편 로그인 계정입니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

        // 언어
        findViewById<View>(
            R.id.menu_language
        ).setOnClickListener {
            Toast.makeText(
                this,
                "현재 한국어로 설정되어 있습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

        // 로그아웃
        findViewById<View>(
            R.id.menu_logout
        ).setOnClickListener {
            Toast.makeText(
                this,
                "로그아웃 기능은 추후 연결됩니다.",
                Toast.LENGTH_SHORT
            ).show()
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

    private fun updateUserInformation() {
        val preferences =
            getSharedPreferences(
                EditProfileActivity.PREFS_NAME,
                MODE_PRIVATE
            )

        val nickname =
            preferences.getString(
                EditProfileActivity.KEY_NICKNAME,
                EditProfileActivity.DEFAULT_NICKNAME
            )
                ?.ifBlank {
                    EditProfileActivity.DEFAULT_NICKNAME
                }
                ?: EditProfileActivity.DEFAULT_NICKNAME

        val name =
            preferences.getString(
                EditProfileActivity.KEY_NAME,
                DEFAULT_DISPLAY_NAME
            )
                ?.ifBlank {
                    DEFAULT_DISPLAY_NAME
                }
                ?: DEFAULT_DISPLAY_NAME

        val phone =
            preferences.getString(
                EditProfileActivity.KEY_PHONE,
                DEFAULT_DISPLAY_PHONE
            )
                ?.ifBlank {
                    DEFAULT_DISPLAY_PHONE
                }
                ?: DEFAULT_DISPLAY_PHONE

        nicknameValue.text =
            nickname

        nameValue.text =
            name

        phoneValue.text =
            phone
    }

    companion object {
        private const val DEFAULT_DISPLAY_NAME =
            "김지민"

        private const val DEFAULT_DISPLAY_PHONE =
            "010-0000-0000"
    }
}