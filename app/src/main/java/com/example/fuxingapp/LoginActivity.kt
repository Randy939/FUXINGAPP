package com.example.fuxingapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient

    private val RC_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val createAccountButton: Button = findViewById(R.id.createAccountButton)
        val googleButton: Button = findViewById(R.id.googleButton)
        val rememberCheckBox = findViewById<CheckBox>(R.id.rememberMeCheckBox)

        // SharedPreferences
        val sharedPref = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPref.getString("email", "")
        val savedPassword = sharedPref.getString("password", "")
        emailEditText.setText(savedEmail)
        passwordEditText.setText(savedPassword)
        rememberCheckBox.isChecked = savedEmail!!.isNotEmpty() && savedPassword!!.isNotEmpty()

        // Configuración de Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                // Guardar en SharedPreferences si está marcado
                if (rememberCheckBox.isChecked) {
                    sharedPref.edit().apply {
                        putString("email", email)
                        putString("password", password)
                        apply()
                    }
                } else {
                    sharedPref.edit().clear().apply()
                }

                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        val userId = auth.currentUser?.uid ?: return@addOnSuccessListener
                        db.collection("Usuarios").document(userId).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val rol = document.getString("rol")
                                    when (rol) {
                                        "admin", "cliente" -> startActivity(Intent(this, MainActivity::class.java))
                                        else -> Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                                    }
                                    finish()
                                } else {
                                    Toast.makeText(this, "No se encontró el usuario en la base de datos", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error al verificar el rol", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error en el inicio de sesión: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        googleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        createAccountButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                auth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        val userId = auth.currentUser?.uid ?: return@addOnSuccessListener
                        val userDoc = db.collection("Usuarios").document(userId)

                        userDoc.get().addOnSuccessListener { document ->
                            if (!document.exists()) {
                                val newUser = hashMapOf(
                                    "nombre" to account.displayName,
                                    "correo" to account.email,
                                    "rol" to "cliente"
                                )
                                userDoc.set(newUser)
                            }
                            val intent = Intent(this, LoadingActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error con Google: ${it.message}", Toast.LENGTH_LONG).show()
                    }

            } catch (e: ApiException) {
                Log.e("Login", "Google sign-in failed", e)
                Toast.makeText(this, "Fallo en login con Google", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
