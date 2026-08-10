package com.example.nailnaeil.ui.main.reservation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.ui.main.reservation.Reservation
import com.example.nailnaeil.ui.main.reservation.ReservationStatus
import com.example.nailnaeil.ui.theme.DangerBgLight
import com.example.nailnaeil.ui.theme.DangerRed
import com.example.nailnaeil.ui.theme.DividerGray
import com.example.nailnaeil.ui.theme.MutedRoseBgLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextDisabled

@Composable
fun ReservationCard(
    reservation: Reservation,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCancelled = reservation.status == ReservationStatus.CANCELLED

    Column(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (isCancelled) 0.55f else 1f)
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceWhite)
            .padding(16.dp)
    ) {
        val (badgeBg, badgeColor) = when (reservation.status) {
            ReservationStatus.CONFIRMED -> MutedRoseBgLight to MutedRosePrimary
            ReservationStatus.COMPLETED -> DividerGray to TextDisabled
            ReservationStatus.CANCELLED -> DangerBgLight to DangerRed
        }
        val dateTimeColor = when (reservation.status) {
            ReservationStatus.CONFIRMED -> MutedRosePrimary
            ReservationStatus.COMPLETED -> TextDisabled
            ReservationStatus.CANCELLED -> TextDisabled
        }
        val textDecoration = if (isCancelled) TextDecoration.LineThrough else TextDecoration.None

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = reservation.status.badgeLabel,
                style = MaterialTheme.typography.labelMedium,
                color = badgeColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(badgeBg)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
            Text(
                text = reservation.dateTime,
                style = MaterialTheme.typography.bodyMedium.copy(textDecoration = textDecoration),
                color = dateTimeColor,
                fontWeight = FontWeight.Bold
            )
        }

        Row(modifier = Modifier.padding(top = 10.dp)) {
            Image(
                painter = painterResource(id = reservation.imageRes),
                contentDescription = "${reservation.shopName} 네일 디자인",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(9.dp))
            )
            Column(modifier = Modifier.padding(start = 18.dp)) {
                Text(
                    text = reservation.shopName,
                    style = MaterialTheme.typography.titleMedium.copy(textDecoration = textDecoration),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = reservation.design,
                    style = MaterialTheme.typography.bodyMedium.copy(textDecoration = textDecoration),
                    modifier = Modifier.padding(top = 7.dp)
                )
                Text(
                    text = reservation.option,
                    style = MaterialTheme.typography.bodySmall.copy(textDecoration = textDecoration),
                    color = TextDisabled,
                    modifier = Modifier.padding(top = 6.dp)
                )
                if (reservation.status != ReservationStatus.CANCELLED) {
                    Text(
                        text = "%,d원".format(reservation.price),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }

        when (reservation.status) {
            ReservationStatus.CONFIRMED -> {
                OutlinedButton(
                    onClick = onDetailClick,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                        .height(50.dp)
                ) {
                    Text("자세히 보기")
                }
                Text(
                    text = "예약 변경 및 취소",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DangerRed,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DangerBgLight)
                        .clickable(onClick = onDetailClick)
                        .padding(vertical = 13.dp)
                )
            }

            ReservationStatus.COMPLETED -> {
                Text(
                    text = "시술이 완료된 견적입니다",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextDisabled,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DividerGray)
                        .padding(vertical = 13.dp)
                )
            }

            ReservationStatus.CANCELLED -> Unit
        }
    }
}
