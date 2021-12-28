package wiki.scene.entity.wanandroid

data class Tab2ResultInfo(
    val bannerList: MutableList<BannerInfo>?,
    val articleListInfo: ApiPageListResponse<ArticleInfo>?
)