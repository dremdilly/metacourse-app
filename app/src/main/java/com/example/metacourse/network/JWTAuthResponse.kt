package com.example.metacourse.network

data class JWTAuthResponse (
    val accessToken: String,
    val tokenType: String
)