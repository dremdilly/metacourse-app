package com.example.metacourse.network

data class AuthResponse(
    val jwtAuthResponse: JWTAuthResponse,
    val person: PersonModel
)