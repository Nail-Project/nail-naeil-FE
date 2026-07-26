package com.example.nailnail.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnail.ui.theme.DividerGray
import com.example.nailnail.ui.theme.MutedRosePrimary
import com.example.nailnail.ui.theme.TextSecondary

@Composable
fun KakaoConsentScreen(onAgreeClick: () -> Unit) {
    var agreeTerms by remember { mutableStateOf(false) }
    var agreePrivacy by remember { mutableStateOf(false) }
    var agreeMarketing by remember { mutableStateOf(false) }
    val allRequiredAgreed by remember { derivedStateOf { agreeTerms && agreePrivacy } }
    val allAgreed by remember { derivedStateOf { agreeTerms && agreePrivacy && agreeMarketing } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text(
            text = "카카오 계정으로\n간편하게 시작해요",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .clickable {
                    val next = !allAgreed
                    agreeTerms = next
                    agreePrivacy = next
                    agreeMarketing = next
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = allAgreed,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(checkedColor = MutedRosePrimary)
            )
            Text(
                text = "전체 동의하기",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        HorizontalDivider(color = DividerGray, modifier = Modifier.padding(vertical = 16.dp))

        ConsentRow(label = "[필수] 이용약관 동의", checked = agreeTerms, onCheckedChange = { agreeTerms = it })
        ConsentRow(label = "[필수] 개인정보 수집 및 이용 동의", checked = agreePrivacy, onCheckedChange = { agreePrivacy = it })
        ConsentRow(label = "[선택] 마케팅 정보 수신 동의", checked = agreeMarketing, onCheckedChange = { agreeMarketing = it })

        Button(
            onClick = onAgreeClick,
            enabled = allRequiredAgreed,
            colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .height(52.dp)
        ) {
            Text("동의하고 계속하기", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ConsentRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = MutedRosePrimary)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
