package com.example.nailnaeil.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nailnaeil.data.remote.EstimateResultApi
import com.example.nailnaeil.data.remote.EstimateTimeApi
import com.example.nailnaeil.data.remote.ReservationApi
import com.example.nailnaeil.data.remote.UserApi
import com.example.nailnaeil.data.repository.EstimateResultRepository
import com.example.nailnaeil.data.repository.EstimateTimeRepository
import com.example.nailnaeil.data.repository.ReservationRepository
import com.example.nailnaeil.data.repository.UserRepository
import com.example.nailnaeil.di.AppContainer
import com.example.nailnaeil.ui.main.MainScaffold
import com.example.nailnaeil.ui.main.address.AddressEditScreen
import com.example.nailnaeil.ui.main.address.AddressFormScreen
import com.example.nailnaeil.ui.main.address.AddressSettingsScreen
import com.example.nailnaeil.ui.main.estimate.EstimateComparisonScreen
import com.example.nailnaeil.ui.main.estimate.ShopDetailScreen
import com.example.nailnaeil.ui.main.my.EditProfileScreen
import com.example.nailnaeil.ui.main.my.FavoriteDesignScreen
import com.example.nailnaeil.ui.main.my.FavoriteShopScreen
import com.example.nailnaeil.ui.main.my.MyInfoScreen
import com.example.nailnaeil.ui.main.my.NoticeScreen
import com.example.nailnaeil.ui.main.my.NotificationSettingScreen
import com.example.nailnaeil.ui.main.my.TermsPolicyScreen
import com.example.nailnaeil.ui.main.reservation.Reservation
import com.example.nailnaeil.ui.main.reservation.ReservationCompleteScreen
import com.example.nailnaeil.ui.main.reservation.ReservationDetailScreen
import com.example.nailnaeil.ui.main.reservation.ReservationMockState
import com.example.nailnaeil.ui.main.reservation.ReservationStatus
import com.example.nailnaeil.ui.onboarding.KakaoConsentScreen
import com.example.nailnaeil.ui.onboarding.LoginScreen
import com.example.nailnaeil.ui.onboarding.PermissionScreen
import com.example.nailnaeil.ui.onboarding.SignupCompleteScreen
import com.example.nailnaeil.ui.onboarding.SplashScreen
import com.example.nailnaeil.ui.quote.QuoteFlow
import kotlinx.coroutines.launch

