package com.example.nailnail.ui.reservation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.nailnail.R
import com.google.android.material.button.MaterialButton

class ReservationChangeDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.dialog_reservation_change,
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

        dialog?.window?.apply {
            setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )

            addFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND
            )

            attributes = attributes.apply {
                width =
                    (resources.displayMetrics.widthPixels * 0.85f)
                        .toInt()

                height =
                    WindowManager.LayoutParams.WRAP_CONTENT

                dimAmount = 0.5f
            }
        }

        dialog?.setCanceledOnTouchOutside(true)
    }

    private fun initClickListeners(view: View) {
        view.findViewById<MaterialButton>(
            R.id.btn_change_previous
        ).setOnClickListener {
            dismiss()
        }

        view.findViewById<MaterialButton>(
            R.id.btn_copy_shop_contact
        ).setOnClickListener {
            copyShopPhoneNumber()
        }
    }

    private fun copyShopPhoneNumber() {
        val clipboardManager =
            requireContext().getSystemService(
                Context.CLIPBOARD_SERVICE
            ) as ClipboardManager

        val clipData =
            ClipData.newPlainText(
                "네일샵 연락처",
                SHOP_PHONE_NUMBER
            )

        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(
            requireContext(),
            "샵 연락처가 복사되었습니다.",
            Toast.LENGTH_SHORT
        ).show()

        dismiss()
    }

    companion object {
        private const val SHOP_PHONE_NUMBER =
            "02-1234-5678"
    }
}