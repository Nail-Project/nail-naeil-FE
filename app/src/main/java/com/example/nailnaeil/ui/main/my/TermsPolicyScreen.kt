package com.example.nailnaeil.ui.main.my

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.ui.theme.TextSecondary

private data class TermsArticle(val heading: String, val body: String)

private data class TermsChapter(val title: String, val articles: List<TermsArticle>)

private val termsChapters = listOf(
    TermsChapter(
        title = "제 1장 총칙",
        articles = listOf(
            TermsArticle(
                "제 1조 (목적)",
                "이 약관은 네일내일(이하 \"회사\")이 제공하는 네일샵 견적·예약 중개 서비스(이하 \"서비스\")의 이용과 관련하여 " +
                    "회사와 회원 간의 권리, 의무 및 책임사항, 기타 필요한 사항을 규정함을 목적으로 합니다."
            ),
            TermsArticle(
                "제 2조 (정의)",
                "1. \"서비스\"란 회원이 원하는 네일 디자인과 조건을 등록하면 인근 네일샵으로부터 견적을 받아 비교하고, " +
                    "이를 바탕으로 예약을 진행할 수 있도록 회사가 제공하는 일체의 서비스를 말합니다.\n" +
                    "2. \"회원\"이란 이 약관에 동의하고 회사와 이용계약을 체결한 자를 말합니다.\n" +
                    "3. \"파트너 매장\"이란 서비스를 통해 견적 및 예약을 받는 네일샵을 말합니다."
            ),
            TermsArticle(
                "제 3조 (약관의 효력 및 변경)",
                "1. 이 약관은 서비스 화면에 게시하거나 기타의 방법으로 회원에게 공지함으로써 효력이 발생합니다.\n" +
                    "2. 회사는 관련 법령을 위배하지 않는 범위에서 이 약관을 개정할 수 있으며, 개정 시 적용일자 및 개정 사유를 " +
                    "명시하여 최소 7일 전부터 공지합니다."
            )
        )
    ),
    TermsChapter(
        title = "제 2장 이용계약의 체결",
        articles = listOf(
            TermsArticle(
                "제 4조 (이용신청)",
                "회원가입은 서비스가 정한 절차에 따라 신청자가 약관 내용에 동의하고, 회사가 정한 가입 양식에 따라 " +
                    "회원정보를 기입하여 신청합니다."
            ),
            TermsArticle(
                "제 5조 (이용신청의 승낙)",
                "회사는 다음 각 호에 해당하는 경우 이용신청을 승낙하지 않거나 사후에 이용계약을 해지할 수 있습니다.\n" +
                    "1. 타인의 명의를 이용하여 신청한 경우\n" +
                    "2. 허위의 정보를 기재하거나 회사가 요청하는 정보를 제공하지 않은 경우\n" +
                    "3. 기타 회원으로 등록하는 것이 서비스 운영에 현저히 지장이 있다고 판단되는 경우"
            )
        )
    ),
    TermsChapter(
        title = "제 3장 서비스의 이용",
        articles = listOf(
            TermsArticle(
                "제 6조 (서비스의 제공)",
                "회사는 회원에게 아래와 같은 서비스를 제공합니다.\n" +
                    "1. 네일 디자인 탐색 및 찜하기 서비스\n" +
                    "2. 견적 요청 및 파트너 매장의 견적 비교 서비스\n" +
                    "3. 예약 생성, 조회 및 취소 서비스\n" +
                    "4. 이용 후기(리뷰) 작성 서비스\n" +
                    "5. 그 밖에 회사가 추가로 개발하거나 제휴를 통해 제공하는 서비스"
            ),
            TermsArticle(
                "제 7조 (견적 및 예약)",
                "1. 회원이 등록한 견적 요청은 파트너 매장에 전달되며, 매장의 응답 여부 및 내용은 매장의 사정에 따라 " +
                    "달라질 수 있습니다.\n" +
                    "2. 회원은 받은 견적 중 하나를 선택하여 예약을 진행할 수 있으며, 예약 확정 이후의 시술은 " +
                    "해당 파트너 매장과 회원 간의 direct 계약에 따릅니다.\n" +
                    "3. 회사는 견적 및 예약의 중개자로서 시술 결과에 대해 직접적인 책임을 지지 않으며, " +
                    "매장 정보를 정확히 제공하기 위해 노력합니다."
            ),
            TermsArticle(
                "제 8조 (예약의 취소)",
                "회원은 예약 확정 이후에도 시술 전까지 서비스 내에서 예약을 취소할 수 있습니다. 다만 매장의 " +
                    "정책에 따라 잦은 취소는 이용에 제한이 있을 수 있습니다."
            )
        )
    ),
    TermsChapter(
        title = "제 4장 계약해지 및 이용제한",
        articles = listOf(
            TermsArticle(
                "제 9조 (회원 탈퇴)",
                "회원은 언제든지 마이페이지를 통해 이용계약 해지(회원 탈퇴)를 신청할 수 있으며, 회사는 관련 법령이 " +
                    "정하는 바에 따라 이를 즉시 처리합니다."
            ),
            TermsArticle(
                "제 10조 (이용제한)",
                "회사는 회원이 이 약관을 위반하거나 서비스의 정상적인 운영을 방해한 경우, 사전 통지 후 " +
                    "서비스 이용을 제한하거나 이용계약을 해지할 수 있습니다."
            )
        )
    ),
    TermsChapter(
        title = "제 5장 기타",
        articles = listOf(
            TermsArticle(
                "제 11조 (면책조항)",
                "회사는 천재지변 또는 이에 준하는 불가항력으로 인하여 서비스를 제공할 수 없는 경우 책임이 면제됩니다."
            ),
            TermsArticle(
                "제 12조 (분쟁의 해결)",
                "이 약관과 관련하여 발생한 분쟁에 대해서는 대한민국 법을 적용하며, 관할 법원은 민사소송법에 따른 " +
                    "관할법원으로 합니다."
            )
        )
    )
)

private const val EFFECTIVE_DATE_NOTICE = "본 약관은 2026년 8월 1일부터 적용됩니다."

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsPolicyScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("이용약관") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(text = "네일내일 이용약관", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                text = EFFECTIVE_DATE_NOTICE,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )

            termsChapters.forEach { chapter ->
                Text(
                    text = chapter.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 32.dp)
                )
                chapter.articles.forEach { article ->
                    Text(
                        text = article.heading,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    Text(
                        text = article.body,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
