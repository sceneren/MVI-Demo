package wiki.scene.frame.ktx

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import wiki.scene.common.dialog.dismissLoadingDialog
import wiki.scene.common.dialog.showLoadingDialog
import wiki.scene.entity.base.*
import wiki.scene.frame.base.activity.BaseActivity
import wiki.scene.network.R
import wiki.scene.network.observer.ResultBuilder

fun <T> launchFlow(
    requestBlock: suspend () -> ApiResponse<T>,
    startCallback: (() -> Unit)? = null,
    completeCallback: (() -> Unit)? = null
): Flow<ApiResponse<T>> {
    return flow {
        emit(requestBlock())
    }.onStart {
        startCallback?.invoke()
    }.onCompletion {
        completeCallback?.invoke()
    }
}

/**
 * 这个方法只是简单的一个封装Loading的普通方法，不返回任何实体类
 */
fun BaseActivity.launchWithLoadingDialog(requestBlock: suspend () -> Unit) {
    lifecycleScope.launch {
        flow {
            emit(requestBlock())
        }.onStart {
            showLoadingDialog(R.string.network_loading)
        }.onCompletion {
            dismissLoadingDialog()
        }.collect()
    }
}

fun BaseActivity.launchWithLoadingPage(isFirst: Boolean, requestBlock: suspend () -> Unit) {
    lifecycleScope.launch {
        flow {
            emit(requestBlock())
        }.onStart {
            if (isFirst) {
                showLoadingPage()
            }
        }.catch {
            showErrorPage()
        }.onCompletion {
            showSuccessPage()
        }.collect()
    }
}


/**
 * 请求不带Loading&&不需要声明LiveData
 */
fun <T> BaseActivity.launchAndCollect(
    requestBlock: suspend () -> ApiResponse<T>,
    startBlock: () -> Unit = {},
    listenerBuilder: ResultBuilder<T>.() -> Unit
) {
    lifecycleScope.launch {
        launchFlow(requestBlock).collect { response ->
            parseResultAndCallback(response, listenerBuilder)
        }
    }
}

/**
 * 请求带Loading&&不需要声明LiveData
 */
fun <T> BaseActivity.launchWithLoadingAndCollect(
    requestBlock: suspend () -> ApiResponse<T>,
    listenerBuilder: ResultBuilder<T>.() -> Unit
) {
    lifecycleScope.launch {
        launchFlow(
            requestBlock,
            { showLoadingDialog() },
            { dismissLoadingDialog() }).collect { response ->
            parseResultAndCallback(response, listenerBuilder)
        }
    }
}

private fun <T> parseResultAndCallback(
    response: ApiResponse<T>,
    listenerBuilder: ResultBuilder<T>.() -> Unit
) {
    val listener = ResultBuilder<T>().also(listenerBuilder)
    when (response) {
        is ApiSuccessResponse -> listener.onSuccess(response.response)
        is ApiEmptyResponse -> listener.onDataEmpty()
        is ApiFailedResponse -> listener.onFailed(response.errorCode, response.errorMsg)
    }
    listener.onComplete()
}
