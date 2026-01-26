package com.example.portable.model

class LoginResponse (
    val success: Boolean, // Armazena o status da operação.
    val userId: Int?,
    val nome: String?,
    val error: String?
)
