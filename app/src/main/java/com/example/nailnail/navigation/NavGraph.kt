package com.example.nailnail.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nailnail.ui.main.MainScaffold
import com.example.nailnail.ui.main.address.AddressEditScreen
import com.example.nailnail.ui.main.address.AddressFormScreen
import com.example.nailnail.ui.main.address.AddressSettingsScreen
import com.example.nailnail.ui.main.estimate.EstimateComparisonScreen
import com.example.nailnail.ui.main.estimate.ShopDetailScreen
import com.example.nailnail.ui.main.my.EditProfileScreen
import com.example.nailnail.ui.main.my.FavoriteDesignScreen
import com.example.nailnail.ui.main.my.FavoriteShopScreen
import com.example.nailnail.ui.main.my.MyInfoScreen
import com.example.nailnail.ui.main.my.NoticeScreen
import com.example.nailnail.ui.main.my.NotificationSettingScreen
import com.example.nailnail.ui.main.my.TermsPolicyScreen
import com.example.nailnail.ui.main.reservation.ReservationCompleteScreen
import com.example.nailnail.ui.main.reservation.ReservationDetailScreen
import com.example.nailnail.ui.main.reservation.ReservationMockState
import com.example.nailnail.ui.onboarding.KakaoConsentScreen
import com.example.nailnail.ui.onboarding.LoginScreen
import com.example.nailnail.ui.onboarding.PermissionScreen
import com.example.nailnail.ui.onboarding.SignupCompleteScreen
import com.example.nailnail.ui.onboarding.SplashScreen
import com.example.nailnail.ui.quote.QuoteFlow

@Composable
fun NailNailNavGraph(navController: NavHostController = rememberNavController()) {
    var mainSelectedTab by remember { mutableStateOf(MainTabRoutes.HOME) }

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Routes.PERMISSION) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.PERMISSION) {
            PermissionScreen(onContinue = { navController.navigate(Routes.LOGIN) })
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onKakaoLoginClick = { navController.navigate(Routes.KAKAO_CONSENT) },
                onNaverLoginClick = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.PERMISSION) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.KAKAO_CONSENT) {
            KakaoConsentScreen(onAgreeClick = { navController.navigate(Routes.SIGNUP_COMPLETE) })
        }

        composable(Routes.SIGNUP_COMPLETE) {
            SignupCompleteScreen(
                onStartClick = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.PERMISSION) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.MAIN) {
            MainScaffold(
                selectedTab = mainSelectedTab,
                onTabSelected = { mainSelectedTab = it },
                onAddressClick = { navController.navigate(Routes.ADDRESS_SETTINGS) },
                onNotificationClick = {},
                onNeedUpgrade = {},
                onStartEstimate = { navController.navigate(Routes.QUOTE_FLOW) },
                onMagazineClick = {},
                onEstimateClick = {},
                onEstimateItemClick = { item ->
                    navController.navigate(Routes.estimateComparison(item.id))
                },
                onReservationItemClick = { reservationId ->
                    navController.navigate(Routes.reservationDetail(reservationId))
                },
                onEditProfileClick = { navController.navigate(Routes.EDIT_PROFILE) },
                onMyInfoClick = { navController.navigate(Routes.MY_INFO) },
                onFavoriteDesignClick = { navController.navigate(Routes.FAVORITE_DESIGN) },
                onFavoriteShopClick = { navController.navigate(Routes.FAVORITE_SHOP) },
                onNotificationSettingClick = { navController.navigate(Routes.NOTIFICATION_SETTING) },
                onNoticeClick = { navController.navigate(Routes.NOTICE) },
                onTermsPolicyClick = { navController.navigate(Routes.TERMS_POLICY) }
            )
        }

        composable(Routes.QUOTE_FLOW) {
            QuoteFlow(onFinish = { navController.popBackStack() })
        }

        composable(Routes.ESTIMATE_COMPARISON) { backStackEntry ->
            val estimateId = backStackEntry.arguments?.getString("estimateId").orEmpty()
            EstimateComparisonScreen(
                estimateId = estimateId,
                onBackClick = { navController.popBackStack() },
                onShopDetailClick = { shopId -> navController.navigate(Routes.shopDetail(shopId)) },
                onReserveClick = {}
            )
        }

        composable(Routes.SHOP_DETAIL) { backStackEntry ->
            val shopId = backStackEntry.arguments?.getString("shopId").orEmpty()
            ShopDetailScreen(
                shopId = shopId,
                onBackClick = { navController.popBackStack() },
                onReservationConfirmed = { reservation ->
                    ReservationMockState.addConfirmed(reservation)
                    navController.navigate(Routes.RESERVATION_COMPLETE)
                }
            )
        }

        composable(Routes.RESERVATION_COMPLETE) {
            ReservationCompleteScreen(
                onGoToReservations = {
                    mainSelectedTab = MainTabRoutes.RESERVATION
                    navController.popBackStack(Routes.MAIN, inclusive = false)
                }
            )
        }

        composable(Routes.RESERVATION_DETAIL) { backStackEntry ->
            val reservationId = backStackEntry.arguments?.getString("reservationId").orEmpty()
            ReservationDetailScreen(
                reservationId = reservationId,
                onBackClick = { navController.popBackStack() },
                onCancelled = { navController.popBackStack() }
            )
        }

        composable(Routes.MY_INFO) {
            MyInfoScreen(
                onBackClick = { navController.popBackStack() },
                onEditProfileClick = { navController.navigate(Routes.EDIT_PROFILE) }
            )
        }

        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.FAVORITE_DESIGN) {
            FavoriteDesignScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.FAVORITE_SHOP) {
            FavoriteShopScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.NOTIFICATION_SETTING) {
            NotificationSettingScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.NOTICE) {
            NoticeScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.TERMS_POLICY) {
            TermsPolicyScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.ADDRESS_SETTINGS) {
            AddressSettingsScreen(
                onBackClick = { navController.popBackStack() },
                onEditClick = { navController.navigate(Routes.ADDRESS_EDIT) },
                onAddressSelected = { navController.popBackStack() }
            )
        }

        composable(Routes.ADDRESS_EDIT) {
            AddressEditScreen(
                onBackClick = { navController.popBackStack() },
                onModifyClick = { address ->
                    navController.navigate(Routes.addressForm(address.id))
                }
            )
        }

        composable(Routes.ADDRESS_FORM) { backStackEntry ->
            val addressId = backStackEntry.arguments?.getString("addressId").orEmpty()
            AddressFormScreen(
                addressId = addressId,
                onBackClick = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }
    }
}
