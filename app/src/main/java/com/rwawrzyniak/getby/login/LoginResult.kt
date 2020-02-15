package com.rwawrzyniak.getby.login

sealed class LoginResult {
    object Default : LoginResult()
    data class Success(val userName: String) : LoginResult()
    data class Fail(val error: String) : LoginResult()
}
