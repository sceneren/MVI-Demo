package wiki.scene.frame.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fondesa.recyclerviewdivider.BaseDividerItemDecoration
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

interface IRecyclerViewUIView<T> : OnRefreshListener, OnLoadMoreListener {
    /**
     * 加载第一页的page
     */
    fun injectRequestFirstPage(): Int

    /**
     * 返回的第一页的page
     */
    fun injectReturnFirstPage(): Int

    /**
     * 注入每页的加载数量
     */
    fun injectPageSize(): Int

    /**
     * 注入RecyclerView
     */
    fun injectRecyclerView(): RecyclerView

    /**
     * 注入SmartRefreshLayout
     */
    fun injectRefreshLayout(): SmartRefreshLayout?

    /**
     * 注入LayoutManager
     */
    fun injectLayoutManager(): RecyclerView.LayoutManager

    /**
     * 注入分割线
     */
    fun injectDivider(): BaseDividerItemDecoration?

    /**
     * 注入Adapter
     */
    fun injectAdapter(): BaseQuickAdapter<T, out BaseViewHolder>

    /**
     *注入空布局
     */
    fun injectEmptyView(): Int

    /**
     * 注入HeaderView
     */
    fun injectHeaderView(): Int

    /**
     * 初始化HeaderView
     */
    fun initHeaderView(headerView: View)

    /**
     * 注入是否有header时候显示空布局
     */
    fun injectHeaderWithEmptyEnable(): Boolean

    /**
     * 是否允许加载更多
     */
    fun enableLoadMore(): Boolean

    /**
     * 初始化RecyclerView
     */
    fun initRecyclerView()

    /**
     * 获取列表数据
     */
    fun getListData(isFirst: Boolean, loadPage: Int)

    /**
     * 开始加载
     */
    fun loadListDataStart(isFirst: Boolean)

    /**
     * 加载成功有分页
     */
    fun loadListDataSuccess(
        isFirst: Boolean,
        currentPage: Int,
        totalPage: Int,
        list: MutableList<T>
    )

    /**
     * 加载成没有分页
     */
    fun loadListDataSuccess(
        isFirst: Boolean,
        list: MutableList<T>
    )

    /**
     * 加载失败有分页
     */
    fun loadListDataFail(isFirst: Boolean, loadPage: Int)

    /**
     * 加载失败没有分页
     */
    fun loadListDataFail(isFirst: Boolean)

}