package com.example.nailnaeil.data.remote

import com.example.nailnaeil.BuildConfig
import com.example.nailnaeil.data.local.TokenStore
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    fun createRetrofit(tokenStore: TokenStore): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenStore))
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                }
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createDeviceTokenApi(retrofit: Retrofit): DeviceTokenApi = retrofit.create(DeviceTokenApi::class.java)
}

private class AuthInterceptor(private val tokenStore: TokenStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            tokenStore.authToken?.let { token -> addHeader("Authorization", "Bearer $token") }
        }.build()
        return chain.proceed(request)
    }
}
