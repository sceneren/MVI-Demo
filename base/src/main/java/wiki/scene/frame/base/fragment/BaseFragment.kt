package wiki.scene.frame.base.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import wiki.scene.frame.R
import wiki.scene.frame.base.IUiView
import wiki.scene.frame.databinding.BaseTitleBarViewBinding
import wiki.scene.frame.loadsir.ErrorCallback
import wiki.scene.frame.loadsir.LoadingCallback

/**
 *
 * @Description:    基类
 * @Author:         scene
 * @CreateDate:     2021/11/10 10:22
 * @UpdateUser:
 * @UpdateDate:     2021/11/10 10:22
 * @UpdateRemark:
 * @Version:        1.0.0
 */
abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId), IUiView,
    OnTitleBarListener {

    //是否已经加载过数据
    private var isLoaded = false

    protected open lateinit var mContext: Context
    protected open lateinit var mActivity: AppCompatActivity

    private val loadSir by lazy {
        LoadSir.getDefault()
    }

    private var loadService: LoadService<Any>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = requireActivity() as AppCompatActivity
        mContext = requireContext()
        ARouter.getInstance().inject(this)
    }

    override fun hasTitleBarBack(): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            beforeLoadData()
            loadData()
            isLoaded = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectLoadServiceView()?.run {
            loadService = loadSir.register(injectLoadServiceView()) {
                onRetryBtnClick()
            }
        }
        bindTitleBarViewBinding()
        initImmersionBar()
        initView()
        initListener()
        initObserver()

    }

    override fun injectLoadServiceView(): View? {
        return null
    }

    open fun injectTitleBarViewBinding(): BaseTitleBarViewBinding? {
        return null
    }

    open fun fullScreen(): Boolean {
        return false
    }

    open fun isDarkMode(): Boolean {
        return true
    }

    open fun keyboardEnable(): Boolean {
        return false
    }

    private fun initImmersionBar() {
        immersionBar {
            if (fullScreen()) {
                hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
            }
            statusBarDarkFont(isDarkMode())
            navigationBarDarkIcon(isDarkMode())
            navigationBarColorInt(Color.WHITE)
            keyboardEnable(keyboardEnable())
        }
    }

    override fun beforeLoadData() {

    }

    override fun loadData() {

    }

    private fun bindTitleBarViewBinding() {
        injectTitleBarViewBinding()?.let {
            initTitleBarView(it.libBaseTvTitleBar)
        }
    }

    override fun initTitleBarView(titleBar: TitleBar) {
        titleBar.run {
            if (hasTitleBarBack()) {
                setLeftIcon(R.drawable.base_ic_back)
            }
            setOnTitleBarListener(this@BaseFragment)
        }
    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun initObserver() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    override fun onLeftClick(view: View?) {
        mActivity.onBackPressed()
    }

    override fun onTitleClick(view: View?) {

    }

    override fun onRightClick(view: View?) {

    }

    override fun showLoadingPage() {
        loadService?.showCallback(LoadingCallback::class.java)
    }

    override fun showSuccessPage() {
        loadService?.showSuccess()
    }

    override fun showErrorPage() {
        loadService?.showCallback(ErrorCallback::class.java)
    }

    override fun onRetryBtnClick() {
        loadData()
    }

    open fun getTitleBarViewBinding(binding: ViewBinding): BaseTitleBarViewBinding {
        return BaseTitleBarViewBinding.bind(binding.root)
    }

}