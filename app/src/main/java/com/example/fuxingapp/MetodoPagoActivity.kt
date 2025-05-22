package com.example.fuxingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import android.content.Intent

class MetodoPagoActivity : AppCompatActivity() {

    private lateinit var textViewOrderPrice: TextView
    private lateinit var textViewTotalPrice: TextView
    private lateinit var textViewFinalPriceValue: TextView
    private lateinit var constraintLayoutCreditCard: ConstraintLayout
    private lateinit var constraintLayoutDebitCard: ConstraintLayout
    private lateinit var radioButtonCreditCard: RadioButton
    private lateinit var radioButtonDebitCard: RadioButton
    private lateinit var textViewCreditCardLabel: TextView
    private lateinit var textViewCreditCardNumber: TextView
    private lateinit var textViewDebitCardLabel: TextView
    private lateinit var textViewDebitCardNumber: TextView
    private lateinit var buttonPayNow: Button
    private lateinit var cardViewAddCard: CardView

    private val TAXES = 0.50 // Impuesto fijo
    private val DELIVERY_FEE = 1.50 // Costo de delivery fijo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metodo_pago)

        textViewOrderPrice = findViewById(R.id.textViewOrderPrice)
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice)
        textViewFinalPriceValue = findViewById(R.id.textViewFinalPriceValue)
        constraintLayoutCreditCard = findViewById(R.id.constraintLayoutCreditCard)
        constraintLayoutDebitCard = findViewById(R.id.constraintLayoutDebitCard)
        radioButtonCreditCard = findViewById(R.id.radioButtonCreditCard)
        radioButtonDebitCard = findViewById(R.id.radioButtonDebitCard)
        textViewCreditCardLabel = findViewById(R.id.textViewCreditCardLabel)
        textViewCreditCardNumber = findViewById(R.id.textViewCreditCardNumber)
        textViewDebitCardLabel = findViewById(R.id.textViewDebitCardLabel)
        textViewDebitCardNumber = findViewById(R.id.textViewDebitCardNumber)
        buttonPayNow = findViewById(R.id.buttonPayNow)
        cardViewAddCard = findViewById(R.id.cardViewAddCard)

        val imageButtonBack: ImageButton = findViewById(R.id.imageButtonBack)

        // Configurar botón de retroceso
        imageButtonBack.setOnClickListener {
            onBackPressed()
        }

        // Obtener el precio del producto de la actividad anterior
        val productPrice = intent.getDoubleExtra("product_price", 0.0)

        // Actualizar el precio de la orden y calcular el total
        updatePrices(productPrice)

        // Configurar listeners para la selección de tarjetas
        constraintLayoutCreditCard.setOnClickListener {
            selectPaymentMethod(PaymentMethod.CREDIT_CARD)
        }

        constraintLayoutDebitCard.setOnClickListener {
            selectPaymentMethod(PaymentMethod.DEBIT_CARD)
        }

        // Configurar listeners para los RadioButtons para que también seleccionen el método de pago
        radioButtonCreditCard.setOnClickListener {
            selectPaymentMethod(PaymentMethod.CREDIT_CARD)
        }

        radioButtonDebitCard.setOnClickListener {
            selectPaymentMethod(PaymentMethod.DEBIT_CARD)
        }

        // Configurar listener para el botón pagar ahora
        buttonPayNow.setOnClickListener {
            val intent = Intent(this, PaymentNotificationActivity::class.java)
            startActivity(intent)
            finish() // Opcional: cierra MetodoPagoActivity
        }

        // Configurar listener para la vista de agregar tarjeta
        cardViewAddCard.setOnClickListener {
            val intent = Intent(this, AgregarTarjetaActivity::class.java)
            startActivity(intent)
        }

        // Seleccionar la tarjeta de crédito por defecto al iniciar
        selectPaymentMethod(PaymentMethod.CREDIT_CARD)

        // Aquí se añadiría la lógica para guardar datos de tarjeta
    }

    private fun updatePrices(productPrice: Double) {
        textViewOrderPrice.text = String.format("S/.%.2f", productPrice)
        val total = productPrice + TAXES + DELIVERY_FEE
        textViewTotalPrice.text = String.format("S/.%.2f", total)
        textViewFinalPriceValue.text = String.format("S/.%.2f", total)
    }

    private fun selectPaymentMethod(method: PaymentMethod) {
        when (method) {
            PaymentMethod.CREDIT_CARD -> {
                constraintLayoutCreditCard.setBackgroundColor(Color.parseColor("#424242"))
                constraintLayoutDebitCard.setBackgroundColor(Color.parseColor("#F5F5F5"))
                radioButtonCreditCard.isChecked = true
                radioButtonDebitCard.isChecked = false
                textViewCreditCardLabel.setTextColor(Color.WHITE)
                textViewCreditCardNumber.setTextColor(Color.WHITE)
                textViewDebitCardLabel.setTextColor(Color.BLACK)
                textViewDebitCardNumber.setTextColor(Color.BLACK)
            }
            PaymentMethod.DEBIT_CARD -> {
                constraintLayoutCreditCard.setBackgroundColor(Color.parseColor("#F5F5F5"))
                constraintLayoutDebitCard.setBackgroundColor(Color.parseColor("#424242"))
                radioButtonCreditCard.isChecked = false
                radioButtonDebitCard.isChecked = true
                textViewCreditCardLabel.setTextColor(Color.BLACK)
                textViewCreditCardNumber.setTextColor(Color.BLACK)
                textViewDebitCardLabel.setTextColor(Color.WHITE)
                textViewDebitCardNumber.setTextColor(Color.WHITE)
            }
        }
    }

    enum class PaymentMethod {
        CREDIT_CARD,
        DEBIT_CARD
    }
} 