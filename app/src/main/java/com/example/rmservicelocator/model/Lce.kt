package com.example.rmservicelocator.model

sealed class Lce<out T> {
    object Loading : Lce<Nothing>()
    data class Success<T>(val data: T) : Lce<T>()
    data class Fail(val error: Throwable) : Lce<Nothing>()
}