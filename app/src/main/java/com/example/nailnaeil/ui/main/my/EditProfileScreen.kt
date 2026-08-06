package com.example.nailnaeil.ui.main.my

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.R
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onSaveClick: (
        nickname: String,
        name: String,
        phoneNumber: String,
        email: String
    ) -> Unit,
    isSaving: Boolean = false
) {
    val context = LocalContext.current

    var nickname by rememberSaveable {
        mutableStateOf(ProfileMockState.nickname)
    }

    var name by rememberSaveable {
        mutableStateOf(ProfileMockState.name)
    }

    var phoneNumber by rememberSaveable {
        mutableStateOf(ProfileMockState.phone)
    }

    var email by rememberSaveable {
        mutableStateOf(ProfileMockState.email)
    }

    val canSave =
        nickname.isNotBlank() &&
                phoneNumber.isNotBlank() &&
                email.isNotBlank() &&
                !isSaving

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("프로필 수정")
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
                },
                actions = {
                    TextButton(
                        enabled = canSave,
                        onClick = {
                            onSaveClick(
                                nickname.trim(),
                                name.trim(),
                                phoneNumber.trim(),
                                email.trim()
                            )
                        }
                    ) {
                        Text(
                            text = if (isSaving) {
                                "저장 중..."
                            } else {
                                "완료"
                            }
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
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 22.dp,
                        bottom = 18.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box {
                    Image(
                        painter = painterResource(
                            id = R.drawable.img_profile
                        ),
                        contentDescription = "프로필 이미지",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(SurfaceWhite)
                            .clickable {
                                Toast.makeText(
                                    context,
                                    "프로필 이미지 변경 기능은 추후 연결됩니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "프로필 이미지 변경"
                        )
                    }
                }
            }

            ProfileField(
                label = "닉네임",
                value = nickname,
                onValueChange = {
                    if (it.length <= 12) {
                        nickname = it
                    }
                },
                placeholder = "닉네임을 입력해주세요"
            )

            ProfileField(
                label = "이름",
                value = name,
                onValueChange = {
                    name = it
                },
                placeholder = "이름을 입력해주세요"
            )

            ProfileField(
                label = "전화번호",
                value = phoneNumber,
                onValueChange = { input ->
                    phoneNumber = input
                        .filter { it.isDigit() }
                        .take(11)
                },
                placeholder = "01012345678",
                keyboardType = KeyboardType.Phone
            )

            ProfileField(
                label = "이메일",
                value = email,
                onValueChange = {
                    email = it
                },
                placeholder = "이메일을 입력해주세요",
                keyboardType = KeyboardType.Email
            )
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
) {
    Column(
        modifier = Modifier.padding(top = 24.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(placeholder)
            },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
    }
}