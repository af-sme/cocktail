package de.appsfactory.cocktail

sealed class Result<out T>

class Success<out T>(val value: T) : Result<T>()

class Error(
    val reason: Reason = Reason.UNKNOWN,
    val throwable: Throwable
) : Result<Nothing>() {
    enum class Reason {
        NO_INTERNET,
        API_ERROR,
        API_MODEL_PARSING,
        UNKNOWN
    }
}