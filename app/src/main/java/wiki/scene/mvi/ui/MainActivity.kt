package wiki.scene.mvi.ui

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.flyco.tablayout.listener.CustomTabEntity
import wiki.scene.common.tab.MainTabEntity
import wiki.scene.common.tab.MainTabSelectListener
import wiki.scene.common.tab.bind
import wiki.scene.frame.base.activity.BaseActivity
import wiki.scene.mvi.R
import wiki.scene.common.router.ARouterConfig
import wiki.scene.mvi.databinding.ActivityMainBinding
import java.util.*

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val mBinding by viewBinding(ActivityMainBinding::bind)

    private val tabList = mutableListOf<CustomTabEntity>()
    private val fragmentArray = mutableListOf<Fragment>()

    override fun enableSlideBack(): Boolean {
        return false
    }

    override fun initView() {
        super.initView()
        tabList.add(
            MainTabEntity(
                "Tab1",
                R.drawable.tab_home_select,
                R.drawable.tab_home_unselect
            )
        )
        tabList.add(
            MainTabEntity(
                "Tab2",
                R.drawable.tab_speech_select,
                R.drawable.tab_speech_unselect
            )
        )
        tabList.add(
            MainTabEntity(
                "Tab3",
                R.drawable.tab_contact_select,
                R.drawable.tab_contact_unselect
            )
        )
        tabList.add(
            MainTabEntity(
                "Tab4",
                R.drawable.tab_more_select,
                R.drawable.tab_more_unselect
            )
        )

        fragmentArray.add(
            0,
            ARouter.getInstance().build(ARouterConfig.App.FRAG_TAB_1).navigation() as Fragment
        )
        fragmentArray.add(
            1,
            ARouter.getInstance().build(ARouterConfig.App.FRAG_TAB_2).navigation() as Fragment
        )
        fragmentArray.add(
            2,
            ARouter.getInstance().build(ARouterConfig.App.FRAG_TAB_3).navigation() as Fragment
        )
        fragmentArray.add(
            3,
            ARouter.getInstance().build(ARouterConfig.App.FRAG_TAB_4).navigation() as Fragment
        )

        mBinding.tabLayout.setTabData(tabList as ArrayList<CustomTabEntity>)
        mBinding.tabLayout.bind(
            this,
            mBinding.viewPager,
            fragmentArray,
            object : MainTabSelectListener {
                override fun onTabSelect(position: Int) {
                    super.onTabSelect(position)
                    mBinding.tabLayout.hideMsg(position)
                }
            })
        mBinding.tabLayout.showMsg(1, 999)
        mBinding.tabLayout.setMsgMargin(1, -10F, 0F)
    }

}
