package com.example.nailnaeil.navigation

import android.content.Intent
import android.net.Uri
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
import com.example.nailnaeil.BuildConfig
import com.example.nailnaeil.debug.UiPreviewScreen
import com.example.nailnaeil.di.AppContainer
import com.example.nailnaeil.ui.main.MainScaffold
import com.example.nailnaeil.ui.main.admin.AdminDemoListScreen
import com.example.nailnaeil.ui.main.admin.AdminHomeScreen
import com.example.nailnaeil.ui.main.admin.AdminMagazineDetailScreen
import com.example.nailnaeil.ui.main.admin.AdminMagazineListScreen
import com.example.nailnaeil.ui.main.admin.AdminSettingsScreen
import com.example.nailnaeil.ui.main.admin.AdminShopDetailScreen
import com.example.nailnaeil.ui.main.admin.AdminShopListScreen
import com.example.nailnaeil.ui.main.address.AddressEditScreen
import com.example.nailnaeil.ui.main.address.AddressFormScreen
import com.example.nailnaeil.ui.main.address.AddressSettingsScreen
import com.example.nailnaeil.ui.main.design.DesignDetailScreen
import com.example.nailnaeil.ui.main.design.RecentProposalsScreen
import com.example.nailnaeil.ui.main.design.SimilarDesignsScreen
import com.example.nailnaeil.ui.main.estimate.EstimateComparisonScreen
import com.example.nailnaeil.ui.main.estimate.ShopDetailScreen
import com.example.nailnaeil.ui.main.my.EditProfileScreen
import com.example.nailnaeil.ui.main.my.FavoriteDesignScreen
import com.example.nailnaeil.ui.main.my.FavoriteShopScreen
import com.example.nailnaeil.ui.main.my.MyInfoScreen
import com.example.nailnaeil.ui.main.my.NoticeScreen
import com.example.nailnaeil.ui.main.my.NotificationSettingScreen
import com.example.nailnaeil.ui.main.my.TermsPolicyScreen
import com.example.nailnaeil.ui.main.my.plan.PlanCompleteScreen
import com.example.nailnaeil.ui.main.my.plan.PlanPaymentScreen
import com.example.nailnaeil.ui.main.my.plan.PlanSelectionScreen
import com.example.nailnaeil.ui.main.reservation.ReservationCompleteScreen
import com.example.nailnaeil.ui.main.reservation.ReservationDetailScreen
import com.example.nailnaeil.ui.onboarding.KakaoConsentScreen
import com.example.nailnaeil.ui.onboarding.LoginScreen
import com.example.nailnaeil.ui.onboarding.PermissionScreen
import com.example.nailnaeil.ui.onboarding.SignupCompleteScreen
import com.example.nailnaeil.ui.onboarding.SplashScreen
import com.example.nailnaeil.ui.quote.QuoteFlow
import kotlinx.coroutines.launch

