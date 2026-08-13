package com.example.nailnaeil.navigation

/** 최상위 NavHost 라우트 (온보딩 + 메인 진입 후 풀스크린으로 뜨는 화면들) */
object Routes {
    const val UI_PREVIEW = "ui_preview" // TODO: UI 리뷰 끝나면 이 줄과 관련 코드 제거
    const val SPLASH = "splash"
    const val PERMISSION = "permission"
    const val LOGIN = "login"
    const val KAKAO_CONSENT = "kakao_consent"
    const val SIGNUP_COMPLETE = "signup_complete"

    const val MAIN = "main"

    const val QUOTE_FLOW = "quote_flow"

    const val ESTIMATE_COMPARISON = "estimate_comparison/{estimateId}"
    fun estimateComparison(estimateId: String) = "estimate_comparison/$estimateId"

    const val SHOP_DETAIL = "shop_detail/{shopId}"
    fun shopDetail(shopId: String) = "shop_detail/$shopId"

    const val RESERVATION_DETAIL = "reservation_detail/{reservationId}"
    fun reservationDetail(reservationId: String) = "reservation_detail/$reservationId"

    const val RESERVATION_COMPLETE = "reservation_complete"

    const val MY_INFO = "my_info"
    const val EDIT_PROFILE = "edit_profile"
    const val FAVORITE_DESIGN = "favorite_design"
    const val FAVORITE_SHOP = "favorite_shop"
    const val NOTIFICATION_SETTING = "notification_setting"
    const val NOTICE = "notice"
    const val TERMS_POLICY = "terms_policy"

    const val MAGAZINE_DETAIL = "magazine_detail/{magazineId}"
    fun magazineDetail(magazineId: String) = "magazine_detail/$magazineId"

    const val SIMILAR_DESIGNS_ALL = "similar_designs_all/{designId}"
    fun similarDesignsAll(designId: String) = "similar_designs_all/$designId"

    const val RECENT_PROPOSALS_ALL = "recent_proposals_all/{designId}"
    fun recentProposalsAll(designId: String) = "recent_proposals_all/$designId"

    const val NOTIFICATION = "notification"

    const val PLAN_SELECTION = "plan_selection"
    const val PLAN_PAYMENT = "plan_payment"
    const val PLAN_COMPLETE = "plan_complete"

    const val ADDRESS_SETTINGS = "address_settings"
    const val ADDRESS_EDIT = "address_edit"
    const val ADDRESS_FORM = "address_form/{addressId}"
    fun addressForm(addressId: String) = "address_form/$addressId"

    const val ADMIN_HOME = "admin_home"
    const val ADMIN_SETTINGS = "admin_settings"
    const val ADMIN_SHOP_LIST = "admin_shop_list"
    const val ADMIN_SHOP_DETAIL = "admin_shop_detail/{shopId}"
    fun adminShopDetail(shopId: Long) = "admin_shop_detail/$shopId"
    const val ADMIN_MAGAZINE_LIST = "admin_magazine_list"
    const val ADMIN_MAGAZINE_DETAIL = "admin_magazine_detail/{designId}"
    fun adminMagazineDetail(designId: String) = "admin_magazine_detail/$designId"
    const val ADMIN_MAGAZINE_NEW_ID = "new"
}

/** MainScaffold 내부 하단 탭 NavHost 라우트 */
object MainTabRoutes {
    const val HOME = "tab_home"
    const val ESTIMATE_LIST = "tab_estimate_list"
    const val RESERVATION = "tab_reservation"
    const val MY = "tab_my"
}
