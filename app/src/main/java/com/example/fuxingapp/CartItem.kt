package com.example.fuxingapp.model

import java.io.Serializable

data class CartItem(
    var id: String = "",
    var nombre: String = "",
    var imagenUrl: String = "",
    var precioUnitario: Double = 0.0,
    var cantidad: Int = 1
) : Serializable {
    val precioTotal: Double
        get() = precioUnitario * cantidad
}
