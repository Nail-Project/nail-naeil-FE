package com.example.nailnaeil.ui.main.reservation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun ReservationCompleteScreen(onGoToReservations: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = MutedRosePrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "예약이 완료되었어요",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "완료된 예약은 예약함에서 확인 가능합니다.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Button(
            onClick = onGoToReservations,
            colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("예약함 바로가기")
        }
    }
}
