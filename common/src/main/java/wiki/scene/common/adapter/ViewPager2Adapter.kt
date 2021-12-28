package wiki.scene.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager2Adapter :
    FragmentStateAdapter {
    private var fragmentArray: MutableList<Fragment>

    constructor(fragment: Fragment, fragmentArray: MutableList<Fragment>) : super(fragment) {
        this.fragmentArray = fragmentArray
    }

    constructor(
        activity: FragmentActivity,
        fragmentArray: MutableList<Fragment>
    ) : super(activity) {
        this.fragmentArray = fragmentArray
    }

    override fun getItemCount(): Int {
        return fragmentArray.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentArray[position]
    }

}