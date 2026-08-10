package com.example.nailnaeil.di

import android.content.Context
import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.NetworkModule
import com.example.nailnaeil.data.repository.DeviceTokenRepository
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

    fun init(context: Context) {
        tokenStore = TokenStore(context)
        retrofit = NetworkModule.createRetrofit(tokenStore)
        val deviceTokenApi = NetworkModule.createDeviceTokenApi(retrofit)
        deviceTokenRepository = DeviceTokenRepository(deviceTokenApi, tokenStore)
    }
}
