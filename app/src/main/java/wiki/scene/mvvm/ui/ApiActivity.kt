package wiki.scene.mvvm.ui

import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.hjq.bar.TitleBar
import com.jeremyliao.liveeventbus.LiveEventBus
import wiki.scene.common.router.ARouterConfig
import wiki.scene.common.toast.showToast
import wiki.scene.frame.base.activity.BaseActivity
import wiki.scene.frame.databinding.BaseTitleBarViewBinding
import wiki.scene.frame.ktx.launchFlow
import wiki.scene.frame.ktx.launchWithLoadingAndCollect
import wiki.scene.frame.ktx.launchWithLoadingDialog
import wiki.scene.mvvm.R
import wiki.scene.mvvm.bean.WxArticleBean
import wiki.scene.mvvm.databinding.ActivityApiBinding
import wiki.scene.mvvm.vm.ApiViewModel
import wiki.scene.network.observer.observeState

@Route(path = ARouterConfig.App.ACT_API)
class ApiActivity : BaseActivity(R.layout.activity_api) {

    private val mBinding by viewBinding(ActivityApiBinding::bind)
    private val mViewModel by viewModels<ApiViewModel>()

    override fun injectTitleBarViewBinding(): BaseTitleBarViewBinding {
        return BaseTitleBarViewBinding.bind(mBinding.root)
    }

    override fun initTitleBarView(titleBar: TitleBar) {
        super.initTitleBarView(titleBar)
        titleBar.title = "测试API使用"
    }

    override fun hasTitleBarBack(): Boolean {
        return true
    }

    override fun initView() {
        initData()
        initObserver()
    }

    override fun initObserver() {
        super.initObserver()

        mViewModel.wxArticleLiveData.observeState(this) {

            onSuccess = { data: List<WxArticleBean>? ->
                showNetErrorPic(false)
                mBinding.tvContent.text = data.toString()
            }

            onDataEmpty = { }

            onComplete = {}

            onFailed = { code, msg ->
                showToast("errorCode: $code   errorMsg: $msg")
                showNetErrorPic(true)
            }
        }

    }

    private fun showNetErrorPic(isShowError: Boolean) {
        mBinding.tvContent.isGone = isShowError
        mBinding.ivContent.isVisible = isShowError
    }

    private fun initData() {
        supportFragmentManager.beginTransaction().replace(R.id.fl_contain, ApiFragment()).commit()
        mBinding.btnNet.setOnClickListener {
            showNetErrorPic(false)
            requestNet()
        }

        mBinding.btnNetError.setOnClickListener {
            showNetErrorPic(false)
            requestNetError()
        }

        mBinding.btLogin.setOnClickListener {
            showNetErrorPic(false)
            login()
        }

        mBinding.btnNoData.setOnClickListener {
            logout()
        }
    }

    /**
     * 不接收返回结果，在viewmodel中通过livedata发送
     */
    private fun requestNet() {
        launchWithLoadingDialog {
            mViewModel.requestNet()
        }
    }

    private fun requestNetError() {
        launchWithLoadingDialog {
            mViewModel.requestNetError()
        }
    }

    /**
     * 链式调用，返回结果的处理都在一起，viewmodel中不需要创建一个livedata对象
     */
    private fun login() {
        launchWithLoadingAndCollect({
            mViewModel.login("FastJetpack", "FastJetpack")
        }) {
            onSuccess = {
                mBinding.tvContent.text = it.toString()
            }
            onFailed = { errorCode, errorMsg ->
                showToast("errorCode: $errorCode   errorMsg: $errorMsg")
            }
        }
    }

    /**
     * 将Flow转变为LiveData
     */
    private fun loginAsLiveData() {
        val loginLiveData = launchFlow(requestBlock = {
            mViewModel.login(
                "FastJetpack",
                "FastJetpack11"
            )
        }).asLiveData()

        loginLiveData.observeState(this) {
            onSuccess = {
                mBinding.tvContent.text = it.toString()
            }
            onFailed = { errorCode, errorMsg ->
                showToast("errorCode: $errorCode   errorMsg: $errorMsg")
            }
        }
    }

    private fun logout() {

        launchWithLoadingAndCollect({ mViewModel.test() }) {
            onSuccess = {
                LogUtils.e("xx", "====>onSuccess")
            }

            onDataEmpty = {
                LogUtils.e("xx", "====>onDataEmpty")
                LiveEventBus.get<WxArticleBean>("key")
                    .post(WxArticleBean(123, "Test", 1))
            }

            onFailed = { errorCode, errorMsg ->
                LogUtils.e("xx", "====>$errorCode:$errorMsg")
            }
        }
    }


}