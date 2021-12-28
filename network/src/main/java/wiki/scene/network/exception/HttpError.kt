package wiki.scene.network.exception

import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException

enum class HttpError(var code: Int, var errorMsg: String) {
    TOKEN_EXPIRE(3001, "token is expired"),
    PARAMS_ERROR(4003, "params is error")
    // ...... more
}

internal fun handlingApiExceptions(code: Int?, errorMsg: String?) = when (code) {
    HttpError.TOKEN_EXPIRE.code -> {
        //处理逻辑
    }
    HttpError.PARAMS_ERROR.code -> {
        //处理逻辑
    }
    else -> errorMsg?.let {
        //处理逻辑
    }
}

internal fun handlingExceptions(e: Throwable) = when (e) {
    is HttpException -> {
        //处理逻辑
    }
    is CancellationException -> {
    }
    is SocketTimeoutException -> {
    }
    is JsonParseException -> {
    }
    else -> {
    }
}