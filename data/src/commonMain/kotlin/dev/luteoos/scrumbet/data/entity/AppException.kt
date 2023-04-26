package dev.luteoos.scrumbet.data.entity

sealed class AppException(val code: Int = 0, message: String? = null) : Throwable(message) {
    class GeneralException(message: String? = null) : Throwable(message)
    class HttpException(code: String? = null) : Throwable(code)
}
