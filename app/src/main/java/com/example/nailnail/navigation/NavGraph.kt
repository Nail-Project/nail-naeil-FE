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
import com.example.nailnail.ui.onboarding.SplashScreen

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
                onMagazineClick = {},
                onEstimateClick = {}
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
