package com.rwawrzyniak.getby.register

sealed class RegisterResult {
    object Default : RegisterResult()
    object Success : RegisterResult()
    data class Fail(val error: String) : RegisterResult()
}