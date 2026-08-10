package com.example.nailnaeil.ui.main.reservation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.ui.theme.DividerGray
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.TextSecondary

private val CancelReasons = listOf(
    "개인 사정으로 인해 취소할게요.",
    "다른 샵을 이용하게 됐어요.",
    "예약 날짜 / 시간을 잘못 선택했어요.",
    "기타"
)

@Composable
fun ReservationCancelReasonSheet(onBack: () -> Unit, onComplete: (String) -> Unit) {
    var selectedReason by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(text = "취소 사유를 선택해주세요.", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        HorizontalDivider(color = DividerGray, modifier = Modifier.padding(top = 28.dp))

        Column(modifier = Modifier.padding(top = 18.dp)) {
            CancelReasons.forEach { reason ->
                val selected = reason == selectedReason
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .selectable(selected = selected, onClick = { selectedReason = reason }),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selected,
                        onClick = { selectedReason = reason },
                        colors = RadioButtonDefaults.colors(selectedColor = MutedRosePrimary, unselectedColor = TextSecondary)
                    )
                    Text(text = reason, style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f).height(54.dp)
            ) {
                Text("뒤로 가기")
            }
            Button(
                onClick = { selectedReason?.let(onComplete) },
                enabled = selectedReason != null,
                colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                modifier = Modifier.weight(1f).height(54.dp)
            ) {
                Text("선택 완료")
            }
        }
    }
}
