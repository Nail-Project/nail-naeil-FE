package com.example.nailnail.ui.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nailnail.R
import com.example.nailnail.navigation.MainTabRoutes
import com.example.nailnail.ui.main.estimate.EstimateItem
import com.example.nailnail.ui.main.estimate.EstimateListScreen
import com.example.nailnail.ui.main.home.EstimateSummary
import com.example.nailnail.ui.main.home.MainHomeScreen
import com.example.nailnail.ui.main.my.MyPageScreen
import com.example.nailnail.ui.main.reservation.ReservationListScreen

private data class MainTab(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int
)

private val mainTabs = listOf(
    MainTab(MainTabRoutes.HOME, "홈", R.drawable.ic_tab_home, R.drawable.ic_tab_home_selected),
    MainTab(MainTabRoutes.ESTIMATE_LIST, "견적함", R.drawable.ic_tab_list, R.drawable.ic_tab_list_selected),
    MainTab(MainTabRoutes.RESERVATION, "예약", R.drawable.ic_tab_reservation, R.drawable.ic_tab_reservation_selected),
    MainTab(MainTabRoutes.MY, "마이", R.drawable.ic_tab_my, R.drawable.ic_tab_my_selected)
)

@Composable
fun MainScaffold(
    onAddressClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onNeedUpgrade: () -> Unit,
    onStartEstimate: () -> Unit,
    onMagazineClick: (String) -> Unit,
    onEstimateClick: (EstimateSummary) -> Unit,
    onEstimateItemClick: (EstimateItem) -> Unit,
    onReservationItemClick: (String) -> Unit,
    onEditProfileClick: () -> Unit,
    onMyInfoClick: () -> Unit,
    onFavoriteDesignClick: () -> Unit,
    onFavoriteShopClick: () -> Unit,
    onNotificationSettingClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onTermsPolicyClick: () -> Unit
) {
    val tabNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            val backStackEntry by tabNavController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            NavigationBar {
                mainTabs.forEach { tab ->
                    val selected = currentRoute == tab.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            tabNavController.navigate(tab.route) {
                                popUpTo(tabNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = if (selected) tab.selectedIcon else tab.icon),
                                contentDescription = tab.label
                            )
                        },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = tabNavController,
            startDestination = MainTabRoutes.HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(MainTabRoutes.HOME) {
                MainHomeScreen(
                    onAddressClick = onAddressClick,
                    onNotificationClick = onNotificationClick,
                    onNeedUpgrade = onNeedUpgrade,
                    onStartEstimate = onStartEstimate,
                    onMagazineClick = onMagazineClick,
                    onEstimateClick = onEstimateClick
                )
            }
            composable(MainTabRoutes.ESTIMATE_LIST) {
                EstimateListScreen(onItemClick = onEstimateItemClick)
            }
            composable(MainTabRoutes.RESERVATION) {
                ReservationListScreen(
                    onReservationClick = onReservationItemClick,
                    onStartEstimate = onStartEstimate
                )
            }
            composable(MainTabRoutes.MY) {
                MyPageScreen(
                    onEditProfileClick = onEditProfileClick,
                    onMyInfoClick = onMyInfoClick,
                    onFavoriteDesignClick = onFavoriteDesignClick,
                    onFavoriteShopClick = onFavoriteShopClick,
                    onNotificationSettingClick = onNotificationSettingClick,
                    onNoticeClick = onNoticeClick,
                    onTermsPolicyClick = onTermsPolicyClick
                )
            }
        }
    }
}
