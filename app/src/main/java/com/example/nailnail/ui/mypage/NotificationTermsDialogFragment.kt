package com.example.nailnail.ui.mypage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.nailnail.R

class NotificationTermsDialogFragment :
    DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.dialog_notification_terms,
            container,
            false
        )
    }

    override fun onStart() {
        super.onStart()

        val dialogWindow =
            dialog?.window ?: return

        dialogWindow.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )

        dialogWindow.setDimAmount(0.5f)

        dialogWindow.setGravity(
            Gravity.CENTER
        )

        val dialogWidth =
            (resources.displayMetrics.widthPixels * 0.84f)
                .toInt()

        val dialogHeight =
            dpToPx(408)

        /*
         * WRAP_CONTENT를 사용하지 않고
         * 피그마 크기로 창 자체를 강제 지정
         */
        dialogWindow.setLayout(
            dialogWidth,
            dialogHeight
        )

        dialog?.setCanceledOnTouchOutside(true)
    }

    private fun dpToPx(dp: Int): Int {
        return (
                dp * resources.displayMetrics.density
                ).toInt()
    }
}