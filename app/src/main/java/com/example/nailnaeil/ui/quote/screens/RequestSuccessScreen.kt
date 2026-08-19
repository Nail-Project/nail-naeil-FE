package com.example.nailnaeil.ui.quote.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.nailnaeil.R
import com.example.nailnaeil.ui.quote.components.PrimaryBottomButton
import com.example.nailnaeil.ui.theme.MutedRoseBgLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SuccessGreenBg
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun RequestSuccessScreen(
    neighborhood: String,
    shopCount: Int,
    onGoToQuoteBox: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSmsExample by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().background(SurfaceWhite)) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(SuccessGreenBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = MutedRosePrimary, modifier = Modifier.size(32.dp))
            }
            Text(
                text = "견적을 요청했어요",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "$neighborhood 주변 ${shopCount}개 샵이 가격과 가능 시간을 확인 중이예요\n답변이 오는대로 알려드릴게요",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp, start = 32.dp, end = 32.dp)
            )
            Text(
                text = "매장에 전달되는 문자 예시 보기",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MutedRosePrimary,
                modifier = Modifier
                    .padding(top = 18.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { showSmsExample = true }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Box(modifier = Modifier.weight(1f))
        }
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(
                text = "샵 상황에 따라 최대 72시간 소요됩니다",
                fontSize = 11.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )
            PrimaryBottomButton(text = "견적함 바로가기", onClick = onGoToQuoteBox)
        }
    }

    if (showSmsExample) {
        Dialog(onDismissRequest = { showSmsExample = false }) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceWhite)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "매장에 이렇게 전달돼요", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextMain)
                    IconButton(onClick = { showSmsExample = false }) {
                        Icon(Icons.Filled.Close, contentDescription = "닫기")
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MutedRoseBgLight)
                ) {
                    Image(
                        painter = painterResource(R.drawable.sms_example),
                        contentDescription = "매장에 전달되는 문자 예시",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    )
                }
                Text(
                    text = "실제 발송 테스트로 확인한 문자 화면이에요. 데모에서는 이 화면을 참고용으로만 보여드려요.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}
