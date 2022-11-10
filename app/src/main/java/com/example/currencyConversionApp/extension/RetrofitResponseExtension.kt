package com.nmc.myapplication.extension

import com.nmc.myapplication.usecase.MISSING_APP_ID
import com.nmc.myapplication.usecase.NOT_ALLOWED
import com.nmc.myapplication.usecase.Resource
import retrofit2.Response
import java.net.HttpURLConnection

fun <T> Response<T>.validateResponse(): Resource<T> =
    when {
        !isSuccessful -> handleHttpError(body())
        else -> Resource.success(body())
    }

fun <T> Response<T>.handleHttpError(body: T): Resource<T> =
    when (code()) {
        HttpURLConnection.HTTP_NOT_MODIFIED,
        HttpURLConnection.HTTP_INTERNAL_ERROR,
        HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> {
            Resource.error(NOT_ALLOWED)
        }
        HttpURLConnection.HTTP_UNAUTHORIZED,
        HttpURLConnection.HTTP_FORBIDDEN -> {
            Resource.error(MISSING_APP_ID)
        }
        else -> {
            Resource.error(code(), body)
        }
    }