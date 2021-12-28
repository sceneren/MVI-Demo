package wiki.scene.mvvm.ui.fragment

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hjq.bar.TitleBar
import wiki.scene.common.router.ARouterConfig
import wiki.scene.entity.wanandroid.ArticleInfo
import wiki.scene.frame.base.fragment.BaseRecyclerViewFragment
import wiki.scene.frame.databinding.BaseTitleBarViewBinding
import wiki.scene.frame.ktx.launchAndCollect
import wiki.scene.mvvm.R
import wiki.scene.mvvm.adapter.ListAdapter
import wiki.scene.mvvm.databinding.FragmentTab3Binding
import wiki.scene.mvvm.vm.tab1.TypeViewModel

@Route(path = ARouterConfig.App.FRAG_TAB_3)
class Tab3Fragment : BaseRecyclerViewFragment<ArticleInfo>(R.layout.fragment_tab_3) {
    private val mBinding by viewBinding(FragmentTab3Binding::bind)
    private val mViewModel by viewModels<TypeViewModel>()

    private val mAdapter by lazy { ListAdapter() }

    override fun injectRequestFirstPage(): Int {
        return 0
    }

    override fun injectTitleBarViewBinding(): BaseTitleBarViewBinding {
        return getTitleBarViewBinding(mBinding)
    }

    override fun initTitleBarView(titleBar: TitleBar) {
        titleBar.title = "Tab2"
    }

    override fun injectRecyclerView(): RecyclerView {
        return mBinding.recyclerview
    }

    override fun injectLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(mContext)
    }

    override fun injectAdapter(): BaseQuickAdapter<ArticleInfo, out BaseViewHolder> {
        return mAdapter
    }

    override fun getListData(isFirst: Boolean, loadPage: Int) {
        launchAndCollect({
            loadListDataStart(isFirst)
            mViewModel.articleList(loadPage)
        }) {
            onSuccess = { result ->
                result?.let {
                    loadListDataSuccess(isFirst, it.curPage, it.pageCount, it.datas)
                }
            }

            onFailed = { errorCode, errorMsg ->
                loadListDataFail(isFirst, loadPage)
            }
        }

    }
}