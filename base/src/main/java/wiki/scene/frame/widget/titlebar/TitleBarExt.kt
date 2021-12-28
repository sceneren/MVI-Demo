package wiki.scene.frame.widget.titlebar

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.blankj.utilcode.util.SizeUtils
import com.hjq.bar.TitleBar

fun TitleBar.addCustomRightView(view: View, widthDp: Float, heightDp: Float) {
    val params = FrameLayout.LayoutParams(SizeUtils.dp2px(widthDp), SizeUtils.dp2px(heightDp))
    params.gravity = Gravity.END or Gravity.CENTER_VERTICAL
    params.marginStart = SizeUtils.dp2px(10F)
    params.marginEnd = SizeUtils.dp2px(10F)
    this.addView(view, params)

    val titleLayoutParams = this.titleView.layoutParams as FrameLayout.LayoutParams
    titleLayoutParams.marginStart = SizeUtils.dp2px(widthDp)
    titleLayoutParams.marginEnd = SizeUtils.dp2px(widthDp)
    this.titleView.layoutParams = titleLayoutParams
}