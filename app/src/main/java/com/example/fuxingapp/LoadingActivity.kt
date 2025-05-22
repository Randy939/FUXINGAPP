package com.example.fuxingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // Duración de la pantalla de carga en milisegundos
        val loadingDuration = 3000L // 3 segundos

        Handler(Looper.getMainLooper()).postDelayed({
            // Crear un Intent para iniciar la siguiente actividad
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cierra LoadingActivity para que el usuario no pueda volver atrás
        }, loadingDuration)
    }
} 