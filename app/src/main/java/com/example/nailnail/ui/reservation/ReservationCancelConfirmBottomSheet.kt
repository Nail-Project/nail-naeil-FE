package com.example.nailnail.ui.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nailnail.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class ReservationCancelConfirmBottomSheet :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.bottom_sheet_reservation_cancel_confirm,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners(view)
    }

    override fun onStart() {
        super.onStart()

        val bottomSheetDialog =
            dialog as? BottomSheetDialog ?: return

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

    private fun initClickListeners(view: View) {
        // 예약 유지
        view.findViewById<MaterialButton>(
            R.id.btn_keep_reservation
        ).setOnClickListener {
            dismiss()
        }

        // 예약 취소 → 취소 사유 선택 바텀시트
        view.findViewById<MaterialButton>(
            R.id.btn_confirm_cancel_reservation
        ).setOnClickListener {
            val fragmentManager =
                parentFragmentManager

            // 현재 바텀시트를 즉시 닫은 후
            // 취소 사유 선택 바텀시트를 표시
            dismissNow()

            ReservationCancelReasonBottomSheet()
                .show(
                    fragmentManager,
                    "ReservationCancelReasonBottomSheet"
                )
        }
    }
}