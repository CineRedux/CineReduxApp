package com.example.cineredux_v2.models




data class User(
    val id: Int,
    val name: String,
    val surname: String, // Changed `string` to `String`
    val username: String,
    val email: String,
    val phone: String,
    val password: String
)
