package com.example.whitelistverification.backend.dto

data class UserDto(
    var email: String = "",
    var nickname: String = "",
    var code: String? = null
)
