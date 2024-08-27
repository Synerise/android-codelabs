package com.synerise.integration.app.main.model

import com.synerise.sdk.error.ApiError

sealed class SyneriseSdkResult<out T>(val status: Status, val data: T?, val error: ApiError?) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    data class Success<out R>(val _data: R?) :
        SyneriseSdkResult<R>(
            status = Status.SUCCESS,
            data = _data,
            error = null
        )

    data class Error(val apiError: ApiError) : SyneriseSdkResult<Nothing>(
        status = Status.ERROR,
        data = null,
        error = apiError
    )

    data class Loading<out R>(val _data: R?) : SyneriseSdkResult<R>(
        status = Status.LOADING,
        data = _data,
        error = null
    )
}