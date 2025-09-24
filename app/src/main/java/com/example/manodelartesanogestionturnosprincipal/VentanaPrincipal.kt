package com.example.manodelartesanogestionturnosprincipal

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class VentanaPrincipal : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventana_principal)

        auth = FirebaseAuth.getInstance()

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Inicialización de SharedPreferences
        preferences = getSharedPreferences("typeUser", MODE_PRIVATE)
        editor = preferences.edit()

        buttonLogin.setOnClickListener {
            val userEmail = inputEmail.text.toString()
            val userContrasena = inputPassword.text.toString()

            if (TextUtils.isEmpty(userEmail)) {
                Toast.makeText(this, "La casilla usuario está vacía", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(userContrasena)) {
                Toast.makeText(this, "La casilla contraseña está vacía", Toast.LENGTH_SHORT).show()
            }

            auth.signInWithEmailAndPassword(userEmail, userContrasena).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this@VentanaPrincipal, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    val intent: Intent = Intent(this@VentanaPrincipal, GestionAppTV::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    editor.putString("user", "Admin")
                    editor.apply()
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this@VentanaPrincipal, "Error: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    public override fun onStart(){
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null){
            val user: String = preferences.getString("user", "").toString()
            if (user == "Admin") {
                val intent: Intent = Intent(
                    this@VentanaPrincipal,
                    GestionAppTV::class.java
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
    }
}