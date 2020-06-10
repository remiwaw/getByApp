package com.rwawrzyniak.getby.authorization

sealed class RegisterResult {
    object Default : RegisterResult()
    object Success : RegisterResult()
    data class Fail(val error: String) : RegisterResult()
}
