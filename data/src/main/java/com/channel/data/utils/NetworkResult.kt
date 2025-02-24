package com.channel.data.utils

sealed class NetworkResult<out T : Any> {
    data object Idle : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()
    data class Success<T : Any>(val data: T) : NetworkResult<T>()
    data class Error(val code: Int, val message: String?) : NetworkResult<Nothing>()
    data class Exception(val e: Throwable, val errorMessage: String) : NetworkResult<Nothing>()
}

