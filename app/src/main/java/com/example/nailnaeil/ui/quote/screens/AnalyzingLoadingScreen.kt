package com.example.nailnaeil.ui.quote.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nailnaeil.di.AppContainer
import com.example.nailnaeil.ui.quote.QuoteUiState
import com.example.nailnaeil.ui.quote.uriToUploadFile
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary

/**
 * 선택한 사진을 서버에 업로드하는 동안 보여주는 로딩 화면.
 * 업로드가 끝나면 자동으로 다음 단계로 이동한다.
 */
@Composable
fun AnalyzingLoadingScreen(
    state: QuoteUiState,
    onAnalyzed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(state.selectedPhotoUris.toList()) {
        state.isUploadingImages.value = true
        state.uploadError.value = null
        val files = state.selectedPhotoUris.map { uriToUploadFile(context, it) }
        AppContainer.imageRepository.uploadImages(files)
            .onSuccess { urls ->
                state.uploadedImageUrls.clear()
                state.uploadedImageUrls.addAll(urls)
                state.isUploadingImages.value = false
                onAnalyzed()
            }
            .onFailure { e ->
                state.isUploadingImages.value = false
                state.uploadError.value = e.message ?: "이미지 업로드에 실패했어요."
            }
    }

    Column(
        modifier = modifier.fillMaxSize().background(SurfaceWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(top = 220.dp)) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val photos = state.selectedPhotoUris
                if (photos.isEmpty()) {
                    CircularProgressIndicator(color = MutedRosePrimary, strokeWidth = 3.dp, modifier = Modifier.size(40.dp))
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy((-14).dp)) {
                        photos.forEach { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceWhite)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                }

                if (state.uploadError.value != null) {
                    Text(
                        text = state.uploadError.value ?: "",
                        modifier = Modifier.padding(top = 20.dp),
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Button(
                        onClick = onAnalyzed,
                        colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Text("사진 없이 계속하기")
                    }
                } else {
                    Text(
                        text = "사진을 업로드하고 있어요",
                        modifier = Modifier.padding(top = 20.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                }
            }
        }
    }
}
