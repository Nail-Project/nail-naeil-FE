package com.example.nailnaeil.ui.main.my.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.ui.quote.components.BackTitleHeader
import com.example.nailnaeil.ui.quote.components.PrimaryBottomButton
import com.example.nailnaeil.ui.theme.AppBackground
import com.example.nailnaeil.ui.theme.BorderLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary

private const val N_PLUS_MONTHLY_PRICE = 3_200

@Composable
fun PlanPaymentScreen(
    onBackClick: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    var agreedToTerms by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().background(SurfaceWhite)) {
        BackTitleHeader(title = "이용권 관리", onBack = onBackClick, modifier = Modifier.padding(horizontal = 16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.5.dp, MutedRosePrimary, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text = "추천",
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedRosePrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(MutedRosePrimary.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
                Text(
                    text = "N 플러스",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Text(
                    text = "더 많은 견적을 한 번에 요청할 수 있는 업그레이드 혜택",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Text(
                text = "결제금액",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 28.dp, bottom = 12.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppBackground)
                    .padding(20.dp)
            ) {
                PriceRow(label = "월 결제 금액", value = "${"%,d".format(N_PLUS_MONTHLY_PRICE)}원")
                PriceRow(label = "할인 혜택", value = "-0원", modifier = Modifier.padding(top = 14.dp))
                PriceRow(label = "쿠폰 할인", value = "-0원", modifier = Modifier.padding(top = 14.dp))
                Text(
                    text = "사용 가능한 쿠폰이 없습니다.",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(1.dp)
                        .background(BorderLight)
                )
                PriceRow(
                    label = "최종 예상 금액",
                    value = "월 ${"%,d".format(N_PLUS_MONTHLY_PRICE)}원",
                    valueColor = MutedRosePrimary,
                    emphasize = true
                )
            }

            Text(
                text = "이용권 구독 유의사항",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 28.dp, bottom = 8.dp)
            )
            repeat(4) {
                Text(
                    text = "- APP (Mobile) 전용 이용권으로, 등록된 결제수단으로 결제됩니다.",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Box(modifier = Modifier.height(16.dp))
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier.clickable { agreedToTerms = !agreedToTerms }
            ) {
                Checkbox(
                    checked = agreedToTerms,
                    onCheckedChange = { agreedToTerms = it },
                    colors = CheckboxDefaults.colors(checkedColor = MutedRosePrimary)
                )
                Text(
                    text = "유료 이용약관에 동의해주세요",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextMain,
                    textDecoration = TextDecoration.Underline
                )
            }
            PrimaryBottomButton(
                text = "확인",
                onClick = onConfirm,
                enabled = agreedToTerms,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun PriceRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: androidx.compose.ui.graphics.Color = TextMain,
    emphasize: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (emphasize) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (emphasize) TextMain else TextSecondary
        )
        Text(
            text = value,
            style = if (emphasize) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}
