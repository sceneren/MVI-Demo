package wiki.scene.mvi.callback

import com.kingja.loadsir.callback.Callback
import wiki.scene.mvi.R

class PlaceHolderCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_place_holder
    }
}