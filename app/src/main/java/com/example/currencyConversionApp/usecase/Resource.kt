package com.nmc.myapplication.usecase

import com.nmc.myapplication.api.retrofit.service.Error

enum class Status {
    SUCCESS,
    ERROR
}

// Fetch error code
const val NOT_FOUND = 404
const val MISSING_APP_ID = 401
const val INVALID_APP_ID = 401
const val NOT_ALLOWED = 429
const val ACCESS_RESTRICTED = 403
const val INVALID_BASE = 400

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val errorCode: Int?,
    var errorMessage: String? = null
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(errorCode: Int? = 999, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, errorCode, getErrorMessage(errorCode, data))
        }

        private fun <T> getErrorMessage(errorCode: Int?, data: T?): String? =
            when ((data as? Error)?.status) {
                NOT_FOUND,
                MISSING_APP_ID,
                NOT_ALLOWED,
                ACCESS_RESTRICTED,
                INVALID_BASE,
                INVALID_APP_ID -> (data as? Error)?.description
                else -> "An error occurred while attempting your request."
            }

    }
}