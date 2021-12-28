package wiki.scene.network.observer

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import wiki.scene.entity.base.*

/**
 *
 * @Description:    livedata
 * @Author:         scene
 * @CreateDate:     2021/11/9 17:54
 * @UpdateUser:
 * @UpdateDate:     2021/11/9 17:54
 * @UpdateRemark:
 * @Version:        1.0.0
 */
typealias StateLiveData<T> = LiveData<ApiResponse<T>>

typealias StateMutableLiveData<T> = MutableLiveData<ApiResponse<T>>

@MainThread
inline fun <T> StateMutableLiveData<T>.observeState(
    owner: LifecycleOwner,
    listenerBuilder: ResultBuilder<T>.() -> Unit
) {
    val listener = ResultBuilder<T>().also(listenerBuilder)
    observe(owner) { apiResponse ->
        when (apiResponse) {
            is ApiSuccessResponse -> listener.onSuccess(apiResponse.response)
            is ApiEmptyResponse -> listener.onDataEmpty()
            is ApiFailedResponse -> listener.onFailed(apiResponse.errorCode, apiResponse.errorMsg)
        }
        listener.onComplete()
    }
}

@MainThread
inline fun <T> LiveData<ApiResponse<T>>.observeState(
    owner: LifecycleOwner,
    listenerBuilder: ResultBuilder<T>.() -> Unit
) {
    val listener = ResultBuilder<T>().also(listenerBuilder)
    observe(owner) { apiResponse ->
        when (apiResponse) {
            is ApiSuccessResponse -> listener.onSuccess(apiResponse.response)
            is ApiEmptyResponse -> listener.onDataEmpty()
            is ApiFailedResponse -> {
                LogUtils.e(apiResponse.errorCode, apiResponse.errorMsg)
                listener.onFailed(apiResponse.errorCode, apiResponse.errorMsg)
            }

        }
        listener.onComplete()
    }
}

class ResultBuilder<T> {
    var onSuccess: (data: T?) -> Unit = {}
    var onDataEmpty: () -> Unit = {}
    var onFailed: (errorCode: Int?, errorMsg: String?) -> Unit = { errorCode, errorMsg ->
        LogUtils.e("errorCode:$errorCode,errorMsg:$errorMsg")
    }
    var onComplete: () -> Unit = {}
}