package com.example.fuxingapp.model

data class Mensaje(
    val contenido: String = "",
    val enviadoPorUsuario: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)
