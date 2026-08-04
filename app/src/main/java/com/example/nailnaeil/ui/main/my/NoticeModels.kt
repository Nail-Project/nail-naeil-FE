package com.example.nailnaeil.ui.main.my

data class NoticeItem(
    val title: String,
    val date: String,
    val isHighlighted: Boolean = false
)

val MockNotices = listOf(
    NoticeItem(title = "[공지] 네일내일 서비스 이용약관 개정 안내", date = "2026년 07월 01일", isHighlighted = true),
    NoticeItem(title = "[공지] 네일내일 서비스 이용약관 개정 안내", date = "2026년 07월 01일"),
    NoticeItem(title = "[공지] 네일내일 서비스 이용약관 개정 안내", date = "2026년 07월 01일"),
    NoticeItem(title = "[공지] 네일내일 서비스 이용약관 개정 안내", date = "2026년 07월 01일"),
    NoticeItem(title = "[공지] 네일내일 서비스 이용약관 개정 안내", date = "2026년 07월 01일"),
    NoticeItem(title = "[공지] 네일내일 서비스 이용약관 개정 안내", date = "2026년 07월 01일"),
    NoticeItem(title = "[공지] 네일내일 서비스 이용약관 개정 안내", date = "2026년 07월 01일"),
    NoticeItem(title = "[공지] 네일내일 서비스 이용약관 개정 안내", date = "2026년 07월 01일"),
    NoticeItem(title = "[공지] 네일내일 서비스 이용약관 개정 안내", date = "2026년 07월 01일")
)
