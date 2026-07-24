package com.example.nailnail.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nailnail.MainActivity
import com.example.nailnail.R
import com.google.android.material.textfield.TextInputEditText

class EditProfileActivity : AppCompatActivity() {

    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var nicknameEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        applyWindowInsets()
        initViews()
        loadProfileData()
        initClickListeners()
        initKeyboardActions()
        initSystemBackButton()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.edit_profile_root)
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
        nameEditText =
            findViewById(R.id.et_name)

        emailEditText =
            findViewById(R.id.et_email)

        nicknameEditText =
            findViewById(R.id.et_nickname)

        phoneEditText =
            findViewById(R.id.et_phone)
    }

    private fun loadProfileData() {
        val preferences =
            getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            )

        val nickname =
            preferences.getString(
                KEY_NICKNAME,
                null
            ) ?: preferences.getString(
                LEGACY_KEY_NICKNAME,
                DEFAULT_NICKNAME
            )

        nicknameEditText.setText(
            nickname ?: DEFAULT_NICKNAME
        )

        nameEditText.setText(
            preferences.getString(
                KEY_NAME,
                DEFAULT_NAME
            )
        )

        phoneEditText.setText(
            preferences.getString(
                KEY_PHONE,
                DEFAULT_PHONE
            )
        )

        emailEditText.setText(
            preferences.getString(
                KEY_EMAIL,
                DEFAULT_EMAIL
            )
        )
    }

    private fun initClickListeners() {
        // 상단 뒤로가기
        findViewById<ImageView>(
            R.id.iv_back
        ).setOnClickListener {
            saveAndFinish()
        }

        // 프로필 사진 변경
        findViewById<View>(
            R.id.btn_change_profile_image
        ).setOnClickListener {
            Toast.makeText(
                this,
                "프로필 이미지 변경 기능은 추후 연결됩니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

        // 홈
        findViewById<View>(
            R.id.nav_home
        ).setOnClickListener {
            saveProfileData()

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }

        // 견적함
        findViewById<View>(
            R.id.nav_estimate
        ).setOnClickListener {
            showPreparingMessage("견적함")
        }

        // 예약
        findViewById<View>(
            R.id.nav_reservation
        ).setOnClickListener {
            showPreparingMessage("예약")
        }

        // 마이페이지로 돌아가기
        findViewById<View>(
            R.id.nav_my_page
        ).setOnClickListener {
            saveAndFinish()
        }
    }

    private fun initKeyboardActions() {
        phoneEditText.setOnEditorActionListener {
                view,
                actionId,
                _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveProfileData()

                hideKeyboard(view)

                Toast.makeText(
                    this,
                    "프로필이 저장되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()

                true
            } else {
                false
            }
        }
    }

    private fun initSystemBackButton() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    saveAndFinish()
                }
            }
        )
    }

    private fun saveAndFinish() {
        saveProfileData()

        Toast.makeText(
            this,
            "프로필이 저장되었습니다.",
            Toast.LENGTH_SHORT
        ).show()

        setResult(RESULT_OK)
        finish()
    }

    private fun saveProfileData() {
        val nickname =
            nicknameEditText.text
                ?.toString()
                ?.trim()
                .orEmpty()
                .ifBlank {
                    DEFAULT_NICKNAME
                }

        val name =
            nameEditText.text
                ?.toString()
                ?.trim()
                .orEmpty()

        val phone =
            phoneEditText.text
                ?.toString()
                ?.trim()
                .orEmpty()

        val email =
            emailEditText.text
                ?.toString()
                ?.trim()
                .orEmpty()
                .ifBlank {
                    DEFAULT_EMAIL
                }

        getSharedPreferences(
            PREFS_NAME,
            MODE_PRIVATE
        ).edit()
            .putString(
                KEY_NICKNAME,
                nickname
            )
            // 기존 코드와의 호환을 위해 함께 저장
            .putString(
                LEGACY_KEY_NICKNAME,
                nickname
            )
            .putString(
                KEY_NAME,
                name
            )
            .putString(
                KEY_PHONE,
                phone
            )
            .putString(
                KEY_EMAIL,
                email
            )
            .apply()
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService<InputMethodManager>()

        inputMethodManager?.hideSoftInputFromWindow(
            view.windowToken,
            0
        )
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

    override fun onPause() {
        super.onPause()

        // 다른 화면으로 전환되더라도 입력값 유지
        saveProfileData()
    }

    companion object {
        const val PREFS_NAME =
            "mypage_profile"

        const val KEY_NAME =
            "profile_name"

        const val KEY_EMAIL =
            "profile_email"

        /*
         * 현재 MyPageActivity가 nickname 키를 사용하므로
         * 같은 키로 저장해야 메인 화면에 바로 반영됨
         */
        const val KEY_NICKNAME =
            "nickname"

        const val KEY_PHONE =
            "profile_phone"

        private const val LEGACY_KEY_NICKNAME =
            "profile_nickname"

        const val DEFAULT_NAME =
            ""

        const val DEFAULT_PHONE =
            ""

        const val DEFAULT_EMAIL =
            "nailtomorrow@gmail.com"

        const val DEFAULT_NICKNAME =
            "네일내일"
    }
}