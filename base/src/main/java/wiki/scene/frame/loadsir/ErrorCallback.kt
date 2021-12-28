package wiki.scene.frame.loadsir

import android.content.Context
import android.view.View
import com.kingja.loadsir.callback.Callback
import wiki.scene.frame.R

class ErrorCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.base_error_view
    }

    override fun onReloadEvent(context: Context, view: View): Boolean {
        return false
    }

}