package com.rwawrzyniak.getby

sealed class LoginResult {
    object Success : LoginResult()
    class Fail(val error: String) : LoginResult()
}