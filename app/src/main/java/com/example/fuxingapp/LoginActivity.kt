package com.example.fuxingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import androidx.core.content.ContextCompat
import com.example.fuxingapp.RegisterActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            // Aquí deberías añadir tu lógica real de validación de login
            // Por ahora, solo llamamos a onLoginSuccess directamente para probar la navegación
            onLoginSuccess()
        }

        val createAccountButton: Button = findViewById(R.id.createAccountButton)
        createAccountButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onLoginSuccess() {
        // Crea un Intent para iniciar LoadingActivity
        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)
        finish() // Opcional: cierra LoginActivity para que el usuario no pueda volver atrás con el botón de retroceso
    }
}

