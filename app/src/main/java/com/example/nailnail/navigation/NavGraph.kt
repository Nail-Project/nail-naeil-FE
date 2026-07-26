package com.example.nailnail.navigation

import androidx.compose.runtime.Composable
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
import com.example.nailnail.ui.onboarding.SplashScreen
import com.example.nailnail.ui.quote.QuoteFlow

@Composable
fun NailNailNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.MAIN) {
            MainScaffold(
                onAddressClick = { navController.navigate(Routes.ADDRESS_SETTINGS) },
                onNotificationClick = {},
                onNeedUpgrade = {},
                onStartEstimate = { navController.navigate(Routes.QUOTE_FLOW) },
                onMagazineClick = {},
                onEstimateClick = {},
                onEstimateItemClick = { item ->
                    navController.navigate(Routes.estimateComparison(item.id))
                }
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
                onReserveClick = {}
            )
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
