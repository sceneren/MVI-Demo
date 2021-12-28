package wiki.scene.common.tab

import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.OnTabSelectListener
import wiki.scene.common.adapter.ViewPager2Adapter

fun CommonTabLayout.bind(
    fragment: Fragment,
    viewPager2: ViewPager2,
    fragmentArray: MutableList<Fragment>,
    listener: MainTabSelectListener? = null
) {
    viewPager2.adapter = ViewPager2Adapter(fragment, fragmentArray)
    viewPager2.isUserInputEnabled = false

    setOnTabSelectListener(object : OnTabSelectListener {
        override fun onTabSelect(position: Int) {
            viewPager2.setCurrentItem(position, false)
            listener?.onTabSelect(position)
        }

        override fun onTabReselect(position: Int) {
            listener?.onTabReselect(position)
        }

    })
}

fun CommonTabLayout.bind(
    activity: FragmentActivity,
    viewPager2: ViewPager2,
    fragmentArray: MutableList<Fragment>,
    listener: MainTabSelectListener? = null
) {
    viewPager2.adapter = ViewPager2Adapter(activity, fragmentArray)
    viewPager2.offscreenPageLimit = viewPager2.size
    viewPager2.isUserInputEnabled = false
    setOnTabSelectListener(object : OnTabSelectListener {
        override fun onTabSelect(position: Int) {
            viewPager2.setCurrentItem(position, false)
            listener?.onTabSelect(position)
        }

        override fun onTabReselect(position: Int) {
            listener?.onTabReselect(position)
        }

    })
}