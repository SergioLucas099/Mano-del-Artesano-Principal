package com.example.manodelartesanogestionturnosprincipal

import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class DetallesAtraccion : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_atraccion)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val atrasDetallesAtraccion = findViewById<ImageView>(R.id.atrasDetallesAtraccion)
        val txtDetNomAtrac = findViewById<TextView>(R.id.txtDetNomAtrac)
        val txtDetTiempAtrac = findViewById<TextView>(R.id.txtDetTiempAtrac)
        val etTurnoAtraccion = findViewById<EditText>(R.id.etTurnoAtraccion)
        val btnActualizarAtracc = findViewById<Button>(R.id.btnActualizarAtracc)

        // Obtener datos Adapter
        val Nombre = intent.getStringExtra("Nombre")
        val Tiempo = intent.getStringExtra("Tiempo")
        val Turno = intent.getStringExtra("Turno")

        atrasDetallesAtraccion.setOnClickListener { finish() }

        txtDetNomAtrac.setText(Nombre)
        txtDetTiempAtrac.setText("$Tiempo seg")
        etTurnoAtraccion.setText(Turno)

        btnActualizarAtracc.setOnClickListener {

            val nuevoTurno = etTurnoAtraccion.text.toString().toIntOrNull() ?: 0

            val map: MutableMap<String, Any> = HashMap()
            map["Nombre"] = Nombre.toString()
            map["Tiempo"] = 20
            map["Turno"] = nuevoTurno

            FirebaseDatabase.getInstance().getReference("Atracciones")
                .child(Nombre.toString())
                .setValue(map).addOnCompleteListener {
                    Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            finish()
        }
    }
}