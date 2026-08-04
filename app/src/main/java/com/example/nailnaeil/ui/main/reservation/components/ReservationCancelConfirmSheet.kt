package com.example.nailnaeil.ui.main.reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.ui.main.reservation.Reservation
import com.example.nailnaeil.ui.theme.AppBackground
import com.example.nailnaeil.ui.theme.DividerGray
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun ReservationCancelConfirmSheet(
    reservation: Reservation,
    onKeepReservation: () -> Unit,
    onConfirmCancel: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(text = "예약을 취소하시겠어요?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(
            text = "방문 전일 18시 이후 취소 시에는 향후 서비스 이용에\n제한이 있을 수 있습니다.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
        )

        HorizontalDivider(color = DividerGray, modifier = Modifier.padding(top = 26.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AppBackground)
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "매장", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
                Text(text = reservation.shopName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "예약일시", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
                Text(text = reservation.dateTime, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onKeepReservation,
                modifier = Modifier.weight(1f).height(54.dp)
            ) {
                Text("예약 유지")
            }
            Button(
                onClick = onConfirmCancel,
                colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                modifier = Modifier.weight(1f).height(54.dp)
            ) {
                Text("예약 취소")
            }
        }
    }
}
