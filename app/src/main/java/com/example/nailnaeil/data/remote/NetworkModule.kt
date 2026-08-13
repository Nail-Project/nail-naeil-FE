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

    fun createShopApi(retrofit: Retrofit): ShopApi = retrofit.create(ShopApi::class.java)

    fun createImageApi(retrofit: Retrofit): ImageApi = retrofit.create(ImageApi::class.java)

    fun createAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    fun createUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    fun createDesignApi(retrofit: Retrofit): DesignApi = retrofit.create(DesignApi::class.java)

    fun createEstimateApi(retrofit: Retrofit): EstimateApi = retrofit.create(EstimateApi::class.java)

    fun createReservationApi(retrofit: Retrofit): ReservationApi = retrofit.create(ReservationApi::class.java)

    fun createReviewApi(retrofit: Retrofit): ReviewApi = retrofit.create(ReviewApi::class.java)

    fun createNotificationApi(retrofit: Retrofit): NotificationApi = retrofit.create(NotificationApi::class.java)

    fun createAdminApi(retrofit: Retrofit): AdminApi = retrofit.create(AdminApi::class.java)
}

private class AuthInterceptor(private val tokenStore: TokenStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            tokenStore.authToken?.let { token -> addHeader("Authorization", "Bearer $token") }
        }.build()
        return chain.proceed(request)
    }
}
