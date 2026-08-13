package com.example.nailnaeil.ui.quote

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

/** 시스템 포토 피커가 넘겨준 content:// Uri를 업로드용 임시 파일로 복사한다. */
suspend fun uriToUploadFile(context: Context, uri: Uri): File = withContext(Dispatchers.IO) {
    val extension = context.contentResolver.getType(uri)?.substringAfterLast('/') ?: "jpg"
    val file = File(context.cacheDir, "quote_upload_${UUID.randomUUID()}.$extension")
    context.contentResolver.openInputStream(uri)?.use { input ->
        file.outputStream().use { output -> input.copyTo(output) }
    }
    file
}
