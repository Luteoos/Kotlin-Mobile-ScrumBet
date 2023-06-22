package dev.luteoos.scrumbet.data.entity

sealed class AppException(message: String? = null) : Throwable(message) {
    class GeneralException(message: String? = null) : AppException(message)
    class HttpException(code: String? = null) : AppException(code)
}
