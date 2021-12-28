package wiki.scene.mvvm.vm

import wiki.scene.entity.base.ApiResponse
import wiki.scene.entity.wanandroid.ApiPageListResponse
import wiki.scene.entity.wanandroid.ArticleInfo
import wiki.scene.frame.base.BaseViewModel
import wiki.scene.mvvm.bean.User
import wiki.scene.mvvm.bean.WxArticleBean
import wiki.scene.mvvm.net.WxArticleRepository
import wiki.scene.network.observer.StateMutableLiveData

/**
 *
 * @Description:    ViewModel
 * @Author:         scene
 * @CreateDate:     2021/11/10 10:23
 * @UpdateUser:
 * @UpdateDate:     2021/11/10 10:23
 * @UpdateRemark:
 * @Version:        1.0.0
 */
class ApiViewModel : BaseViewModel() {

    private val repository by lazy { WxArticleRepository() }

    val wxArticleLiveData = StateMutableLiveData<List<WxArticleBean>>()

    val articleListLiveData = StateMutableLiveData<ApiPageListResponse<ArticleInfo>>()

    suspend fun requestNet() {
        wxArticleLiveData.value = repository.fetchWxArticleFromNet()
    }

    suspend fun requestNetError() {
        wxArticleLiveData.value = repository.fetchWxArticleError()
    }

    suspend fun login(username: String, password: String): ApiResponse<User?> {
        return repository.login(username, password)
    }

    suspend fun logout(): ApiResponse<Any?> {
        return repository.logout()
    }

    suspend fun test(): ApiResponse<Any?> {
        return repository.test()
    }

    suspend fun articleList(page: Int) {
        articleListLiveData.value = repository.articleList(page)
    }
}