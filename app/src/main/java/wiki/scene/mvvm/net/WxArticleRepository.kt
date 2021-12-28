package wiki.scene.mvvm.net

import wiki.scene.mvvm.bean.User
import wiki.scene.mvvm.bean.WxArticleBean
import wiki.scene.entity.base.ApiResponse
import wiki.scene.entity.wanandroid.ApiPageListResponse
import wiki.scene.entity.wanandroid.ArticleInfo
import wiki.scene.entity.wanandroid.BannerInfo
import wiki.scene.entity.wanandroid.Tab2ResultInfo
import wiki.scene.network.base.BaseRepository


class WxArticleRepository : BaseRepository() {

    private val mService by lazy {
        RetrofitClient.service
    }

    suspend fun fetchWxArticleFromNet(): ApiResponse<List<WxArticleBean>> {
        return executeHttp {
            mService.getWxArticle()
        }
    }

    suspend fun fetchWxArticleError(): ApiResponse<List<WxArticleBean>> {
        return executeHttp {
            mService.getWxArticleError()
        }
    }

    suspend fun login(username: String, password: String): ApiResponse<User?> {
        return executeHttp {
            mService.login(username, password)
        }
    }

    suspend fun logout(): ApiResponse<Any?> {
        return executeHttp {
            mService.logout()
        }
    }

    suspend fun test(): ApiResponse<Any?> {
        return executeHttp {
            mService.cryptocurrencyListings()
        }
    }

    suspend fun articleList(page: Int): ApiResponse<ApiPageListResponse<ArticleInfo>> {
        return executeHttp {
            mService.articleList(page)
        }
    }

    suspend fun banner(): ApiResponse<MutableList<BannerInfo>> {
        return executeHttp {
            mService.banner()
        }
    }

}