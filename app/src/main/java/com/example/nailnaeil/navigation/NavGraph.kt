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
import com.example.nailnaeil.data.remote.BookmarkApi
import com.example.nailnaeil.data.repository.BookmarkRepository
import com.example.nailnaeil.di.AppContainer
import com.example.nailnaeil.ui.main.MainScaffold
import com.example.nailnaeil.ui.main.address.AddressEditScreen
import com.example.nailnaeil.ui.main.address.AddressFormScreen
import com.example.nailnaeil.ui.main.address.AddressSettingsScreen
import com.example.nailnaeil.ui.main.estimate.EstimateComparisonScreen
import com.example.nailnaeil.ui.main.estimate.ShopDetailScreen
import com.example.nailnaeil.ui.main.my.EditProfileScreen
import com.example.nailnaeil.ui.main.my.FavoriteDesignItem
import com.example.nailnaeil.ui.main.my.FavoriteDesignScreen
import com.example.nailnaeil.ui.main.my.FavoriteShopScreen
import com.example.nailnaeil.ui.main.my.MyInfoScreen
import com.example.nailnaeil.ui.main.my.NoticeScreen
import com.example.nailnaeil.ui.main.my.NotificationSettingScreen
import com.example.nailnaeil.ui.main.my.TermsPolicyScreen
import com.example.nailnaeil.ui.main.reservation.ReservationCompleteScreen
import com.example.nailnaeil.ui.main.reservation.ReservationDetailScreen
import com.example.nailnaeil.ui.main.reservation.ReservationMockState
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

    val bookmarkApi =
        remember {
            AppContainer
                .retrofit
                .create(
                    BookmarkApi::class.java
                )
        }

    val bookmarkRepository =
        remember {
            BookmarkRepository(
                bookmarkApi =
                    bookmarkApi,
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
            MainScaffold(
                selectedTab =
                    mainSelectedTab,
                onTabSelected = {
                    mainSelectedTab = it
                },
                onAddressClick = {
                    navController.navigate(
                        Routes.ADDRESS_SETTINGS
                    )
                },
                onNotificationClick = {},
                onNeedUpgrade = {},
                onStartEstimate = {
                    navController.navigate(
                        Routes.QUOTE_FLOW
                    )
                },
                onMagazineClick = {},
                onEstimateClick = {},
                onEstimateItemClick = { item ->
                    navController.navigate(
                        Routes.estimateComparison(
                            item.id
                        )
                    )
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
                onBackClick = {
                    navController
                        .popBackStack()
                },
                onShopDetailClick = {
                        shopId ->
                    navController.navigate(
                        Routes.shopDetail(
                            shopId
                        )
                    )
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
                            inclusive = false
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
            MyInfoScreen(
                onBackClick = {
                    navController
                        .popBackStack()
                },
                onEditProfileClick = {
                    navController.navigate(
                        Routes.EDIT_PROFILE
                    )
                }
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

            var designs by remember {
                mutableStateOf<
                        List<FavoriteDesignItem>
                        >(
                    emptyList()
                )
            }

            var isLoading by remember {
                mutableStateOf(true)
            }

            var isLoadingMore by remember {
                mutableStateOf(false)
            }

            var errorMessage by remember {
                mutableStateOf<String?>(
                    null
                )
            }

            var nextCursor by remember {
                mutableStateOf<String?>(
                    null
                )
            }

            var hasNext by remember {
                mutableStateOf(false)
            }

            var removingDesignIds by remember {
                mutableStateOf<Set<Long>>(
                    emptySet()
                )
            }

            fun loadBookmarks(
                isFirstLoad: Boolean
            ) {
                if (
                    isLoadingMore ||
                    (!isFirstLoad && !hasNext)
                ) {
                    return
                }

                if (isFirstLoad) {
                    isLoading = true
                    errorMessage = null
                    nextCursor = null
                    hasNext = false
                } else {
                    isLoadingMore = true
                }

                coroutineScope.launch {

                    bookmarkRepository
                        .getBookmarks(
                            cursor =
                                if (
                                    isFirstLoad
                                ) {
                                    null
                                } else {
                                    nextCursor
                                },
                            size = 10
                        )
                        .onSuccess {
                                response ->

                            val newDesigns =
                                response
                                    .designs
                                    .map {
                                            design ->

                                        FavoriteDesignItem(
                                            designId =
                                                design.designId,
                                            imageUrl =
                                                design.imageUrl,
                                            designName =
                                                design.title,
                                            tags =
                                                design.tags,
                                            viewCount =
                                                design.viewCount,
                                            wishCount =
                                                design.wishCount
                                        )
                                    }

                            designs =
                                if (
                                    isFirstLoad
                                ) {
                                    newDesigns
                                } else {
                                    (
                                            designs +
                                                    newDesigns
                                            )
                                        .distinctBy {
                                            it.designId
                                        }
                                }

                            nextCursor =
                                response
                                    .pageInfo
                                    .nextCursor

                            hasNext =
                                response
                                    .pageInfo
                                    .hasNext

                            isLoading =
                                false

                            isLoadingMore =
                                false
                        }
                        .onFailure {
                                exception ->

                            if (
                                isFirstLoad
                            ) {
                                errorMessage =
                                    exception.message
                                        ?: "찜 목록을 불러오지 못했습니다."
                            }

                            isLoading =
                                false

                            isLoadingMore =
                                false
                        }
                }
            }

            LaunchedEffect(
                Unit
            ) {
                loadBookmarks(
                    isFirstLoad =
                        true
                )
            }

            FavoriteDesignScreen(
                designs =
                    designs,
                isLoading =
                    isLoading,
                isLoadingMore =
                    isLoadingMore,
                hasNext =
                    hasNext,
                errorMessage =
                    errorMessage,
                removingDesignIds =
                    removingDesignIds,
                onBackClick = {
                    navController
                        .popBackStack()
                },
                onRetryClick = {
                    loadBookmarks(
                        isFirstLoad =
                            true
                    )
                },
                onLoadMore = {
                    loadBookmarks(
                        isFirstLoad =
                            false
                    )
                },
                onDesignClick = {
                        designId ->

                    // TODO:
                    // 디자인 상세 화면 연동 시 사용
                },
                onRemoveWishClick = {
                        designId ->

                    if (
                        !removingDesignIds
                            .contains(
                                designId
                            )
                    ) {
                        removingDesignIds =
                            removingDesignIds +
                                    designId

                        coroutineScope.launch {

                            bookmarkRepository
                                .removeDesignWish(
                                    designId =
                                        designId
                                )
                                .onSuccess {
                                        response ->

                                    if (
                                        !response
                                            .isBookmarked
                                    ) {
                                        designs =
                                            designs
                                                .filterNot {
                                                    it.designId ==
                                                            designId
                                                }
                                    } else {
                                        designs =
                                            designs
                                                .map {
                                                        design ->

                                                    if (
                                                        design.designId ==
                                                        designId
                                                    ) {
                                                        design.copy(
                                                            wishCount =
                                                                response
                                                                    .wishCount
                                                        )
                                                    } else {
                                                        design
                                                    }
                                                }
                                    }
                                }
                                .onFailure {
                                        exception ->

                                    Toast.makeText(
                                        context,
                                        exception.message
                                            ?: "찜 해제에 실패했습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            removingDesignIds =
                                removingDesignIds -
                                        designId
                        }
                    }
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