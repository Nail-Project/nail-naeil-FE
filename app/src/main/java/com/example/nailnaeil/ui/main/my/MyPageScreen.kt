package com.example.nailnaeil.ui.main.my

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.R
import com.example.nailnaeil.ui.theme.DividerGray
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun MyPageScreen(
    onEditProfileClick: () -> Unit,
    onMyInfoClick: () -> Unit,
    onFavoriteDesignClick: () -> Unit,
    onFavoriteShopClick: () -> Unit,
    onNotificationSettingClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onTermsPolicyClick: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
    ) {
        item {
            Text(
                text = "마이",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onEditProfileClick),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_profile),
                        contentDescription = "프로필 이미지",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(67.dp)
                            .clip(RoundedCornerShape(34.dp))
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = ProfileMockState.nickname,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Row(modifier = Modifier.padding(top = 4.dp)) {
                            Text(
                                text = "진행중인 견적",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Text(
                                text = " 1",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "   시술예정 예약",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Text(
                                text = " 2",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    OutlinedButton(onClick = onEditProfileClick) {
                        Text("수정", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onFavoriteDesignClick),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.Favorite, contentDescription = "찜한 디자인", tint = TextSecondary)
                        Text(text = "찜한 디자인", style = MaterialTheme.typography.bodySmall, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))
                    }
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(32.dp)
                            .background(DividerGray)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onFavoriteShopClick),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.Storefront, contentDescription = "찜한 매장", tint = TextSecondary)
                        Text(text = "찜한 매장", style = MaterialTheme.typography.bodySmall, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, DividerGray, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Text(text = "네일내일 무료 이용 중", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = "N 플러스 플랜으로 업그레이드 시 더 많은 견적을 한 번에 받아볼 수 있어요!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Button(
                        onClick = {
                            Toast.makeText(context, "N 플러스 혜택 화면은 추후 연결됩니다.", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .height(48.dp)
                    ) {
                        Text("더 많은 혜택 받기", color = SurfaceWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(DividerGray)
            )
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "설정 및 안내",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 20.dp, bottom = 12.dp)
                )
                MyPageMenuRow(label = "내 정보", onClick = onMyInfoClick)
                MyPageMenuRow(label = "알림 설정", onClick = onNotificationSettingClick)
                MyPageMenuRow(label = "공지사항", onClick = onNoticeClick)
                MyPageMenuRow(label = "약관 및 정책", onClick = onTermsPolicyClick, showDivider = false)
            }
        }
    }
}

@Composable
private fun MyPageMenuRow(label: String, onClick: () -> Unit, showDivider: Boolean = true) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Icon(Icons.Filled.ChevronRight, contentDescription = "$label 이동", tint = TextSecondary)
        }
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(DividerGray)
            )
        }
    }
}
