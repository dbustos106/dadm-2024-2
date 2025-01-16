package com.example.reto_8.model

sealed class RoomResponse<out T> {
    data class Success<T>(val data: T): RoomResponse<T>()
    data class Error(val errorMessage: String): RoomResponse<Nothing>()
}
