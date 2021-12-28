package wiki.scene.mvvm.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.launcher.ARouter
import wiki.scene.common.router.ARouterConfig

class TypeFragmentAdapter(fragment: Fragment, private val typeList: MutableList<Int>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return typeList.size
    }

    override fun createFragment(position: Int): Fragment {
        return ARouter.getInstance()
            .build(ARouterConfig.App.FRAG_TYPE)
            .withInt("type", typeList[position])
            .navigation() as Fragment
    }
}
