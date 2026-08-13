package com.example.nailnaeil.di

import android.content.Context
import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.NetworkModule
import com.example.nailnaeil.data.repository.AdminDesignRepository
import com.example.nailnaeil.data.repository.AdminShopRepository
import com.example.nailnaeil.data.repository.AuthRepository
import com.example.nailnaeil.data.repository.DesignRepository
import com.example.nailnaeil.data.repository.DeviceTokenRepository
import com.example.nailnaeil.data.repository.EstimateRepository
import com.example.nailnaeil.data.repository.ImageRepository
import com.example.nailnaeil.data.repository.NotificationRepository
import com.example.nailnaeil.data.repository.ReservationRepository
import com.example.nailnaeil.data.repository.ReviewRepository
import com.example.nailnaeil.data.repository.ShopRepository
import com.example.nailnaeil.data.repository.UserRepository
import retrofit2.Retrofit

/**
 * Hilt/Koin 없이 앱 전역에서 쓰는 인스턴스를 보관하는 최소 수동 DI 컨테이너.
 * NailNailApplication.onCreate()에서 init() 호출로 초기화된다.
 *
 * 새 API를 붙일 때는 이 파일을 수정할 필요 없이, 각자 Repository에서
 * AppContainer.retrofit.create(XxxApi::class.java) 로 바로 사용하면 된다.
 */
object AppContainer {

    lateinit var tokenStore: TokenStore
        private set

    lateinit var retrofit: Retrofit
        private set

    lateinit var deviceTokenRepository: DeviceTokenRepository
        private set

    lateinit var shopRepository: ShopRepository
        private set

    lateinit var imageRepository: ImageRepository
        private set

    lateinit var authRepository: AuthRepository
        private set

    lateinit var userRepository: UserRepository
        private set

    lateinit var designRepository: DesignRepository
        private set

    lateinit var estimateRepository: EstimateRepository
        private set

    lateinit var reservationRepository: ReservationRepository
        private set

    lateinit var reviewRepository: ReviewRepository
        private set

    lateinit var notificationRepository: NotificationRepository
        private set

    lateinit var adminShopRepository: AdminShopRepository
        private set

    lateinit var adminDesignRepository: AdminDesignRepository
        private set

    fun init(context: Context) {
        tokenStore = TokenStore(context)
        retrofit = NetworkModule.createRetrofit(tokenStore)
        val deviceTokenApi = NetworkModule.createDeviceTokenApi(retrofit)
        deviceTokenRepository = DeviceTokenRepository(deviceTokenApi, tokenStore)
        val shopApi = NetworkModule.createShopApi(retrofit)
        shopRepository = ShopRepository(shopApi)
        val imageApi = NetworkModule.createImageApi(retrofit)
        imageRepository = ImageRepository(imageApi)
        val authApi = NetworkModule.createAuthApi(retrofit)
        authRepository = AuthRepository(authApi, tokenStore)
        val userApi = NetworkModule.createUserApi(retrofit)
        userRepository = UserRepository(userApi, tokenStore)
        val designApi = NetworkModule.createDesignApi(retrofit)
        designRepository = DesignRepository(designApi)
        val estimateApi = NetworkModule.createEstimateApi(retrofit)
        estimateRepository = EstimateRepository(estimateApi)
        val reservationApi = NetworkModule.createReservationApi(retrofit)
        reservationRepository = ReservationRepository(reservationApi)
        val reviewApi = NetworkModule.createReviewApi(retrofit)
        reviewRepository = ReviewRepository(reviewApi)
        val notificationApi = NetworkModule.createNotificationApi(retrofit)
        notificationRepository = NotificationRepository(notificationApi)
        val adminApi = NetworkModule.createAdminApi(retrofit)
        adminShopRepository = AdminShopRepository(adminApi, tokenStore)
        adminDesignRepository = AdminDesignRepository(adminApi, tokenStore)
    }
}
