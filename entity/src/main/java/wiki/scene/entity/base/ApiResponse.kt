package wiki.scene.entity.base

import java.io.Serializable

open class ApiResponse<T>(
    open val data: T? = null,
    open val errorCode: Int? = null,
    open val errorMsg: String? = null,
    open val error: Throwable? = null,
) : Serializable {
    val isSuccess: Boolean
        get() = errorCode == 0

    companion object {
        const val NET_ERROR_CODE = -9001
    }
}

data class ApiSuccessResponse<T>(val response: T) : ApiResponse<T>(data = response, errorCode = 0)

class ApiEmptyResponse<T> : ApiResponse<T>(errorCode = 0)

data class ApiFailedResponse<T>(override val errorCode: Int?, override val errorMsg: String?) :
    ApiResponse<T>(errorCode = errorCode, errorMsg = errorMsg)