package dev.luteoos.scrumbet.data.entity

sealed class AppException(message: String? = null) : Exception(message) {
    class GeneralException(message: String? = null) : AppException(message)
    class HttpException(val code: Int, message: String?) : AppException(message)
}
