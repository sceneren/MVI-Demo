package wiki.scene.mvvm.vm.tab1

import wiki.scene.entity.base.ApiEmptyResponse
import wiki.scene.entity.base.ApiFailedResponse
import wiki.scene.entity.base.ApiResponse
import wiki.scene.entity.base.ApiSuccessResponse
import wiki.scene.entity.wanandroid.ApiPageListResponse
import wiki.scene.entity.wanandroid.ArticleInfo
import wiki.scene.entity.wanandroid.BannerInfo
import wiki.scene.entity.wanandroid.Tab2ResultInfo
import wiki.scene.frame.base.BaseViewModel
import wiki.scene.mvvm.net.WxArticleRepository

class TypeViewModel : BaseViewModel() {

    private val repository by lazy { WxArticleRepository() }

    suspend fun articleList(page: Int): ApiResponse<ApiPageListResponse<ArticleInfo>> {
        return repository.articleList(page)
    }

    suspend fun banner(): ApiResponse<MutableList<BannerInfo>> {
        return repository.banner()
    }

    suspend fun getTab2Data(page: Int): ApiResponse<Tab2ResultInfo> {
        val bannerListResultInfo = if (page == 0) {
            banner()
        } else {
            ApiEmptyResponse()
        }
        val articleListResultInfo = articleList(page)

        return if (bannerListResultInfo.isSuccess) {
            if (articleListResultInfo.isSuccess) {
                ApiSuccessResponse(
                    Tab2ResultInfo(
                        bannerListResultInfo.data,
                        articleListResultInfo.data
                    )
                )
            } else {
                ApiFailedResponse(
                    articleListResultInfo.errorCode,
                    articleListResultInfo.errorMsg
                )
            }
        } else {
            ApiFailedResponse(bannerListResultInfo.errorCode, bannerListResultInfo.errorMsg)
        }
    }
}