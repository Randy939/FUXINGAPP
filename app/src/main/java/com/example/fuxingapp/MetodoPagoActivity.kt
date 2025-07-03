package com.example.fuxingapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MetodoPagoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metodo_pago)

        val subtotal = intent.getDoubleExtra("subtotal", 0.0)

        // Impuestos y delivery fijos
        val impuesto = 0.50
        val delivery = 1.50
        val total = subtotal + impuesto + delivery

        // Referencias a los TextView
        val textOrder = findViewById<TextView>(R.id.textViewOrderPrice)
        val textTaxes = findViewById<TextView>(R.id.textViewTaxesPrice)
        val textDelivery = findViewById<TextView>(R.id.textViewDeliveryPrice)
        val textTotal = findViewById<TextView>(R.id.textViewTotalPrice)
        val textFinal = findViewById<TextView>(R.id.textViewFinalPriceValue)
        val layoutCredit = findViewById<ConstraintLayout>(R.id.constraintLayoutCreditCard)
        val layoutDebit = findViewById<ConstraintLayout>(R.id.constraintLayoutDebitCard)
        val radioCredit = findViewById<RadioButton>(R.id.radioButtonCreditCard)
        val radioDebit = findViewById<RadioButton>(R.id.radioButtonDebitCard)

        fun actualizarColoresSeleccion(tipo: String) {
            if (tipo == "credito") {
                layoutCredit.setBackgroundColor(Color.parseColor("#424242"))
                layoutDebit.setBackgroundColor(Color.parseColor("#F5F5F5"))
                radioCredit.isChecked = true
                radioDebit.isChecked = false
            } else {
                layoutDebit.setBackgroundColor(Color.parseColor("#F5F5F5"))
                layoutCredit.setBackgroundColor(Color.parseColor("#424242"))
                radioCredit.isChecked = false
                radioDebit.isChecked = true
            }
        }

        // Listeners para selección por radio
        radioCredit.setOnClickListener {
            actualizarColoresSeleccion("credito")
        }
        radioDebit.setOnClickListener {
            actualizarColoresSeleccion("debito")
        }

// Listeners para selección por tocar el layout
        layoutCredit.setOnClickListener {
            actualizarColoresSeleccion("credito")
        }
        layoutDebit.setOnClickListener {
            actualizarColoresSeleccion("debito")
        }

        // Mostrar valores
        textOrder.text = "S/. %.2f".format(subtotal)
        textTaxes.text = "S/. %.2f".format(impuesto)
        textDelivery.text = "S/. %.2f".format(delivery)
        textTotal.text = "S/. %.2f".format(total)
        textFinal.text = "S/. %.2f".format(total)

        // Botón para volver atrás
        val btnBack = findViewById<ImageButton>(R.id.imageButtonBack)
        btnBack.setOnClickListener {
            finish()
        }

        // Botón de pagar (acción temporal)
        val btnPayNow = findViewById<Button>(R.id.buttonPayNow)
        btnPayNow.setOnClickListener {
            val intent = Intent(this, PaymentNotificationActivity::class.java)
            startActivity(intent)
        }
    }
}
