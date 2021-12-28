package wiki.scene.common.tab

import androidx.annotation.DrawableRes
import com.flyco.tablayout.listener.CustomTabEntity

class MainTabEntity(
    private val title: String,
    @DrawableRes private val selectedIcon: Int,
    @DrawableRes private val unSelectedIcon: Int
) : CustomTabEntity {

    override fun getTabTitle(): String {
        return title
    }

    override fun getTabSelectedIcon(): Int {
        return selectedIcon
    }

    override fun getTabUnselectedIcon(): Int {
        return unSelectedIcon
    }
}