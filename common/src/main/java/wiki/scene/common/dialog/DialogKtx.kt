package wiki.scene.common.dialog

import androidx.annotation.StringRes
import com.kongzue.dialogx.dialogs.TipDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import wiki.scene.common.R

fun showLoadingDialog(message: String) {
    WaitDialog.show(message)
}

fun showErrorDialog(message: String) {
    TipDialog.show(message, WaitDialog.TYPE.ERROR)
}

fun showErrorDialog(@StringRes resId: Int) {
    TipDialog.show(resId, WaitDialog.TYPE.ERROR)
}

fun showSuccessDialog(message: String) {
    TipDialog.show(message, WaitDialog.TYPE.SUCCESS)
}

fun showSuccessDialog(@StringRes resId: Int) {
    TipDialog.show(resId, WaitDialog.TYPE.SUCCESS)
}

fun showLoadingDialog(@StringRes resId: Int = R.string.common_loading) {
    WaitDialog.show(resId)
}

fun dismissLoadingDialog() {
    WaitDialog.dismiss()
}