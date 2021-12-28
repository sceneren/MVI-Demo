package wiki.scene.common.toast

import androidx.annotation.StringRes

fun showToast(message: String?) {
    ToastUtil.show(message)
}

fun showToast(@StringRes resId: Int) {
    ToastUtil.show(resId)
}