package wiki.scene.mvi.ui.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hjq.bar.TitleBar
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.indicator.enums.IndicatorSlideMode
import wiki.scene.common.router.ARouterConfig
import wiki.scene.common.utils.toPx
import wiki.scene.entity.wanandroid.ArticleInfo
import wiki.scene.entity.wanandroid.BannerInfo
import wiki.scene.frame.base.fragment.BaseRecyclerViewFragment
import wiki.scene.frame.databinding.BaseTitleBarViewBinding
import wiki.scene.frame.ktx.launchAndCollect
import wiki.scene.mvi.R
import wiki.scene.mvi.adapter.BannerAdapter
import wiki.scene.mvi.adapter.ListAdapter
import wiki.scene.mvi.databinding.FragmentTab2Binding
import wiki.scene.mvi.databinding.FragmentTab2HeaderBinding
import wiki.scene.mvi.vm.tab1.TypeViewModel

@Route(path = ARouterConfig.App.FRAG_TAB_2)
class Tab2Fragment : BaseRecyclerViewFragment<ArticleInfo>(R.layout.fragment_tab_2) {
    private val mBinding by viewBinding(FragmentTab2Binding::bind)
    private val mViewModel by viewModels<TypeViewModel>()

    private var bannerBinding: FragmentTab2HeaderBinding? = null

    private val mAdapter by lazy { ListAdapter() }

    override fun injectTitleBarViewBinding(): BaseTitleBarViewBinding {
        return getTitleBarViewBinding(mBinding)
    }

    override fun injectRequestFirstPage(): Int {
        return 0
    }

    override fun initTitleBarView(titleBar: TitleBar) {
        titleBar.title = "Tab2"
    }

    override fun injectRefreshLayout(): SmartRefreshLayout {
        return mBinding.refreshLayout
    }

    override fun injectHeaderView(): Int {
        return R.layout.fragment_tab_2_header
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

    override fun initHeaderView(headerView: View) {
        super.initHeaderView(headerView)
        bannerBinding = FragmentTab2HeaderBinding.bind(headerView).apply {
            (banner as BannerViewPager<BannerInfo>).apply {
                //??????Indicator??????
                setIndicatorSlideMode(IndicatorSlideMode.SCALE)
                //??????Indicator??????
                setIndicatorHeight(6.toPx())
                //??????Indicator???????????????
                setIndicatorSliderWidth(6.toPx(), 16.toPx())
                //??????2???banner?????????
                setPageMargin(20.toPx())
                //??????
                setRevealWidth(20.toPx())
                setPageStyle(PageStyle.MULTI_PAGE_SCALE)
                adapter = BannerAdapter()
                setLifecycleRegistry(lifecycle)
            }.create()
        }
    }

    private fun bindBanner(newBannerList: MutableList<BannerInfo>) {
        bannerBinding?.banner?.refreshData(newBannerList)
    }

    override fun getListData(isFirst: Boolean, loadPage: Int) {
        launchAndCollect({
            mViewModel.getTab2Data(loadPage)
        }, {
            loadListDataStart(isFirst)
        }) {
            onSuccess = { resultInfo ->
                resultInfo?.run {
                    articleListInfo?.run {
                        loadListDataSuccess(
                            isFirst,
                            curPage,
                            pageCount,
                            datas
                        )
                    }

                    bannerList?.run {
                        if (this.isNotEmpty()) {
                            bindBanner(this)
                        }
                    }
                }

            }

            onFailed = { errorCode, errorMsg ->
                LogUtils.e("${errorCode}:$errorMsg")
                loadListDataFail(isFirst, loadPage)
            }
        }
    }
}