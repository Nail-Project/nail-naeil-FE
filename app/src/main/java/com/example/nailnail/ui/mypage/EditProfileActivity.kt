package com.example.nailnail.ui.mypage

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nailnail.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EditProfileActivity : AppCompatActivity() {

    private lateinit var nameLayout: TextInputLayout
    private lateinit var nicknameLayout: TextInputLayout

    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var nicknameEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText

    private lateinit var saveButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        applyWindowInsets()
        initViews()
        loadProfileData()
        initClickListeners()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.edit_profile_root)
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

    private fun initViews() {
        nameLayout = findViewById(R.id.layout_name)
        nicknameLayout = findViewById(R.id.layout_nickname)

        nameEditText = findViewById(R.id.et_name)
        emailEditText = findViewById(R.id.et_email)
        nicknameEditText = findViewById(R.id.et_nickname)
        phoneEditText = findViewById(R.id.et_phone)

        saveButton = findViewById(R.id.btn_save_profile)
    }

    private fun loadProfileData() {
        val preferences =
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        nameEditText.setText(
            preferences.getString(
                KEY_NAME,
                DEFAULT_NAME
            )
        )

        emailEditText.setText(
            preferences.getString(
                KEY_EMAIL,
                DEFAULT_EMAIL
            )
        )

        nicknameEditText.setText(
            preferences.getString(
                KEY_NICKNAME,
                DEFAULT_NICKNAME
            )
        )

        phoneEditText.setText(
            preferences.getString(
                KEY_PHONE,
                ""
            )
        )
    }

    private fun initClickListeners() {
        findViewById<ImageView>(R.id.iv_back)
            .setOnClickListener {
                finish()
            }

        saveButton.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val name =
            nameEditText.text
                ?.toString()
                ?.trim()
                .orEmpty()

        val email =
            emailEditText.text
                ?.toString()
                ?.trim()
                .orEmpty()

        val nickname =
            nicknameEditText.text
                ?.toString()
                ?.trim()
                .orEmpty()

        val phone =
            phoneEditText.text
                ?.toString()
                ?.trim()
                .orEmpty()

        nameLayout.error = null
        nicknameLayout.error = null

        if (name.isBlank()) {
            nameLayout.error = "이름을 입력해 주세요."
            nameEditText.requestFocus()
            return
        }

        if (nickname.isBlank()) {
            nicknameLayout.error = "닉네임을 입력해 주세요."
            nicknameEditText.requestFocus()
            return
        }

        getSharedPreferences(
            PREFS_NAME,
            MODE_PRIVATE
        ).edit()
            .putString(KEY_NAME, name)
            .putString(KEY_EMAIL, email)
            .putString(KEY_NICKNAME, nickname)
            .putString(KEY_PHONE, phone)
            .apply()

        Toast.makeText(
            this,
            "프로필이 저장되었습니다.",
            Toast.LENGTH_SHORT
        ).show()

        setResult(RESULT_OK)
        finish()
    }

    companion object {
        const val PREFS_NAME = "mypage_profile"

        const val KEY_NAME = "profile_name"
        const val KEY_EMAIL = "profile_email"
        const val KEY_NICKNAME = "profile_nickname"
        const val KEY_PHONE = "profile_phone"

        const val DEFAULT_NAME = "네일내일"
        const val DEFAULT_EMAIL = "choyoun0711@naver.com"
        const val DEFAULT_NICKNAME = "네일내일"
    }
}