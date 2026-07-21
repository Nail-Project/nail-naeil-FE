package com.example.nailnail.ui.reservation

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.widget.CompoundButtonCompat
import com.example.nailnail.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class ReservationCancelReasonBottomSheet :
    BottomSheetDialogFragment() {

    private lateinit var reasonGroup: RadioGroup
    private lateinit var completeButton: MaterialButton

    private var selectedReason: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.bottom_sheet_reservation_cancel_reason,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        initViews(view)
        applyRadioButtonColors(view)
        initClickListeners(view)
    }

    override fun onStart() {
        super.onStart()

        val bottomSheetDialog =
            dialog as? BottomSheetDialog
                ?: return

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            ) ?: return

        bottomSheet.setBackgroundResource(
            android.R.color.transparent
        )

        val behavior =
            BottomSheetBehavior.from(bottomSheet)

        behavior.state =
            BottomSheetBehavior.STATE_EXPANDED

        behavior.skipCollapsed = true
    }

    private fun initViews(view: View) {
        reasonGroup =
            view.findViewById(
                R.id.group_cancel_reason
            )

        completeButton =
            view.findViewById(
                R.id.btn_cancel_reason_complete
            )
    }

    private fun applyRadioButtonColors(view: View) {
        val radioButtonTint =
            ColorStateList(
                arrayOf(
                    intArrayOf(
                        android.R.attr.state_checked
                    ),
                    intArrayOf()
                ),
                intArrayOf(
                    Color.parseColor(COLOR_PRIMARY),
                    Color.parseColor(COLOR_RADIO_UNCHECKED)
                )
            )

        val radioButtons = listOf(
            view.findViewById<RadioButton>(
                R.id.radio_cancel_personal
            ),
            view.findViewById<RadioButton>(
                R.id.radio_cancel_other_shop
            ),
            view.findViewById<RadioButton>(
                R.id.radio_cancel_wrong_datetime
            ),
            view.findViewById<RadioButton>(
                R.id.radio_cancel_other
            )
        )

        radioButtons.forEach { radioButton ->
            CompoundButtonCompat.setButtonTintList(
                radioButton,
                radioButtonTint
            )
        }
    }

    private fun initClickListeners(view: View) {
        reasonGroup.setOnCheckedChangeListener {
                group,
                checkedId ->

            val selectedRadioButton =
                group.findViewById<RadioButton>(
                    checkedId
                )

            selectedReason =
                selectedRadioButton.text
                    .toString()

            activateCompleteButton()
        }

        view.findViewById<MaterialButton>(
            R.id.btn_cancel_reason_back
        ).setOnClickListener {
            dismiss()
        }

        completeButton.setOnClickListener {
            completeCancellation()
        }
    }

    private fun activateCompleteButton() {
        completeButton.isEnabled = true

        completeButton.backgroundTintList =
            ColorStateList.valueOf(
                Color.parseColor(COLOR_PRIMARY)
            )
    }

    private fun completeCancellation() {
        Toast.makeText(
            requireContext(),
            "예약이 취소되었습니다.",
            Toast.LENGTH_SHORT
        ).show()

        dismiss()

        val reservationIntent =
            Intent(
                requireContext(),
                ReservationActivity::class.java
            ).apply {
                putExtra(
                    ReservationActivity.EXTRA_SHOW_EMPTY_STATE,
                    true
                )

                flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

        startActivity(reservationIntent)

        requireActivity().finish()
    }

    companion object {
        private const val COLOR_PRIMARY =
            "#B2607D"

        private const val COLOR_RADIO_UNCHECKED =
            "#DED9D6"
    }
}