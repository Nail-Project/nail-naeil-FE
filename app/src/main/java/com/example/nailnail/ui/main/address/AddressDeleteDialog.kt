package com.example.nailnail.ui.main.address

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/** 삭제 확인 -> 삭제 완료 2단계 팝업. [step]이 null이면 아무것도 그리지 않는다. */
enum class DeleteDialogStep { CONFIRM, COMPLETE }

@Composable
fun AddressDeleteDialog(
    step: DeleteDialogStep,
    addressLabel: String,
    onCancel: () -> Unit,
    onConfirmDelete: () -> Unit,
    onAcknowledgeComplete: () -> Unit
) {
    when (step) {
        DeleteDialogStep.CONFIRM -> AlertDialog(
            onDismissRequest = onCancel,
            title = { Text("'$addressLabel'") },
            text = { Text("주소를 삭제할까요?") },
            dismissButton = { TextButton(onClick = onCancel) { Text("이전으로") } },
            confirmButton = { TextButton(onClick = onConfirmDelete) { Text("주소 삭제") } }
        )

        DeleteDialogStep.COMPLETE -> AlertDialog(
            onDismissRequest = onAcknowledgeComplete,
            title = { Text("'$addressLabel'") },
            text = { Text("삭제 완료되었습니다.") },
            confirmButton = { TextButton(onClick = onAcknowledgeComplete) { Text("확인") } }
        )
    }
}
