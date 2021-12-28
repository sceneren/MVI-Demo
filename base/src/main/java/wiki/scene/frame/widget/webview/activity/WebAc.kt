package wiki.scene.frame.widget.webview.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.webkit.WebView
import android.widget.LinearLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.hjq.bar.TitleBar
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.kongzue.dialogx.dialogs.BottomMenu
import wiki.scene.common.router.ARouterConfig
import wiki.scene.frame.R
import wiki.scene.frame.base.activity.BaseActivity
import wiki.scene.frame.databinding.BaseTitleBarViewBinding
import wiki.scene.frame.databinding.BaseWebAcWebBinding
import wiki.scene.frame.databinding.BaseWebAcWebTitleBarViewRightActionViewBinding
import wiki.scene.frame.widget.titlebar.addCustomRightView
import wiki.scene.frame.widget.webview.layout.WebLayout

@Route(path = ARouterConfig.Base.BASE_WEB)
class WebAc : BaseActivity(R.layout.base_web_ac_web) {

    @JvmField
    @Autowired(name = "webUrl")
    var url: String? = null

    private var agentWeb: AgentWeb? = null
    private val mBinding by viewBinding(BaseWebAcWebBinding::bind)

    private val mWebViewClient: WebViewClient = object : WebViewClient() {

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        }
    }
    private val mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            getTitleBarViewBinding(mBinding).libBaseTvTitleBar.title = title
        }
    }

    override fun hasTitleBarBack(): Boolean {
        return true
    }

    override fun injectTitleBarViewBinding(): BaseTitleBarViewBinding {
        return BaseTitleBarViewBinding.bind(mBinding.root)
    }

    override fun initTitleBarView(titleBar: TitleBar) {
        super.initTitleBarView(titleBar)
        val rightActionView = View.inflate(
            mActivity,
            R.layout.base_web_ac_web_title_bar_view_right_action_view,
            null
        )

        val actionBinding = BaseWebAcWebTitleBarViewRightActionViewBinding.bind(rightActionView)
        actionBinding.rlClose.setOnClickListener {
            finish()
        }

        actionBinding.rlMore.setOnClickListener {

            BottomMenu.show(arrayOf("刷新", "使用浏览器打开"))
                .setCancelButton("取消")
                .setOnMenuItemClickListener { _, _, index ->
                    when (index) {
                        0 -> {
                            this@WebAc.agentWeb!!.urlLoader.reload()
                        }
                        1 -> {
                            try {
                                val i = Intent(Intent.ACTION_VIEW)
                                i.data = Uri.parse(url)
                                startActivity(i)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }
                    false
                }
        }
        titleBar.addCustomRightView(rightActionView, 100F, 30F)
    }

    override fun onLeftClick(view: View?) {
        super.onLeftClick(view)
        backOrFinish()
    }

    override fun initView() {
        super.initView()
        LogUtils.d("webUrl=$url")
        agentWeb = AgentWeb.with(this)
            .setAgentWebParent(mBinding.parent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setWebChromeClient(mWebChromeClient)
            .setWebViewClient(mWebViewClient)
            .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setWebLayout(WebLayout(this))
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK) //打开其他应用时，弹窗咨询用户是否前往其他应用
            .interceptUnkownUrl() //拦截找不到相关页面的Scheme
            .createAgentWeb()
            .ready()
            .go(url)
    }

    override fun onBackPressed() {
        backOrFinish()
    }

    private fun backOrFinish() {
        if (agentWeb == null) {
            finish()
        } else {
            if (agentWeb!!.webCreator.webView.canGoBack()) {
                agentWeb!!.back()
            } else {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        agentWeb?.webLifeCycle?.onResume()
    }

    override fun onPause() {
        super.onPause()
        agentWeb?.webLifeCycle?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb?.webLifeCycle?.onDestroy()
    }
}