package com.example.portable.model

class LoginResponse (
    val status: Boolean, // Armazena o status da operação.
    val userId: Int?,
    val nome: String?,
    val error: String?
)
