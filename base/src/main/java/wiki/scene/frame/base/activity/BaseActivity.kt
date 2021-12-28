package wiki.scene.frame.base.activity

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.KeyboardUtils
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.parfoismeng.slidebacklib.registerSlideBack
import com.parfoismeng.slidebacklib.unregisterSlideBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.jessyan.autosize.AutoSizeCompat
import wiki.scene.frame.R
import wiki.scene.frame.base.IUiView
import wiki.scene.frame.databinding.BaseTitleBarViewBinding
import wiki.scene.frame.loadsir.ErrorCallback
import wiki.scene.frame.loadsir.LoadingCallback

/**
 *
 * @Description:    基类
 * @Author:         scene
 * @CreateDate:     2021/11/10 10:20
 * @UpdateUser:
 * @UpdateDate:     2021/11/10 10:20
 * @UpdateRemark:
 * @Version:        1.0.0
 */
abstract class BaseActivity(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId),
    IUiView, OnTitleBarListener {

    protected open lateinit var mActivity: AppCompatActivity

    private val loadSir by lazy {
        LoadSir.getDefault()
    }

    private var loadService: LoadService<Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        mActivity = this
        if (enableSlideBack()) {
            registerSlideBack {
                finish()
            }
        }
        bindTitleBarViewBinding()
        if (injectLoadServiceView() != null) {
            loadService = loadSir.register(injectLoadServiceView()) {
                onRetryBtnClick()
            }
        }
        initImmersionBar()
        initView()
        beforeLoadData()
        loadData()
        initListener()
        initObserver()
    }

    override fun hasTitleBarBack(): Boolean {
        return false
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
            titleBar(injectTitleBarViewBinding()?.libBaseTvTitleBar)
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
            setOnTitleBarListener(this@BaseActivity)
        }
    }

    override fun injectLoadServiceView(): View? {
        return null
    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun initObserver() {

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterSlideBack()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (touchHideSoft()) {
            ev?.run {
                if (ev.action == MotionEvent.ACTION_DOWN) {
                    val v = currentFocus
                    v?.let {
                        if (isShouldHideKeyboard(v, ev)) {
                            KeyboardUtils.hideSoftInput(v)
                        }
                    }

                }
            }

        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 是否触摸editText以外的隐藏软键盘
     */
    open fun touchHideSoft(): Boolean {
        return true
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    private fun isShouldHideKeyboard(v: View, event: MotionEvent): Boolean {
        if (v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            // 点击EditText的事件，忽略它。
            return (event.x <= left || event.x >= right
                    || event.y <= top || event.y >= bottom)
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false
    }

    override fun onLeftClick(view: View?) {
        onBackPressed()
    }

    override fun onTitleClick(view: View?) {

    }

    override fun onRightClick(view: View?) {

    }

    override fun getResources(): Resources {
        CoroutineScope(Dispatchers.Main).launch {
            AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        }
        return super.getResources()
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

    open fun enableSlideBack(): Boolean {
        return true
    }

}