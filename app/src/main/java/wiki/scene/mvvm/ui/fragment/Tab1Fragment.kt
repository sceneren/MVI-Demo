package wiki.scene.mvvm.ui.fragment

import by.kirich1409.viewbindingdelegate.viewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjq.bar.TitleBar
import wiki.scene.common.router.ARouterConfig
import wiki.scene.frame.base.fragment.BaseFragment
import wiki.scene.frame.databinding.BaseTitleBarViewBinding
import wiki.scene.mvvm.R
import wiki.scene.mvvm.adapter.TypeFragmentAdapter
import wiki.scene.mvvm.databinding.FragmentTab1Binding
import java.util.*

@Route(path = ARouterConfig.App.FRAG_TAB_1)
class Tab1Fragment : BaseFragment(R.layout.fragment_tab_1) {
    private val mBinding by viewBinding(FragmentTab1Binding::bind)
    private val typeList = mutableListOf<Int>()
    private val titleList = mutableListOf<String>()

    override fun injectTitleBarViewBinding(): BaseTitleBarViewBinding {
        return getTitleBarViewBinding(mBinding)
    }

    override fun initTitleBarView(titleBar: TitleBar) {
        titleBar.title = "Tab1"
    }

    override fun initView() {
        super.initView()
        for (i in 0..5) {
            typeList.add(i)
            titleList.add("类型$i")
        }
        mBinding.viewPager2.adapter = TypeFragmentAdapter(this, typeList)
        mBinding.viewPager2.offscreenPageLimit = titleList.size
        mBinding.tabLayout.setViewPager2(mBinding.viewPager2, titleList as ArrayList<String>)
    }

}