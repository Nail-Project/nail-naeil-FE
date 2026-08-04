package com.example.nailnaeil.ui.main.my

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
fun EditProfileScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("프로필 수정") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
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
                    .padding(top = 22.dp, bottom = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.img_profile),
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
                                Toast.makeText(context, "프로필 이미지 변경 기능은 추후 연결됩니다.", Toast.LENGTH_SHORT).show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = "프로필 이미지 변경")
                    }
                }
            }

            ProfileField(
                label = "닉네임",
                value = ProfileMockState.nickname,
                onValueChange = { if (it.length <= 12) ProfileMockState.nickname = it },
                placeholder = "닉네임을 입력해주세요"
            )
            ProfileField(
                label = "이름",
                value = ProfileMockState.name,
                onValueChange = { ProfileMockState.name = it },
                placeholder = "이름을 입력해주세요"
            )
            ProfileField(
                label = "전화번호",
                value = ProfileMockState.phone,
                onValueChange = { if (it.length <= 13) ProfileMockState.phone = it },
                placeholder = "전화번호를 입력해주세요",
                keyboardType = KeyboardType.Phone
            )
            ProfileField(
                label = "이메일",
                value = ProfileMockState.email,
                onValueChange = {},
                placeholder = "이메일",
                enabled = false
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
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
    }
}
