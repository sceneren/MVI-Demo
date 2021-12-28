package wiki.scene.mvvm.net

import retrofit2.http.*
import wiki.scene.mvvm.bean.User
import wiki.scene.mvvm.bean.WxArticleBean
import wiki.scene.entity.base.ApiResponse
import wiki.scene.entity.wanandroid.ApiPageListResponse
import wiki.scene.entity.wanandroid.ArticleInfo
import wiki.scene.entity.wanandroid.BannerInfo

interface ApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
    }

    @GET("wxarticle/chapters/json")
    suspend fun getWxArticle(): ApiResponse<List<WxArticleBean>>

    @GET("abc/chapters/json")
    suspend fun getWxArticleError(): ApiResponse<List<WxArticleBean>>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") userName: String,
        @Field("password") passWord: String
    ): ApiResponse<User?>

    @GET("user/logout/json")
    suspend fun logout(): ApiResponse<Any?>

    @GET("article/list/{page}/json")
    suspend fun articleList(@Path("page") page: Int): ApiResponse<ApiPageListResponse<ArticleInfo>>

    @GET("banner/json")
    suspend fun banner(): ApiResponse<MutableList<BannerInfo>>

    @Headers("X-CMC_PRO_API_KEY:66a331f9-9f8c-43cb-b995-a4694a174494")
    @GET("v1/cryptocurrency/listings/latest")
    suspend fun cryptocurrencyListings(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 100,
        @Query("convert") convert: String = "USD"
    ): ApiResponse<Any?>
}