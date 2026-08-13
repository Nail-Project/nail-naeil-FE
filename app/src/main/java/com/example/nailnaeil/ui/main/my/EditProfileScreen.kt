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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nailnaeil.R
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel { ProfileViewModel() }
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val profile = uiState.profile
    var email by remember(profile?.email) { mutableStateOf(profile?.email ?: "") }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            viewModel.consumeSaveSuccess()
            onBackClick()
        }
    }

    LaunchedEffect(uiState.saveError) {
        uiState.saveError?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("프로필 수정") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.updateEmail(email) }, enabled = !uiState.isSaving) {
                        Text(if (uiState.isSaving) "저장 중..." else "저장")
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
                    AsyncImage(
                        model = profile?.profileImageUrl,
                        placeholder = painterResource(id = R.drawable.img_profile),
                        error = painterResource(id = R.drawable.img_profile),
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

            // 닉네임·전화번호는 가입 시 값이 고정되어 서버에서 수정을 지원하지 않는다(읽기 전용으로 표시).
            ProfileField(
                label = "닉네임",
                value = profile?.nickname ?: "",
                onValueChange = {},
                placeholder = "닉네임",
                enabled = false
            )
            ProfileField(
                label = "전화번호",
                value = profile?.phoneNumber ?: "",
                onValueChange = {},
                placeholder = "전화번호",
                enabled = false,
                keyboardType = KeyboardType.Phone
            )
            ProfileField(
                label = "이메일",
                value = email,
                onValueChange = { email = it },
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
