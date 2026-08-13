package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.ImageUploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageApi {

    @Multipart
    @POST("api/v1/image/upload")
    suspend fun uploadImages(
        @Part images: List<MultipartBody.Part>
    ): Response<ApiResponse<ImageUploadResponse>>
}
