package wiki.scene.frame.base.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.module.LoadMoreModule
import com.fondesa.recyclerviewdivider.BaseDividerItemDecoration
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import wiki.scene.frame.R
import wiki.scene.frame.base.IRecyclerViewUIView

/**
 *
 * @Description:    列表的快速实现
 * @Author:         scene
 * @CreateDate:     2021/11/10 10:22
 * @UpdateUser:
 * @UpdateDate:     2021/11/10 10:22
 * @UpdateRemark:
 * @Version:        1.0.0
 */
abstract class BaseRecyclerViewFragment<T>(@LayoutRes contentLayoutId: Int) :
    BaseFragment(contentLayoutId),
    IRecyclerViewUIView<T> {
    //当前显示的页数
    private var currentPageNo = 0

    override fun injectRequestFirstPage(): Int {
        return 1
    }

    override fun injectReturnFirstPage(): Int {
        return 1
    }

    override fun injectPageSize(): Int {
        return 20
    }

    override fun injectRefreshLayout(): SmartRefreshLayout? {
        return null
    }

    override fun injectDivider(): BaseDividerItemDecoration? {
        return null
    }

    override fun injectEmptyView(): Int {
        return R.layout.base_empty_view
    }

    override fun injectHeaderView(): Int {
        return 0
    }

    override fun initHeaderView(headerView: View) {

    }

    override fun injectHeaderWithEmptyEnable(): Boolean {
        return true
    }

    override fun enableLoadMore(): Boolean {
        //通过判断是否实现了接口来实现，使用时只需要在Adapter实现LoadMoreModule接口就行
        return LoadMoreModule::class.java.isAssignableFrom(injectAdapter()::class.java)
    }

    override fun initView() {
        super.initView()
        injectRefreshLayout()?.setEnableLoadMore(false)
        injectRefreshLayout()?.setOnRefreshListener(this)
        injectRecyclerView().layoutManager = injectLayoutManager()
        injectDivider()?.addTo(injectRecyclerView())
        injectRecyclerView().adapter = injectAdapter()
        injectAdapter().setEmptyView(injectEmptyView())
        injectAdapter().headerWithEmptyEnable = injectHeaderWithEmptyEnable()
        injectAdapter().isUseEmpty = false
        if (injectHeaderView() != 0) {
            val headerView = LayoutInflater.from(mContext)
                .inflate(injectHeaderView(), injectRecyclerView().parent as ViewGroup, false)
            initHeaderView(headerView)
            injectAdapter().addHeaderView(headerView)
        }
        if (enableLoadMore()) {
            injectAdapter().loadMoreModule.preLoadNumber = 3
            injectAdapter().loadMoreModule.setOnLoadMoreListener(this)
        }

        initRecyclerView()

    }

    override fun initRecyclerView() {

    }

    override fun loadData() {
        super.loadData()
        getListData(true, injectRequestFirstPage())
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        getListData(false, injectRequestFirstPage())
    }

    override fun onLoadMore() {
        getListData(false, currentPageNo + 1)
    }

    override fun injectLoadServiceView(): View? {
        return if (injectRefreshLayout() == null) {
            injectRecyclerView()
        } else {
            injectRefreshLayout()
        }
    }

    override fun loadListDataStart(isFirst: Boolean) {
        if (isFirst) {
            showLoadingPage()
        }
    }

    override fun loadListDataSuccess(
        isFirst: Boolean,
        currentPage: Int,
        totalPage: Int,
        list: MutableList<T>
    ) {
        //给标记的页数赋值
        currentPageNo = if (injectRequestFirstPage() == injectReturnFirstPage()) {
            currentPage
        } else {
            //特殊处理请求页数和返回页数不一致的情况，比如wanAndroid Api
            currentPage - (injectReturnFirstPage() - injectRequestFirstPage())
        }
        val hasMore = currentPage < totalPage
        if (isFirst) {
            showSuccessPage()
        } else {
            injectRefreshLayout()?.finishRefresh(true)
        }

        if (currentPage == injectReturnFirstPage()) {
            injectAdapter().setNewInstance(list)
        } else {
            injectAdapter().addData(list)
        }

        if (hasMore) {
            injectAdapter().loadMoreModule.loadMoreComplete()
        } else {
            injectAdapter().loadMoreModule.loadMoreEnd()
        }
        injectAdapter().isUseEmpty = true
    }

    override fun loadListDataSuccess(isFirst: Boolean, list: MutableList<T>) {
        if (isFirst) {
            showSuccessPage()
        } else {
            injectRefreshLayout()?.finishRefresh(true)
        }
        injectAdapter().setNewInstance(list)
        injectAdapter().isUseEmpty = true
    }

    override fun loadListDataFail(isFirst: Boolean, loadPage: Int) {
        if (isFirst) {
            showErrorPage()
        } else {
            if (loadPage == injectRequestFirstPage()) {
                injectRefreshLayout()?.finishRefresh(false)
            } else {
                injectAdapter().loadMoreModule.loadMoreFail()
            }
        }
    }

    override fun loadListDataFail(isFirst: Boolean) {
        if (isFirst) {
            showErrorPage()
        } else {
            injectRefreshLayout()?.finishRefresh(false)
        }
    }

    override fun onRetryBtnClick() {
        super.onRetryBtnClick()
        loadData()
    }
}