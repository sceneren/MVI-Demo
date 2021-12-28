package wiki.scene.frame.base

import android.view.View
import com.hjq.bar.TitleBar

interface IUiView {
    fun beforeLoadData()
    fun loadData()
    fun initTitleBarView(titleBar: TitleBar)
    fun initView()
    fun initListener()
    fun initObserver()
    fun hasTitleBarBack(): Boolean
    fun injectLoadServiceView(): View?
    fun showLoadingPage()
    fun showSuccessPage()
    fun showErrorPage()

    /**
     * 点击重试按钮
     */
    fun onRetryBtnClick()
}