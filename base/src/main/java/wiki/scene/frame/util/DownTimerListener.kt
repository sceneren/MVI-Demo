package wiki.scene.frame.util

import com.blankj.utilcode.util.LogUtils

class DownTimerListener {
    var onTimerStart: () -> Unit = {
        LogUtils.e("xx", "====>DownTimerListener.onTimerStart")
    }
    var onTimerTick: (second: Int) -> Unit = {}
    var onTimerFinish: () -> Unit = {}
}