@Composable
fun NailNailNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.SPLASH
) {
    var mainSelectedTab by remember { mutableStateOf(MainTabRoutes.HOME) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // 로그인 성공 시점에 호출되는 훅. 실제 로그인 API 연동 시 TokenStore.authToken 저장 이후 이 함수를 호출할 것.
    fun onLoginSuccess() {
        coroutineScope.launch { AppContainer.deviceTokenRepository.registerCurrentDeviceToken() }
    }

    // 딥링크(nailnaeil://auth/callback)로 이미 토큰을 받은 채로 MAIN에 바로 진입한 경우도 기기 토큰을 등록한다.
    LaunchedEffect(startDestination) {
        if (startDestination == Routes.MAIN) onLoginSuccess()
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.UI_PREVIEW) {
            UiPreviewScreen()
        }

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
                onKakaoLoginClick = {
                    // 카카오 인가 페이지로 이동 → 동의 후 백엔드가 앱 딥링크로 리다이렉트할 예정이나,
                    // 그 콜백 스킴이 아직 정해지지 않아 앱이 가로채는 부분은 미구현 상태.
                    val url = "${BuildConfig.BASE_URL}api/v1/auth/kakao"
                    runCatching {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    }
                },
                onTestLoginSuccess = {
                    onLoginSuccess()
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
                    onLoginSuccess()
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
                onNeedUpgrade = { navController.navigate(Routes.PLAN_SELECTION) },
                onStartEstimate = { navController.navigate(Routes.QUOTE_FLOW) },
                onMagazineClick = { design -> navController.navigate(Routes.magazineDetail(design.designId.toString())) },
                onEstimateClick = { estimate -> navController.navigate(Routes.estimateComparison(estimate.estimateId.toString())) },
                onSeeAllEstimatesClick = { mainSelectedTab = MainTabRoutes.ESTIMATE_LIST },
                onEstimateItemClick = { item ->
                    navController.navigate(Routes.estimateComparison(item.estimateId.toString()))
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
                onTermsPolicyClick = { navController.navigate(Routes.TERMS_POLICY) },
                onAdminUnlocked = { navController.navigate(Routes.ADMIN_HOME) }
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
                onShopDetailClick = { proposalId -> navController.navigate(Routes.shopDetail(proposalId.toString())) }
            )
        }

        composable(Routes.MAGAZINE_DETAIL) { backStackEntry ->
            val designId = backStackEntry.arguments?.getString("magazineId")?.toLongOrNull() ?: 0L
            DesignDetailScreen(
                designId = designId,
                onBackClick = { navController.popBackStack() },
                onStartEstimate = { navController.navigate(Routes.QUOTE_FLOW) },
                onProposalClick = { proposalId -> navController.navigate(Routes.shopDetail(proposalId.toString())) },
                onSimilarDesignClick = { similarDesignId -> navController.navigate(Routes.magazineDetail(similarDesignId.toString())) },
                onSeeAllProposalsClick = { id -> navController.navigate(Routes.recentProposalsAll(id.toString())) },
                onSeeAllSimilarDesignsClick = { id -> navController.navigate(Routes.similarDesignsAll(id.toString())) }
            )
        }

        composable(Routes.SIMILAR_DESIGNS_ALL) { backStackEntry ->
            val designId = backStackEntry.arguments?.getString("designId")?.toLongOrNull() ?: 0L
            SimilarDesignsScreen(
                designId = designId,
                onBackClick = { navController.popBackStack() },
                onDesignClick = { clickedId -> navController.navigate(Routes.magazineDetail(clickedId.toString())) }
            )
        }

        composable(Routes.RECENT_PROPOSALS_ALL) { backStackEntry ->
            val designId = backStackEntry.arguments?.getString("designId")?.toLongOrNull() ?: 0L
            RecentProposalsScreen(
                designId = designId,
                onBackClick = { navController.popBackStack() },
                onRequestEstimate = { shopId -> navController.navigate(Routes.shopDetail(shopId.toString())) }
            )
        }

        composable(Routes.SHOP_DETAIL) { backStackEntry ->
            val shopId = backStackEntry.arguments?.getString("shopId").orEmpty()
            ShopDetailScreen(
                shopId = shopId,
                onBackClick = { navController.popBackStack() },
                onReservationConfirmed = { navController.navigate(Routes.RESERVATION_COMPLETE) }
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
                onEditProfileClick = { navController.navigate(Routes.EDIT_PROFILE) },
                onLoggedOut = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
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

        composable(Routes.ADMIN_HOME) {
            AdminHomeScreen(
                onBackClick = { navController.popBackStack() },
                onMagazineManageClick = { navController.navigate(Routes.ADMIN_MAGAZINE_LIST) },
                onShopManageClick = { navController.navigate(Routes.ADMIN_SHOP_LIST) },
                onDemoManageClick = { navController.navigate(Routes.ADMIN_DEMO_LIST) },
                onSettingsClick = { navController.navigate(Routes.ADMIN_SETTINGS) }
            )
        }

        composable(Routes.ADMIN_SETTINGS) {
            AdminSettingsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.ADMIN_DEMO_LIST) {
            AdminDemoListScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.ADMIN_SHOP_LIST) {
            AdminShopListScreen(
                onBackClick = { navController.popBackStack() },
                onShopClick = { shopId -> navController.navigate(Routes.adminShopDetail(shopId)) }
            )
        }

        composable(Routes.ADMIN_SHOP_DETAIL) { backStackEntry ->
            val shopId = backStackEntry.arguments?.getString("shopId")?.toLongOrNull() ?: 0L
            AdminShopDetailScreen(
                shopId = shopId,
                onBackClick = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Routes.ADMIN_MAGAZINE_LIST) {
            AdminMagazineListScreen(
                onBackClick = { navController.popBackStack() },
                onDesignClick = { designId -> navController.navigate(Routes.adminMagazineDetail(designId.toString())) },
                onAddClick = { navController.navigate(Routes.adminMagazineDetail(Routes.ADMIN_MAGAZINE_NEW_ID)) }
            )
        }

        composable(Routes.ADMIN_MAGAZINE_DETAIL) { backStackEntry ->
            val designIdArg = backStackEntry.arguments?.getString("designId") ?: Routes.ADMIN_MAGAZINE_NEW_ID
            AdminMagazineDetailScreen(
                designId = designIdArg.toLongOrNull(),
                onBackClick = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Routes.PLAN_SELECTION) {
            PlanSelectionScreen(
                onBackClick = { navController.popBackStack() },
                onSelectNPlus = { navController.navigate(Routes.PLAN_PAYMENT) }
            )
        }

        composable(Routes.PLAN_PAYMENT) {
            PlanPaymentScreen(
                onBackClick = { navController.popBackStack() },
                onConfirm = { navController.navigate(Routes.PLAN_COMPLETE) }
            )
        }

        composable(Routes.PLAN_COMPLETE) {
            PlanCompleteScreen(
                onBackClick = { navController.popBackStack() },
                onConfirm = { navController.popBackStack(Routes.MAIN, inclusive = false) }
            )
        }

        composable(Routes.ADDRESS_SETTINGS) {
            AddressSettingsScreen(
                onBackClick = { navController.popBackStack() },
                onEditClick = { navController.navigate(Routes.ADDRESS_EDIT) },
                onAddressSelected = { navController.popBackStack() },
                onAddNewClick = { navController.navigate(Routes.addressForm("new")) }
            )
        }

        composable(Routes.ADDRESS_EDIT) {
            AddressEditScreen(
                onBackClick = { navController.popBackStack() },
                onModifyClick = { address ->
                    navController.navigate(Routes.addressForm(address.addressId.toString()))
                },
                onAddClick = { navController.navigate(Routes.addressForm("new")) }
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
