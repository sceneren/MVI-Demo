package wiki.scene.common.toast

import android.app.Application
import androidx.annotation.StringRes
import com.hjq.toast.ToastUtils

object ToastUtil {
    fun init(application: Application) {
        // 初始化 Toast 框架
        ToastUtils.init(application)
    }

    fun show(message: String?) {
        ToastUtils.show(message)
    }

    fun show(@StringRes resId: Int) {
        ToastUtils.show(resId)
    }

    fun show(any: Any?) {
        ToastUtils.show(any)
    }
}