package com.example.nailnaeil.ui.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.nailnaeil.R
import com.example.nailnaeil.data.remote.dto.DesignSummary
import com.example.nailnaeil.data.remote.dto.EstimateListItem
import com.example.nailnaeil.navigation.MainTabRoutes
import com.example.nailnaeil.ui.main.estimate.EstimateListScreen
import com.example.nailnaeil.ui.main.home.MainHomeScreen
import com.example.nailnaeil.ui.main.my.MyPageScreen
import com.example.nailnaeil.ui.main.reservation.ReservationListScreen
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextDisabled
import com.example.nailnaeil.ui.theme.TextMain

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
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    onAddressClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onNeedUpgrade: () -> Unit,
    onStartEstimate: () -> Unit,
    onMagazineClick: (DesignSummary) -> Unit,
    onEstimateClick: (EstimateListItem) -> Unit,
    onSeeAllEstimatesClick: () -> Unit = {},
    onEstimateItemClick: (EstimateListItem) -> Unit,
    onReservationItemClick: (String) -> Unit,
    onEditProfileClick: () -> Unit,
    onMyInfoClick: () -> Unit,
    onFavoriteDesignClick: () -> Unit,
    onFavoriteShopClick: () -> Unit,
    onNotificationSettingClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onTermsPolicyClick: () -> Unit,
    onAdminUnlocked: () -> Unit = {}
) {
    Scaffold(
        containerColor = SurfaceWhite,
        bottomBar = {
            NavigationBar(containerColor = SurfaceWhite) {
                mainTabs.forEach { tab ->
                    val selected = selectedTab == tab.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onTabSelected(tab.route) },
                        icon = {
                            Icon(
                                painter = painterResource(id = if (selected) tab.selectedIcon else tab.icon),
                                contentDescription = tab.label
                            )
                        },
                        label = { Text(tab.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TextMain,
                            selectedTextColor = TextMain,
                            unselectedIconColor = TextDisabled,
                            unselectedTextColor = TextDisabled,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (selectedTab) {
                MainTabRoutes.HOME -> MainHomeScreen(
                    onAddressClick = onAddressClick,
                    onNotificationClick = onNotificationClick,
                    onNeedUpgrade = onNeedUpgrade,
                    onStartEstimate = onStartEstimate,
                    onMagazineClick = onMagazineClick,
                    onEstimateClick = onEstimateClick,
                    onSeeAllEstimatesClick = onSeeAllEstimatesClick
                )
                MainTabRoutes.ESTIMATE_LIST -> EstimateListScreen(onItemClick = onEstimateItemClick)
                MainTabRoutes.RESERVATION -> ReservationListScreen(
                    onReservationClick = onReservationItemClick,
                    onStartEstimate = onStartEstimate
                )
                MainTabRoutes.MY -> MyPageScreen(
                    onEditProfileClick = onEditProfileClick,
                    onMyInfoClick = onMyInfoClick,
                    onFavoriteDesignClick = onFavoriteDesignClick,
                    onFavoriteShopClick = onFavoriteShopClick,
                    onNotificationSettingClick = onNotificationSettingClick,
                    onNoticeClick = onNoticeClick,
                    onTermsPolicyClick = onTermsPolicyClick,
                    onNeedUpgrade = onNeedUpgrade,
                    onInProgressEstimatesClick = { onTabSelected(MainTabRoutes.ESTIMATE_LIST) },
                    onUpcomingReservationsClick = { onTabSelected(MainTabRoutes.RESERVATION) },
                    onAdminUnlocked = onAdminUnlocked
                )
            }
        }
    }
}
