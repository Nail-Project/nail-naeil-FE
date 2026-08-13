package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.DesignAdminCreateRequest
import com.example.nailnaeil.data.remote.dto.DesignAdminResponse
import com.example.nailnaeil.data.remote.dto.DesignAdminUpdateRequest
import com.example.nailnaeil.data.remote.dto.ShopAdmin
import com.example.nailnaeil.data.remote.dto.ShopAdminInput
import com.example.nailnaeil.data.remote.dto.ShopAdminListResponse
import com.example.nailnaeil.data.remote.dto.ShopSyncRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AdminApi {

    /** 소상공인 상가정보 API에서 지정한 업종 코드의 네일샵 데이터를 동기화한다. */
    @POST("admin/api/v1/shops/sync")
    suspend fun syncShops(
        @Header("x-admin-sync-key") adminSyncKey: String,
        @Body request: ShopSyncRequest
    ): Response<Unit>

    @GET("admin/api/v1/shops")
    suspend fun getShops(
        @Header("x-admin-sync-key") adminSyncKey: String,
        @Query("cursor") cursor: Long? = null,
        @Query("limit") limit: Int? = null,
        @Query("active") active: String? = null
    ): Response<ApiResponse<ShopAdminListResponse>>

    @POST("admin/api/v1/shops")
    suspend fun createShop(
        @Header("x-admin-sync-key") adminSyncKey: String,
        @Body request: ShopAdminInput
    ): Response<ApiResponse<ShopAdmin>>

    @GET("admin/api/v1/shops/{shopId}")
    suspend fun getShopDetail(
        @Header("x-admin-sync-key") adminSyncKey: String,
        @Path("shopId") shopId: Long
    ): Response<ApiResponse<ShopAdmin>>

    @PATCH("admin/api/v1/shops/{shopId}")
    suspend fun updateShop(
        @Header("x-admin-sync-key") adminSyncKey: String,
        @Path("shopId") shopId: Long,
        @Body request: ShopAdminInput
    ): Response<ApiResponse<ShopAdmin>>

    /** 레코드를 삭제하지 않고 isDataActive를 false로 변경한다(비활성화). */
    @DELETE("admin/api/v1/shops/{shopId}")
    suspend fun deactivateShop(
        @Header("x-admin-sync-key") adminSyncKey: String,
        @Path("shopId") shopId: Long
    ): Response<Unit>

    @POST("admin/api/v1/designs")
    suspend fun createDesign(
        @Header("x-admin-design-key") adminDesignKey: String,
        @Body request: DesignAdminCreateRequest
    ): Response<ApiResponse<DesignAdminResponse>>

    @PATCH("admin/api/v1/designs/{designId}")
    suspend fun updateDesign(
        @Header("x-admin-design-key") adminDesignKey: String,
        @Path("designId") designId: Long,
        @Body request: DesignAdminUpdateRequest
    ): Response<ApiResponse<DesignAdminResponse>>

    @DELETE("admin/api/v1/designs/{designId}")
    suspend fun deleteDesign(
        @Header("x-admin-design-key") adminDesignKey: String,
        @Path("designId") designId: Long
    ): Response<Unit>
}