@Composable
fun NailNailNavGraph(
    navController: NavHostController =
        rememberNavController()
) {
    var mainSelectedTab by remember {
        mutableStateOf(
            MainTabRoutes.HOME
        )
    }

    val coroutineScope =
        rememberCoroutineScope()

    val context =
        LocalContext.current

    val userRepository =
        remember {
            UserRepository(
                userApi =
                    AppContainer
                        .retrofit
                        .create(
                            UserApi::class.java
                        ),

                tokenStore =
                    AppContainer.tokenStore
            )
        }

    val reservationRepository =
        remember {
            ReservationRepository(
                reservationApi =
                    AppContainer
                        .retrofit
                        .create(
                            ReservationApi::class.java
                        ),

                tokenStore =
                    AppContainer.tokenStore
            )
        }

    val estimateResultRepository =
        remember {
            EstimateResultRepository(
                estimateResultApi =
                    AppContainer
                        .retrofit
                        .create(
                            EstimateResultApi::class.java
                        ),

                tokenStore =
                    AppContainer.tokenStore
            )
        }

    val estimateTimeRepository =
        remember {
            EstimateTimeRepository(
                estimateTimeApi =
                    AppContainer
                        .retrofit
                        .create(
                            EstimateTimeApi::class.java
                        ),

                tokenStore =
                    AppContainer.tokenStore
            )
        }

    fun onLoginSuccess() {

        coroutineScope.launch {

            AppContainer
                .deviceTokenRepository
                .registerCurrentDeviceToken()
        }
    }

    NavHost(
        navController =
            navController,

        startDestination =
            Routes.SPLASH
    ) {

        composable(
            Routes.SPLASH
        ) {

            SplashScreen(
                onTimeout = {

                    navController.navigate(
                        Routes.PERMISSION
                    ) {

                        popUpTo(
                            Routes.SPLASH
                        ) {
                            inclusive =
                                true
                        }
                    }
                }
            )
        }

        composable(
            Routes.PERMISSION
        ) {

            PermissionScreen(
                onContinue = {

                    navController.navigate(
                        Routes.LOGIN
                    )
                }
            )
        }

        composable(
            Routes.LOGIN
        ) {

            LoginScreen(
                onKakaoLoginClick = {

                    navController.navigate(
                        Routes.KAKAO_CONSENT
                    )
                },

                onNaverLoginClick = {

                    onLoginSuccess()

                    navController.navigate(
                        Routes.MAIN
                    ) {

                        popUpTo(
                            Routes.PERMISSION
                        ) {
                            inclusive =
                                true
                        }
                    }
                }
            )
        }

        composable(
            Routes.KAKAO_CONSENT
        ) {

            KakaoConsentScreen(
                onAgreeClick = {

                    navController.navigate(
                        Routes.SIGNUP_COMPLETE
                    )
                }
            )
        }

        composable(
            Routes.SIGNUP_COMPLETE
        ) {

            SignupCompleteScreen(
                onStartClick = {

                    onLoginSuccess()

                    navController.navigate(
                        Routes.MAIN
                    ) {

                        popUpTo(
                            Routes.PERMISSION
                        ) {
                            inclusive =
                                true
                        }
                    }
                }
            )
        }

        composable(
            Routes.MAIN
        ) {

            var confirmedReservations by remember {
                mutableStateOf<
                        List<Reservation>
                        >(
                    emptyList()
                )
            }

            var pastReservations by remember {
                mutableStateOf<
                        List<Reservation>
                        >(
                    emptyList()
                )
            }

            var isReservationLoading by remember {
                mutableStateOf(
                    false
                )
            }

            var reservationErrorMessage by remember {
                mutableStateOf<String?>(
                    null
                )
            }

            fun loadReservations() {

                if (
                    isReservationLoading
                ) {
                    return
                }

                isReservationLoading =
                    true

                reservationErrorMessage =
                    null

                coroutineScope.launch {

                    val confirmedResult =
                        reservationRepository
                            .getReservations(
                                status =
                                    "CONFIRMED",

                                page =
                                    0,

                                size =
                                    10
                            )

                    val pastResult =
                        reservationRepository
                            .getReservations(
                                status =
                                    "PAST",

                                page =
                                    0,

                                size =
                                    10
                            )

                    if (
                        confirmedResult.isSuccess &&
                        pastResult.isSuccess
                    ) {

                        val confirmedResponse =
                            confirmedResult
                                .getOrThrow()

                        val pastResponse =
                            pastResult
                                .getOrThrow()

                        confirmedReservations =
                            confirmedResponse
                                .reservations
                                .map {
                                        item ->

                                    item.toReservation()
                                }

                        pastReservations =
                            pastResponse
                                .reservations
                                .map {
                                        item ->

                                    item.toReservation()
                                }

                    } else {

                        val error =
                            confirmedResult
                                .exceptionOrNull()
                                ?: pastResult
                                    .exceptionOrNull()

                        reservationErrorMessage =
                            error?.message
                                ?: "예약 목록을 불러오지 못했습니다."
                    }

                    isReservationLoading =
                        false
                }
            }

            LaunchedEffect(
                mainSelectedTab
            ) {

                if (
                    mainSelectedTab ==
                    MainTabRoutes.RESERVATION
                ) {

                    loadReservations()
                }
            }

            MainScaffold(
                selectedTab =
                    mainSelectedTab,

                onTabSelected = {
                    mainSelectedTab =
                        it
                },

                onAddressClick = {

                    navController.navigate(
                        Routes.ADDRESS_SETTINGS
                    )
                },

                onNotificationClick =
                    {},

                onNeedUpgrade =
                    {},

                onStartEstimate = {

                    navController.navigate(
                        Routes.QUOTE_FLOW
                    )
                },

                onMagazineClick =
                    {},

                onEstimateClick =
                    {},

                onEstimateItemClick = {
                        item ->

                    navController.navigate(
                        Routes
                            .estimateComparison(
                                item.id
                            )
                    )
                },

                confirmedReservations =
                    confirmedReservations,

                pastReservations =
                    pastReservations,

                isReservationLoading =
                    isReservationLoading,

                reservationErrorMessage =
                    reservationErrorMessage,

                onReservationRetryClick = {

                    loadReservations()
                },

                onReservationItemClick = {
                        reservationId ->

                    navController.navigate(
                        Routes.reservationDetail(
                            reservationId
                        )
                    )
                },

                onEditProfileClick = {

                    navController.navigate(
                        Routes.EDIT_PROFILE
                    )
                },

                onMyInfoClick = {

                    navController.navigate(
                        Routes.MY_INFO
                    )
                },

                onFavoriteDesignClick = {

                    navController.navigate(
                        Routes.FAVORITE_DESIGN
                    )
                },

                onFavoriteShopClick = {

                    navController.navigate(
                        Routes.FAVORITE_SHOP
                    )
                },

                onNotificationSettingClick = {

                    navController.navigate(
                        Routes.NOTIFICATION_SETTING
                    )
                },

                onNoticeClick = {

                    navController.navigate(
                        Routes.NOTICE
                    )
                },

                onTermsPolicyClick = {

                    navController.navigate(
                        Routes.TERMS_POLICY
                    )
                }
            )
        }

        composable(
            Routes.QUOTE_FLOW
        ) {

            QuoteFlow(
                onFinish = {

                    navController
                        .popBackStack()
                }
            )
        }

        composable(
            Routes.ESTIMATE_COMPARISON
        ) { backStackEntry ->

            val estimateId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "estimateId"
                    )
                    .orEmpty()

            EstimateComparisonScreen(
                estimateId =
                    estimateId,

                estimateResultRepository =
                    estimateResultRepository,

                estimateTimeRepository =
                    estimateTimeRepository,

                reservationRepository =
                    reservationRepository,

                onBackClick = {

                    navController
                        .popBackStack()
                },

                onShopDetailClick = {
                        proposalId ->

                    navController.navigate(
                        Routes.shopDetail(
                            proposalId
                        )
                    )
                },

                onReservationConfirmed = {

                    navController.navigate(
                        Routes.RESERVATION_COMPLETE
                    )
                }
            )
        }

        composable(
            Routes.SHOP_DETAIL
        ) { backStackEntry ->

            val shopId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "shopId"
                    )
                    .orEmpty()

            ShopDetailScreen(
                shopId =
                    shopId,

                onBackClick = {

                    navController
                        .popBackStack()
                },

                onReservationConfirmed = {
                        reservation ->

                    ReservationMockState
                        .addConfirmed(
                            reservation
                        )

                    navController.navigate(
                        Routes.RESERVATION_COMPLETE
                    )
                }
            )
        }

        composable(
            Routes.RESERVATION_COMPLETE
        ) {

            ReservationCompleteScreen(
                onGoToReservations = {

                    mainSelectedTab =
                        MainTabRoutes.RESERVATION

                    navController
                        .popBackStack(
                            Routes.MAIN,
                            inclusive =
                                false
                        )
                }
            )
        }

        composable(
            Routes.RESERVATION_DETAIL
        ) { backStackEntry ->

            val reservationId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "reservationId"
                    )
                    .orEmpty()

            ReservationDetailScreen(
                reservationId =
                    reservationId,

                reservationRepository =
                    reservationRepository,

                onBackClick = {

                    navController
                        .popBackStack()
                },

                onCancelled = {

                    navController
                        .popBackStack()
                }
            )
        }

        composable(
            Routes.MY_INFO
        ) {

            var isLoggingOut by remember {
                mutableStateOf(
                    false
                )
            }

            var isDeletingAccount by remember {
                mutableStateOf(
                    false
                )
            }

            MyInfoScreen(
                onBackClick = {

                    navController
                        .popBackStack()
                },

                onEditProfileClick = {

                    navController.navigate(
                        Routes.EDIT_PROFILE
                    )
                },

                onLogoutClick = {

                    if (
                        !isLoggingOut
                    ) {

                        isLoggingOut =
                            true

                        coroutineScope.launch {

                            userRepository
                                .logout()
                                .onSuccess {

                                    Toast.makeText(
                                        context,
                                        "로그아웃되었습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.navigate(
                                        Routes.LOGIN
                                    ) {

                                        popUpTo(
                                            navController
                                                .graph
                                                .id
                                        ) {
                                            inclusive =
                                                true
                                        }

                                        launchSingleTop =
                                            true
                                    }
                                }
                                .onFailure {
                                        exception ->

                                    isLoggingOut =
                                        false

                                    Toast.makeText(
                                        context,

                                        exception.message
                                            ?: "로그아웃에 실패했습니다.",

                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                },

                onDeleteAccountClick = {

                    if (
                        !isDeletingAccount
                    ) {

                        isDeletingAccount =
                            true

                        coroutineScope.launch {

                            userRepository
                                .deleteAccount()
                                .onSuccess {

                                    Toast.makeText(
                                        context,
                                        "회원탈퇴가 완료되었습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.navigate(
                                        Routes.LOGIN
                                    ) {

                                        popUpTo(
                                            navController
                                                .graph
                                                .id
                                        ) {
                                            inclusive =
                                                true
                                        }

                                        launchSingleTop =
                                            true
                                    }
                                }
                                .onFailure {
                                        exception ->

                                    isDeletingAccount =
                                        false

                                    Toast.makeText(
                                        context,

                                        exception.message
                                            ?: "회원탈퇴에 실패했습니다.",

                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                },

                isLoggingOut =
                    isLoggingOut,

                isDeletingAccount =
                    isDeletingAccount
            )
        }

        composable(
            Routes.EDIT_PROFILE
        ) {

            EditProfileScreen(
                onBackClick = {

                    navController
                        .popBackStack()
                }
            )
        }

        composable(
            Routes.FAVORITE_DESIGN
        ) {

            FavoriteDesignScreen(
                onBackClick = {

                    navController
                        .popBackStack()
                }
            )
        }

        composable(
            Routes.FAVORITE_SHOP
        ) {

            FavoriteShopScreen(
                onBackClick = {

                    navController
                        .popBackStack()
                }
            )
        }

        composable(
            Routes.NOTIFICATION_SETTING
        ) {

            NotificationSettingScreen(
                onBackClick = {

                    navController
                        .popBackStack()
                }
            )
        }

        composable(
            Routes.NOTICE
        ) {

            NoticeScreen(
                onBackClick = {

                    navController
                        .popBackStack()
                }
            )
        }

        composable(
            Routes.TERMS_POLICY
        ) {

            TermsPolicyScreen(
                onBackClick = {

                    navController
                        .popBackStack()
                }
            )
        }

        composable(
            Routes.ADDRESS_SETTINGS
        ) {

            AddressSettingsScreen(
                onBackClick = {

                    navController
                        .popBackStack()
                },

                onEditClick = {

                    navController.navigate(
                        Routes.ADDRESS_EDIT
                    )
                },

                onAddressSelected = {

                    navController
                        .popBackStack()
                }
            )
        }

        composable(
            Routes.ADDRESS_EDIT
        ) {

            AddressEditScreen(
                onBackClick = {

                    navController
                        .popBackStack()
                },

                onModifyClick = {
                        address ->

                    navController.navigate(
                        Routes.addressForm(
                            address.id
                        )
                    )
                }
            )
        }

        composable(
            Routes.ADDRESS_FORM
        ) { backStackEntry ->

            val addressId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "addressId"
                    )
                    .orEmpty()

            AddressFormScreen(
                addressId =
                    addressId,

                onBackClick = {

                    navController
                        .popBackStack()
                },

                onSaved = {

                    navController
                        .popBackStack()
                }
            )
        }
    }
}

private fun com.example.nailnaeil.data.remote.dto.ReservationListItemResponse
        .toReservation(): Reservation {

    return Reservation(
        id =
            reservationId
                .toString(),

        status =
            when (
                status.uppercase()
            ) {

                "CONFIRMED" ->
                    ReservationStatus
                        .CONFIRMED

                "CANCELLED" ->
                    ReservationStatus
                        .CANCELLED

                else ->
                    ReservationStatus
                        .COMPLETED
            },

        dateTime =
            formatReservationDate(
                reservedAt
            ),

        shopName =
            shopName,

        design =
            nailTypeLabel(
                nailType
            ),

        option =
            removalTypeLabel(
                removalType
            ),

        price =
            totalPrice,

        imageUrl =
            shopThumbnailUrl,

        proposalId =
            proposalId,

        nailType =
            nailType,

        removalType =
            removalType
    )
}

private fun formatReservationDate(
    reservedAt: String
): String {

    if (
        reservedAt.length < 16
    ) {
        return reservedAt
    }

    return try {

        val month =
            reservedAt.substring(
                5,
                7
            )

        val day =
            reservedAt.substring(
                8,
                10
            )

        val hour =
            reservedAt.substring(
                11,
                13
            )

        val minute =
            reservedAt.substring(
                14,
                16
            )

        "$month.$day · $hour:$minute"

    } catch (
        exception: Exception
    ) {
        reservedAt
    }
}

private fun nailTypeLabel(
    nailType: String
): String {

    return when (
        nailType.uppercase()
    ) {

        "HAND" ->
            "손 네일"

        "PEDICURE" ->
            "페디큐어"

        "BOTH" ->
            "손·발 네일"

        else ->
            nailType
    }
}

private fun removalTypeLabel(
    removalType: String
): String {

    return when (
        removalType.uppercase()
    ) {

        "NONE" ->
            "제거 없음"

        "GEL" ->
            "젤 제거"

        "PARTS" ->
            "파츠 제거"

        else ->
            removalType
    }
}