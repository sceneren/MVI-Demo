package wiki.scene.mvvm.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import wiki.scene.mvvm.R
import wiki.scene.mvvm.bean.WxArticleBean
import wiki.scene.mvvm.databinding.FragmentApiBinding
import wiki.scene.mvvm.vm.ApiViewModel
import wiki.scene.frame.base.fragment.BaseFragment
import wiki.scene.network.observer.observeState

/**
 *
 * @Description:    请求接口
 * @Author:         scene
 * @CreateDate:     2021/11/10 10:22
 * @UpdateUser:
 * @UpdateDate:     2021/11/10 10:22
 * @UpdateRemark:
 * @Version:        1.0.0
 */
class ApiFragment : BaseFragment(R.layout.fragment_api) {

    private val mBinding by viewBinding(FragmentApiBinding::bind)
    private val mViewModel by activityViewModels<ApiViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    override fun initObserver() {
        super.initObserver()

        mViewModel.wxArticleLiveData.observeState(this) {
            onSuccess = { result: List<WxArticleBean>? ->
                result?.let {
                    val wxArticleBean: WxArticleBean = result[0]
                    (wxArticleBean.name + "   是否展示： " + wxArticleBean.visible + "\n" + result[1].name + "   是否展示： " + result[1].visible).also { mBinding.text.text = it }
                }
            }
        }

    }
}