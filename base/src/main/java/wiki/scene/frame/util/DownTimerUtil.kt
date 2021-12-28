package wiki.scene.frame.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

object DownTimerUtil {
    fun countDownCoroutines(
        total: Int,
        scope: CoroutineScope,
        downTimerListener: DownTimerListener.() -> Unit
    ): Job {
        val listener = DownTimerListener().also(downTimerListener)
        return flow {
            for (i in total downTo 1) {
                emit(i)
                delay(1000)
            }
        }.flowOn(Dispatchers.Main)
            .onStart { listener.onTimerStart.invoke() }
            .onEach { listener.onTimerTick.invoke(it) }
            .onCompletion { listener.onTimerFinish.invoke() }
            .launchIn(scope)
    }
}