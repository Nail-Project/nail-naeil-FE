package com.example.nailnail.ui.mypage

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nailnail.R
import com.google.android.material.switchmaterial.SwitchMaterial

class NotificationSettingActivity :
    AppCompatActivity() {

    private lateinit var reservationSwitch:
            SwitchMaterial

    private lateinit var estimateSwitch:
            SwitchMaterial

    private lateinit var allAdSwitch:
            SwitchMaterial

    private lateinit var pushSwitch:
            SwitchMaterial

    private lateinit var snsSwitch:
            SwitchMaterial

    private lateinit var emailSwitch:
            SwitchMaterial

    private lateinit var pushRow: View
    private lateinit var snsRow: View
    private lateinit var emailRow: View

    private var isUpdatingAdSwitches =
        false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(
            R.layout.activity_notification_setting
        )

        applyWindowInsets()
        initViews()
        applySwitchColors()
        loadNotificationSettings()
        initClickListeners()
        initSwitchListeners()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(
                R.id.notification_setting_root
            )
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
        reservationSwitch =
            findViewById(
                R.id.switch_reservation_notification
            )

        estimateSwitch =
            findViewById(
                R.id.switch_estimate_notification
            )

        allAdSwitch =
            findViewById(
                R.id.switch_all_ad_notification
            )

        pushSwitch =
            findViewById(
                R.id.switch_push_notification
            )

        snsSwitch =
            findViewById(
                R.id.switch_sns_notification
            )

        emailSwitch =
            findViewById(
                R.id.switch_email_notification
            )

        pushRow =
            findViewById(
                R.id.row_push_notification
            )

        snsRow =
            findViewById(
                R.id.row_sns_notification
            )

        emailRow =
            findViewById(
                R.id.row_email_notification
            )
    }

    private fun applySwitchColors() {
        val thumbColors =
            ColorStateList(
                arrayOf(
                    intArrayOf(
                        android.R.attr.state_checked
                    ),
                    intArrayOf()
                ),
                intArrayOf(
                    Color.parseColor(COLOR_PRIMARY),
                    Color.parseColor(COLOR_SWITCH_GRAY)
                )
            )

        val trackColors =
            ColorStateList(
                arrayOf(
                    intArrayOf(
                        android.R.attr.state_checked
                    ),
                    intArrayOf()
                ),
                intArrayOf(
                    Color.parseColor(COLOR_TRACK_ON),
                    Color.parseColor(COLOR_TRACK_OFF)
                )
            )

        listOf(
            reservationSwitch,
            estimateSwitch,
            allAdSwitch,
            pushSwitch,
            snsSwitch,
            emailSwitch
        ).forEach { notificationSwitch ->

            notificationSwitch.thumbTintList =
                thumbColors

            notificationSwitch.trackTintList =
                trackColors
        }
    }

    private fun loadNotificationSettings() {
        val preferences =
            getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            )

        isUpdatingAdSwitches = true

        reservationSwitch.isChecked =
            preferences.getBoolean(
                KEY_RESERVATION,
                true
            )

        estimateSwitch.isChecked =
            preferences.getBoolean(
                KEY_ESTIMATE,
                true
            )

        val allAdsEnabled =
            preferences.getBoolean(
                KEY_ALL_ADS,
                false
            )

        allAdSwitch.isChecked =
            allAdsEnabled

        pushSwitch.isChecked =
            preferences.getBoolean(
                KEY_PUSH,
                allAdsEnabled
            )

        snsSwitch.isChecked =
            preferences.getBoolean(
                KEY_SNS,
                allAdsEnabled
            )

        emailSwitch.isChecked =
            preferences.getBoolean(
                KEY_EMAIL,
                allAdsEnabled
            )

        updateAdSubSwitchState(
            allAdsEnabled
        )

        isUpdatingAdSwitches = false
    }

    private fun initClickListeners() {
        findViewById<ImageView>(
            R.id.iv_back
        ).setOnClickListener {
            finish()
        }

        findViewById<View>(
            R.id.tv_ad_terms
        ).setOnClickListener {
            NotificationTermsDialogFragment()
                .show(
                    supportFragmentManager,
                    "NotificationTermsDialog"
                )
        }
    }

    private fun initSwitchListeners() {
        reservationSwitch
            .setOnCheckedChangeListener {
                    _,
                    _ ->

                saveNotificationSettings()
            }

        estimateSwitch
            .setOnCheckedChangeListener {
                    _,
                    _ ->

                saveNotificationSettings()
            }

        allAdSwitch
            .setOnCheckedChangeListener {
                    _,
                    isChecked ->

                if (isUpdatingAdSwitches) {
                    return@setOnCheckedChangeListener
                }

                isUpdatingAdSwitches = true

                pushSwitch.isChecked =
                    isChecked

                snsSwitch.isChecked =
                    isChecked

                emailSwitch.isChecked =
                    isChecked

                updateAdSubSwitchState(
                    isChecked
                )

                isUpdatingAdSwitches = false

                saveNotificationSettings()
            }

        pushSwitch
            .setOnCheckedChangeListener {
                    _,
                    _ ->

                if (!isUpdatingAdSwitches) {
                    saveNotificationSettings()
                }
            }

        snsSwitch
            .setOnCheckedChangeListener {
                    _,
                    _ ->

                if (!isUpdatingAdSwitches) {
                    saveNotificationSettings()
                }
            }

        emailSwitch
            .setOnCheckedChangeListener {
                    _,
                    _ ->

                if (!isUpdatingAdSwitches) {
                    saveNotificationSettings()
                }
            }
    }

    private fun updateAdSubSwitchState(
        isEnabled: Boolean
    ) {
        pushSwitch.isEnabled =
            isEnabled

        snsSwitch.isEnabled =
            isEnabled

        emailSwitch.isEnabled =
            isEnabled

        val rowAlpha =
            if (isEnabled) {
                1f
            } else {
                0.6f
            }

        pushRow.alpha =
            rowAlpha

        snsRow.alpha =
            rowAlpha

        emailRow.alpha =
            rowAlpha
    }

    private fun saveNotificationSettings() {
        getSharedPreferences(
            PREFS_NAME,
            MODE_PRIVATE
        ).edit()
            .putBoolean(
                KEY_RESERVATION,
                reservationSwitch.isChecked
            )
            .putBoolean(
                KEY_ESTIMATE,
                estimateSwitch.isChecked
            )
            .putBoolean(
                KEY_ALL_ADS,
                allAdSwitch.isChecked
            )
            .putBoolean(
                KEY_PUSH,
                pushSwitch.isChecked
            )
            .putBoolean(
                KEY_SNS,
                snsSwitch.isChecked
            )
            .putBoolean(
                KEY_EMAIL,
                emailSwitch.isChecked
            )
            .apply()
    }

    companion object {
        private const val PREFS_NAME =
            "notification_settings"

        private const val KEY_RESERVATION =
            "reservation_notification"

        private const val KEY_ESTIMATE =
            "estimate_notification"

        private const val KEY_ALL_ADS =
            "all_ad_notification"

        private const val KEY_PUSH =
            "push_notification"

        private const val KEY_SNS =
            "sns_notification"

        private const val KEY_EMAIL =
            "email_notification"

        private const val COLOR_PRIMARY =
            "#B2607D"

        private const val COLOR_TRACK_ON =
            "#F0DCE3"

        private const val COLOR_SWITCH_GRAY =
            "#B6AFAB"

        private const val COLOR_TRACK_OFF =
            "#F8F6F5"
    }
}