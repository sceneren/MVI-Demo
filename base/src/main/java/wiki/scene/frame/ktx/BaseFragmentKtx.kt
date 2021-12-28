package wiki.scene.frame.ktx

import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import wiki.scene.common.dialog.dismissLoadingDialog
import wiki.scene.common.dialog.showLoadingDialog
import wiki.scene.entity.base.ApiEmptyResponse
import wiki.scene.entity.base.ApiFailedResponse
import wiki.scene.entity.base.ApiResponse
import wiki.scene.entity.base.ApiSuccessResponse
import wiki.scene.frame.base.fragment.BaseFragment
import wiki.scene.network.R
import wiki.scene.network.observer.ResultBuilder


/**
 * 这个方法只是简单的一个封装Loading的普通方法，不返回任何实体类
 */
fun BaseFragment.launchWithLoadingDialog(requestBlock: suspend () -> Unit) {
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

fun BaseFragment.launchWithLoadingPage(isFirst: Boolean, requestBlock: suspend () -> Unit) {
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
fun <T> BaseFragment.launchAndCollect(
    requestBlock: suspend () -> ApiResponse<T>,
    startBlock: () -> Unit = {},
    listenerBuilder: ResultBuilder<T>.() -> Unit
) {
    lifecycleScope.launch {
        launchFlow(requestBlock)
            .onStart { startBlock.invoke() }
            .collect { response ->
                parseResultAndCallback(response, listenerBuilder)
            }
    }
}

/**
 * 请求带Loading&&不需要声明LiveData
 */
fun <T> BaseFragment.launchWithLoadingDialogAndCollect(
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

fun <T> BaseFragment.launchWithLoadingPageAndCollect(
    requestBlock: suspend () -> ApiResponse<T>,
    listenerBuilder: ResultBuilder<T>.() -> Unit
) {
    lifecycleScope.launch {
        launchFlow(
            requestBlock,
            { showLoadingPage() },
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
        is ApiFailedResponse -> {
            LogUtils.e(response.errorCode, response.errorMsg)
            listener.onFailed(response.errorCode, response.errorMsg)
        }
    }
    listener.onComplete()
}
