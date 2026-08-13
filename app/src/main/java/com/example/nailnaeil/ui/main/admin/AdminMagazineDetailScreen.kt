package com.example.nailnaeil.ui.main.admin

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nailnaeil.data.remote.dto.DesignAdminCreateRequest
import com.example.nailnaeil.data.remote.dto.DesignAdminUpdateRequest
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMagazineDetailScreen(
    designId: Long?,
    onBackClick: () -> Unit,
    onSaved: () -> Unit,
    viewModel: AdminMagazineDetailViewModel = viewModel { AdminMagazineDetailViewModel(designId) }
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val existing = uiState.existingDesign

    var title by remember(existing) { mutableStateOf(existing?.title ?: "") }
    var imageUrl by remember(existing) { mutableStateOf(existing?.imageUrl ?: "") }
    var durationMinutes by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("") }
    var recommendedShape by remember { mutableStateOf("") }
    var description by remember(existing) { mutableStateOf(existing?.description ?: "") }
    var tagsText by remember(existing) { mutableStateOf(existing?.tags?.joinToString(", ") ?: "") }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            Toast.makeText(context, "저장했어요.", Toast.LENGTH_SHORT).show()
            onSaved()
        }
    }

    LaunchedEffect(uiState.saveError) {
        uiState.saveError?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (viewModel.isCreateMode) "매거진 등록" else "매거진 수정") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    if (!viewModel.isCreateMode) {
                        Text(
                            text = "예상 시술가·최근 견적가·비슷한 디자인은 실제 견적 데이터로 서버가 자동 계산해요. " +
                                "이 화면에서 직접 편집할 수 있는 값이 아니라 표시하지 않아요.",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }

                    AdminField(label = "제목", value = title, onValueChange = { title = it })
                    AdminField(label = "대표 이미지 URL", value = imageUrl, onValueChange = { imageUrl = it })
                    AdminField(
                        label = "시술 시간(분)",
                        value = durationMinutes,
                        onValueChange = { durationMinutes = it },
                        keyboardType = KeyboardType.Number
                    )
                    AdminField(label = "난이도 (예: 낮음/보통/높음)", value = difficulty, onValueChange = { difficulty = it })
                    AdminField(label = "추천 쉐입 (예: 라운드/스퀘어/아몬드)", value = recommendedShape, onValueChange = { recommendedShape = it })
                    AdminField(label = "디자인 설명", value = description, onValueChange = { description = it }, singleLine = false)
                    AdminField(label = "카테고리 (쉼표로 구분, 예: 프렌치, 파츠)", value = tagsText, onValueChange = { tagsText = it })

                    Row(modifier = Modifier.padding(top = 32.dp)) {
                        OutlinedButton(onClick = onBackClick, modifier = Modifier.weight(1f)) {
                            Text("취소")
                        }
                        Button(
                            onClick = {
                                val tags = tagsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                                if (viewModel.isCreateMode) {
                                    val duration = durationMinutes.toIntOrNull()
                                    if (title.isBlank() || imageUrl.isBlank() || duration == null || difficulty.isBlank() ||
                                        recommendedShape.isBlank() || description.isBlank()
                                    ) {
                                        Toast.makeText(context, "필수 항목을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    viewModel.create(
                                        DesignAdminCreateRequest(
                                            title = title,
                                            imageUrl = imageUrl,
                                            durationMinutes = duration,
                                            difficulty = difficulty,
                                            recommendedShape = recommendedShape,
                                            description = description,
                                            tags = tags.ifEmpty { null }
                                        )
                                    )
                                } else {
                                    viewModel.update(
                                        DesignAdminUpdateRequest(
                                            title = title.ifBlank { null },
                                            imageUrl = imageUrl.ifBlank { null },
                                            durationMinutes = durationMinutes.toIntOrNull(),
                                            difficulty = difficulty.ifBlank { null },
                                            recommendedShape = recommendedShape.ifBlank { null },
                                            description = description.ifBlank { null },
                                            tags = tags.ifEmpty { null }
                                        )
                                    )
                                }
                            },
                            enabled = !uiState.isSaving,
                            colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                        ) {
                            Text(if (uiState.isSaving) "저장 중..." else "저장", color = SurfaceWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .let { if (!singleLine) it.height(120.dp) else it }
        )
    }
}
