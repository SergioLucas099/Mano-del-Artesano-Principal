package com.example.manodelartesanogestionturnosprincipal

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class GestionAppTV : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var agregarUsuario: LinearLayout
    private lateinit var subirVideos: LinearLayout
    private lateinit var subirTexto: LinearLayout
    private lateinit var crearAtraccion: LinearLayout
    private lateinit var reiniciarTurnos: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_app_tv)

        auth = FirebaseAuth.getInstance()
        val BotonSalir = findViewById<ImageView>(R.id.BotonSalir)
        agregarUsuario = findViewById(R.id.agregarUsuario)
        subirVideos = findViewById(R.id.subirVideos)
        subirTexto = findViewById(R.id.subirTexto)
        crearAtraccion = findViewById(R.id.crearAtraccion)
        reiniciarTurnos = findViewById(R.id.reiniciarTurnos)

        BotonSalir.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cerrar Sesión")
            builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")

            builder.setPositiveButton("Cerrar sesión") { dialog, _ ->
                cerrarSesion()
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

        agregarUsuario.setOnClickListener {
            Toast.makeText(this, "Proximamente...", Toast.LENGTH_SHORT).show()
        }

        subirVideos.setOnClickListener {
            //val intent: Intent = Intent(this@GestionAppTV, SubirVideoFirebase::class.java)
            val intent: Intent = Intent(this@GestionAppTV, SubirVideoFirebase::class.java)
            startActivity(intent)
            finish()
        }

        subirTexto.setOnClickListener {
            val intent: Intent = Intent(this@GestionAppTV, SubirTextoFirebase::class.java)
            startActivity(intent)
            finish()
        }

        crearAtraccion.setOnClickListener {
            val intent: Intent = Intent(this@GestionAppTV, CrearAtraccion::class.java)
            startActivity(intent)
            finish()
        }

        reiniciarTurnos.setOnClickListener {
            val intent: Intent = Intent(this@GestionAppTV, ReiniciarTurnos::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun cerrarSesion() {
        auth.signOut()
        val intent = Intent(this, VentanaPrincipal::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}