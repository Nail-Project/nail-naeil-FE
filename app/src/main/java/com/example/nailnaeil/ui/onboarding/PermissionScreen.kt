package com.example.nailnaeil.ui.onboarding

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.PermissionBoxPink
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun PermissionScreen(onContinue: () -> Unit) {
    val requiredPermissions = buildList {
        add(Manifest.permission.ACCESS_FINE_LOCATION)
        add(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { onContinue() }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "네일내일 이용을 위해\n권한을 허용해주세요",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "허용하지 않아도 서비스는 이용할 수 있지만, 일부 기능이 제한될 수 있어요.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(top = 12.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(PermissionBoxPink)
                .padding(20.dp)
        ) {
            PermissionRow(
                icon = Icons.Filled.LocationOn,
                title = "위치 정보",
                description = "내 주변 네일샵을 찾아드릴 때 사용해요"
            )
            PermissionRow(
                icon = Icons.Filled.Notifications,
                title = "알림",
                description = "견적 응답, 예약 확정 소식을 알려드려요",
                modifier = Modifier.padding(top = 20.dp)
            )
        }

        Button(
            onClick = { permissionLauncher.launch(requiredPermissions.toTypedArray()) },
            colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .height(52.dp)
        ) {
            Text("허용하고 계속하기", color = SurfaceWhite, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PermissionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = title, tint = MutedRosePrimary)
        Column(modifier = Modifier.padding(start = 14.dp)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
