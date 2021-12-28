package wiki.scene.common.utils

import com.blankj.utilcode.util.SizeUtils

fun Int.toPx(): Int {
    return this.toFloat().toPx()
}

fun Float.toPx(): Int {
    return SizeUtils.dp2px(this)
}