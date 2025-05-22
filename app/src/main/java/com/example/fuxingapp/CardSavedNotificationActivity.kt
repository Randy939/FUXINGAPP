package com.example.fuxingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CardSavedNotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_saved_notification)

        val buttonContinue: Button = findViewById(R.id.buttonContinue)
        buttonContinue.setOnClickListener {
            finish() // Cierra esta actividad y regresa a la anterior (AgregarTarjetaActivity)
        }
    }
} 