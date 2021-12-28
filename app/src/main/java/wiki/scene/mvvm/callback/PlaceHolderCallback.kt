package wiki.scene.mvvm.callback

import com.kingja.loadsir.callback.Callback
import wiki.scene.mvvm.R

class PlaceHolderCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_place_holder
    }
}