package com.example.nailnaeil.ui.main.my

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.ui.theme.AppBackground
import com.example.nailnaeil.ui.theme.DividerGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyInfoScreen(
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    isLoggingOut: Boolean = false,
    isDeletingAccount: Boolean = false
) {
    val context = LocalContext.current

    var showDeleteAccountDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("내 정보 수정")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(AppBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surface
                    )
                    .padding(horizontal = 16.dp)
            ) {
                MyInfoRow(
                    label = "닉네임",
                    value = ProfileMockState.nickname,
                    onClick = onEditProfileClick
                )

                MyInfoRow(
                    label = "이름",
                    value = ProfileMockState.name
                        .ifBlank { "김지민" },
                    onClick = onEditProfileClick
                )

                MyInfoRow(
                    label = "휴대폰 번호",
                    value = ProfileMockState.phone
                        .ifBlank { "010-0000-0000" },
                    onClick = onEditProfileClick
                )

                MyInfoRow(
                    label = "간편 로그인",
                    value = "카카오",
                    onClick = {
                        Toast.makeText(
                            context,
                            "카카오 간편 로그인 계정입니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )

                MyInfoRow(
                    label = "언어",
                    value = "한국어",
                    onClick = {
                        Toast.makeText(
                            context,
                            "현재 한국어로 설정되어 있습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    showDivider = false
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(AppBackground)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surface
                    )
            ) {
                Text(
                    text = if (isLoggingOut) {
                        "로그아웃 중..."
                    } else {
                        "로그아웃"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            enabled = !isLoggingOut &&
                                    !isDeletingAccount,
                            onClick = onLogoutClick
                        )
                        .padding(
                            horizontal = 16.dp,
                            vertical = 18.dp
                        )
                )

                Text(
                    text = if (isDeletingAccount) {
                        "회원탈퇴 중..."
                    } else {
                        "회원탈퇴"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            enabled = !isDeletingAccount &&
                                    !isLoggingOut
                        ) {
                            showDeleteAccountDialog = true
                        }
                        .padding(
                            horizontal = 16.dp,
                            vertical = 14.dp
                        )
                )
            }
        }
    }

    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isDeletingAccount) {
                    showDeleteAccountDialog = false
                }
            },
            title = {
                Text(
                    text = "회원탈퇴",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "정말 회원탈퇴하시겠어요?\n탈퇴 후에는 계정 정보를 복구할 수 없습니다."
                )
            },
            confirmButton = {
                TextButton(
                    enabled = !isDeletingAccount,
                    onClick = {
                        showDeleteAccountDialog = false
                        onDeleteAccountClick()
                    }
                ) {
                    Text(
                        text = "탈퇴하기",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !isDeletingAccount,
                    onClick = {
                        showDeleteAccountDialog = false
                    }
                ) {
                    Text("취소")
                }
            }
        )
    }
}

@Composable
private fun MyInfoRow(
    label: String,
    value: String,
    onClick: () -> Unit,
    showDivider: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onClick
                )
                .padding(vertical = 20.dp),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector =
                        Icons.Filled.ChevronRight,
                    contentDescription =
                        "$label 수정"
                )
            }
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