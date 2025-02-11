package com.example.seekhoassignment.utils.network

import java.lang.Exception

sealed class ApiState<out R> {
    data class Success<out T>(val data: T) : ApiState<T>()
    data class Error(val message:String ,val exception: Exception) : ApiState<Nothing>()
    data object Loading : ApiState<Nothing>()
}