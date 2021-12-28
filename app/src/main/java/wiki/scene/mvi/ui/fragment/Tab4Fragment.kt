package wiki.scene.mvi.ui.fragment

import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.hjq.bar.TitleBar
import wiki.scene.common.router.ARouterConfig
import wiki.scene.common.view.clickWithLimit
import wiki.scene.frame.base.fragment.BaseFragment
import wiki.scene.frame.databinding.BaseTitleBarViewBinding
import wiki.scene.frame.util.DownTimerUtil
import wiki.scene.mvi.R
import wiki.scene.mvi.databinding.FragmentTab4Binding

@Route(path = ARouterConfig.App.FRAG_TAB_4)
class Tab4Fragment : BaseFragment(R.layout.fragment_tab_4) {
    private val mBinding by viewBinding(FragmentTab4Binding::bind)

    override fun injectTitleBarViewBinding(): BaseTitleBarViewBinding {
        return BaseTitleBarViewBinding.bind(mBinding.root)
    }

    override fun initTitleBarView(titleBar: TitleBar) {
        titleBar.title = "Tab3"
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnStartDownTimer
            .clickWithLimit {
                DownTimerUtil.countDownCoroutines(10, lifecycleScope) {
                    onTimerStart = {
                        LogUtils.e("xx", "====>onTimerStart")
                        mBinding.btnStartDownTimer.isEnabled = false
                    }
                    onTimerTick = {
                        LogUtils.e("xx", "====>onTimerTick:$it")
                        mBinding.btnStartDownTimer.text = "倒计时:$it"
                    }

                    onTimerFinish = {
                        LogUtils.e("xx", "====>onFinish")
                        mBinding.btnStartDownTimer.run {
                            isEnabled = true
                            text = "开始倒计时"
                        }

                    }
                }
            }

    }
}