package com.example.fuxingapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var addressEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var buttonEditProfile: Button
    private lateinit var buttonLogout: Button
    private lateinit var buttonBack: ImageButton
    private lateinit var phoneEditText: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Enlazar vistas
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        addressEditText = findViewById(R.id.deliveryAddressEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        buttonEditProfile = findViewById(R.id.buttonEditProfile)
        buttonLogout = findViewById(R.id.buttonLogout)
        buttonBack = findViewById(R.id.imageButtonBack)
        phoneEditText = findViewById(R.id.phoneEditText)

        val userId = auth.currentUser?.uid ?: return

        // Cargar datos del usuario
        db.collection("Usuarios").document(userId).get()
            .addOnSuccessListener { document ->
                nameEditText.setText(document.getString("nombre") ?: "")
                emailEditText.setText(auth.currentUser?.email ?: "")
                addressEditText.setText(document.getString("direccion") ?: "")
                passwordEditText.setText(document.getString("password") ?: "")
                phoneEditText.setText(document.getString("telefono") ?: "")

            }

        // Botón Editar Perfil
        buttonEditProfile.setOnClickListener {
            val nombre = nameEditText.text.toString()
            val direccion = addressEditText.text.toString()
            val password = passwordEditText.text.toString()
            val telefono = phoneEditText.text.toString()

            val datos = mapOf(
                "nombre" to nombre,
                "direccion" to direccion,
                "telefono" to telefono
            )

            // Guardar datos en Firestore
            db.collection("Usuarios").document(userId).update(datos)
                .addOnSuccessListener {
                    Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()

                    // Cambiar contraseña si corresponde
                    if (password.isNotEmpty()) {
                        auth.currentUser?.updatePassword(password)
                            ?.addOnSuccessListener {
                                Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                            }
                            ?.addOnFailureListener {
                                Toast.makeText(this, "Debes volver a iniciar sesión para cambiar la contraseña", Toast.LENGTH_LONG).show()
                            }
                    }

                    // Volver al menú principal
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
        }


        // Botón Cerrar Sesión
        buttonLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Botón Regresar
        buttonBack.setOnClickListener {
            finish()
        }
    }
}
