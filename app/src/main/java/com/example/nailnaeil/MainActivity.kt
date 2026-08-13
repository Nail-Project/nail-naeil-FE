package com.example.nailnaeil

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.nailnaeil.di.AppContainer
import com.example.nailnaeil.navigation.NailNailNavGraph
import com.example.nailnaeil.navigation.Routes
import com.example.nailnaeil.ui.theme.NailNailTheme

class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startedFromAuthDeepLink = handleAuthDeepLink(intent)

        setContent {
            NailNailTheme {
                val controller = rememberNavController()
                navController = controller
                NailNailNavGraph(
                    navController = controller,
                    startDestination = if (startedFromAuthDeepLink) Routes.MAIN else Routes.SPLASH
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (handleAuthDeepLink(intent)) {
            navController?.navigate(Routes.MAIN) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
        }
    }

    /**
     * 소셜 로그인 콜백 딥링크(nailnaeil://auth/callback?accessToken=...&refreshToken=...)를 처리한다.
     * 토큰을 저장하고, 이 딥링크로 진입한 것이면 true를 반환한다.
     */
    private fun handleAuthDeepLink(intent: Intent?): Boolean {
        val uri: Uri = intent?.data ?: return false
        if (uri.scheme != "nailnaeil" || uri.host != "auth") return false
        val accessToken = uri.getQueryParameter("accessToken") ?: return false
        val refreshToken = uri.getQueryParameter("refreshToken")
        AppContainer.tokenStore.authToken = accessToken
        AppContainer.tokenStore.refreshToken = refreshToken
        return true
    }
}
