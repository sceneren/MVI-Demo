package wiki.scene.entity.wanandroid

data class ApiPageListResponse<T>(
    val curPage: Int = 0,
    val pageCount: Int = 0,
    val datas: MutableList<T> = mutableListOf()
)