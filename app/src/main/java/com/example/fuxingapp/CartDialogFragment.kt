package com.example.fuxingapp

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent // Importar Intent

class CartDialogFragment : DialogFragment() {

    private lateinit var recyclerViewCartItems: RecyclerView
    private lateinit var textViewTotalPrice: TextView
    private lateinit var buttonCloseCart: Button
    private lateinit var buttonProceedToPayment: Button
    private lateinit var cartAdapter: CartAdapter // Necesitaremos crear este adaptador

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del diálogo
        return inflater.inflate(R.layout.dialog_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar vistas
        recyclerViewCartItems = view.findViewById(R.id.recyclerViewCartItems)
        textViewTotalPrice = view.findViewById(R.id.textViewTotalPrice)
        buttonCloseCart = view.findViewById(R.id.buttonCloseCart)
        buttonProceedToPayment = view.findViewById(R.id.buttonProceedToPayment)

        // Configurar RecyclerView
        recyclerViewCartItems.layoutManager = LinearLayoutManager(context)

        // Cargar items del carrito
        val cartItems = context?.let { CartManager.getCartItems(it) } ?: emptyList()

        // Configurar adaptador
        // Necesitaremos un CartAdapter que tome una lista de CartItem
        cartAdapter = CartAdapter(cartItems) {
            // Lógica para eliminar item del carrito
            context?.let { ctx ->
                CartManager.removeItemFromCart(ctx, it) // 'it' es el CartItem a eliminar
                // Actualizar la lista y el total en el diálogo
                updateCartUI()
            }
        }
        recyclerViewCartItems.adapter = cartAdapter

        // Calcular y mostrar total
        updateTotal(cartItems) // Usar una función para actualizar el total

        // Configurar listeners de botones
        buttonCloseCart.setOnClickListener {
            dismiss() // Cerrar el diálogo
        }

        buttonProceedToPayment.setOnClickListener {
            // Navegar a la actividad de método de pago
            val intent = Intent(activity, MetodoPagoActivity::class.java)
            // Puedes pasar datos del carrito a la actividad de pago si es necesario
            // por ejemplo: intent.putExtra("cart_items", ArrayList(cartItems))
            val total = cartItems.sumOf { it.quantity * it.price } // Recalcular total antes de pasar
            intent.putExtra("cart_total", total) // Pasar el total a la actividad de pago
            startActivity(intent)
            dismiss() // Cerrar el diálogo después de navegar
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        // Opcional: configurar estilo del diálogo si es necesario
        // dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    private fun updateCartUI() {
        val cartItems = context?.let { CartManager.getCartItems(it) } ?: emptyList()
        cartAdapter = CartAdapter(cartItems) { // Volver a pasar la lambda al crear el nuevo adaptador
            context?.let { ctx ->
                CartManager.removeItemFromCart(ctx, it)
                updateCartUI()
            }
        }
        recyclerViewCartItems.adapter = cartAdapter
        updateTotal(cartItems)
    }

    private fun updateTotal(cartItems: List<CartItem>) {
        val total = cartItems.sumOf { it.quantity * it.price }
        textViewTotalPrice.text = String.format("Total: S/.%.2f", total)
    }
} 