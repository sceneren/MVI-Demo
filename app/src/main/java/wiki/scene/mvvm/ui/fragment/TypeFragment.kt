package wiki.scene.mvvm.ui.fragment

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.luck.picture.lib.entity.LocalMedia
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import wiki.scene.common.mojito.MojitoUtils
import wiki.scene.common.router.ARouterConfig
import wiki.scene.entity.wanandroid.ArticleInfo
import wiki.scene.frame.base.fragment.BaseRecyclerViewFragment
import wiki.scene.frame.ktx.launchAndCollect
import wiki.scene.frame.router.RouterUtil
import wiki.scene.frame.widget.picture_selector.PictureSelectorUtils
import wiki.scene.frame.widget.picture_selector.listener.PictureSelectorListener
import wiki.scene.mvvm.R
import wiki.scene.mvvm.adapter.ListAdapter
import wiki.scene.mvvm.databinding.FragmentTypeBinding
import wiki.scene.mvvm.ui.ApiActivity
import wiki.scene.mvvm.vm.tab1.TypeViewModel

@Route(path = ARouterConfig.App.FRAG_TYPE)
class TypeFragment : BaseRecyclerViewFragment<ArticleInfo>(R.layout.fragment_type) {
    @JvmField
    @Autowired
    var type: Int = 0

    private val mBinding by viewBinding(FragmentTypeBinding::bind)
    private val mViewModel by viewModels<TypeViewModel>()
    private val mAdapter by lazy { ListAdapter() }

    override fun injectRequestFirstPage(): Int {
        return 0
    }

    override fun injectRecyclerView(): RecyclerView {
        return mBinding.recyclerview
    }

    override fun injectLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(mContext)
    }

    override fun injectRefreshLayout(): SmartRefreshLayout {
        return mBinding.refreshLayout
    }

    override fun injectAdapter(): BaseQuickAdapter<ArticleInfo, out BaseViewHolder> {
        mAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.left_menu -> {
                    LogUtils.e("left_menu")
                    val list = mutableListOf(
                        "/storage/emulated/0/Pictures/1.png",
                        "/storage/emulated/0/Pictures/2.png",
                        "/storage/emulated/0/Pictures/3.png",
                        "/storage/emulated/0/Pictures/4.png",
//                        "https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1592057985345-assets/web-upload/c2fe2b62-5519-4129-856e-ba19428a508a.jpeg",
                        "https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1591753659216-assets/web-upload/2c772338-b6b6-4173-a830-202831511172.jpeg"
                    )
                    MojitoUtils.previewImage(mBinding.recyclerview, R.id.tv_name, position, list)
                }
                R.id.right_menu -> {
                    LogUtils.e("right_menu")
                    startActivity(Intent(mContext, ApiActivity::class.java))
                }
                R.id.tv_name -> {
                    if (type == 0) {
                        PictureSelectorUtils.selectPicture(
                            this@TypeFragment,
                            8,
                            object : PictureSelectorListener() {
                                override fun onResult(result: MutableList<LocalMedia>?) {
                                    result?.run {
                                        LogUtils.e(forEach { LogUtils.e(it.toString()) })
                                        MojitoUtils.previewImage(mContext, map { it.compressPath })
                                    }

                                }

                            })
                    } else {
                        RouterUtil.launchWeb(mAdapter.data[position].link)
                    }

                }
            }
        }
        return mAdapter
    }

    override fun getListData(isFirst: Boolean, loadPage: Int) {
        launchAndCollect({
            mViewModel.articleList(loadPage)
        }, {
            loadListDataStart(isFirst)
        }) {
            onSuccess = { result ->
                result?.let {
                    loadListDataSuccess(isFirst, it.curPage, it.pageCount, it.datas)
                }
            }
            onFailed = { errorCode, errorMsg ->
                LogUtils.e("errorCode:$errorCode,errorMsg:$errorMsg")
                loadListDataFail(isFirst, loadPage)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        PictureSelectorUtils.clearCache()
    }
}