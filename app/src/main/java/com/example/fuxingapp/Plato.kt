package com.example.fuxingapp
import java.io.Serializable

data class Plato(
    var id: String = "",
    var nombre: String = "",
    var descripcion: String = "",
    var categoria: String = "",
    var imagenUrl: String = "",
    var rating: Float = 0f,
    var precio: Double = 0.0
) : Serializable