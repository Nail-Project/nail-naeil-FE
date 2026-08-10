package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException

private val errorParser = Gson()

/** success에 실제 데이터가 담기는 API용. body.success가 null이면 실패로 취급한다. */
suspend fun <T : Any> apiCall(call: suspend () -> Response<ApiResponse<T>>): Result<T> {
    return try {
        val response = call()
        val success = response.body()?.success
        if (response.isSuccessful && success != null) {
            Result.success(success)
        } else {
            Result.failure(toApiException(response))
        }
    } catch (e: IOException) {
        Result.failure(ApiException(null, "네트워크 연결을 확인해주세요."))
    } catch (e: Exception) {
        Result.failure(ApiException(null, e.message ?: "알 수 없는 오류가 발생했습니다."))
    }
}

/** 응답 본문이 없거나 의미 없는 API용(등록/해제/로그아웃 등). HTTP 상태 코드만으로 성공 여부 판단. */
suspend fun apiCallUnit(call: suspend () -> Response<Unit>): Result<Unit> {
    return try {
        val response = call()
        if (response.isSuccessful) Result.success(Unit) else Result.failure(toApiException(response))
    } catch (e: IOException) {
        Result.failure(ApiException(null, "네트워크 연결을 확인해주세요."))
    } catch (e: Exception) {
        Result.failure(ApiException(null, e.message ?: "알 수 없는 오류가 발생했습니다."))
    }
}

private fun toApiException(response: Response<*>): ApiException {
    val error = response.errorBody()?.string()?.let { raw ->
        runCatching { errorParser.fromJson(raw, ApiResponse::class.java).error }.getOrNull()
    }
    return ApiException(error?.code, error?.message ?: "요청 처리 중 오류가 발생했습니다. (HTTP ${response.code()})")
}
