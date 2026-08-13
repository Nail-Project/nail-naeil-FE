package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.remote.ImageApi
import com.example.nailnaeil.data.remote.apiCall
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/** 최대 3개까지 이미지를 업로드하고 S3 URL 목록을 반환한다. */
class ImageRepository(
    private val api: ImageApi
) {

    suspend fun uploadImages(images: List<File>): Result<List<String>> {
        val parts = images.map { file ->
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", file.name, requestBody)
        }
        return apiCall { api.uploadImages(parts) }.map { it.imageUrls }
    }
}
