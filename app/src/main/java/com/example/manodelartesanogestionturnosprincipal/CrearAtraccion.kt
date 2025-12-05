package com.example.manodelartesanogestionturnosprincipal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.manodelartesanogestionturnosprincipal.Adapter.AtraccionAdapter
import com.example.manodelartesanogestionturnosprincipal.Model.AtraccionModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.widget.NumberPicker
import android.widget.TextView

class CrearAtraccion : AppCompatActivity() {

    private lateinit var listaAtracciones : ArrayList<AtraccionModel>
    private lateinit var numberPickerMinutes: NumberPicker
    private lateinit var numberPickerSeconds: NumberPicker
    private lateinit var AvisoSinAtracciones: LinearLayout
    var totalSeconds = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_atraccion)

        val databaseReference = FirebaseDatabase.getInstance().getReference("Atracciones")
        val atrasCrearTurnos = findViewById<ImageView>(R.id.atrasCrearTurnos)
        val editTextNAtraccion = findViewById<EditText>(R.id.editTextNAtraccion)
        val btnSubirInfo = findViewById<Button>(R.id.btnSubirInfo)
        val RevListaAtraccion = findViewById<RecyclerView>(R.id.RevListaAtraccion)
        AvisoSinAtracciones = findViewById(R.id.AvisoSinAtracciones)

        numberPickerMinutes = findViewById(R.id.numberPickerMinutes)
        numberPickerSeconds = findViewById(R.id.numberPickerSeconds)

        // Configuración del picker de minutos (0 a 59)
        numberPickerMinutes.minValue = 0
        numberPickerMinutes.maxValue = 59

        // Configuración del picker de segundos (0 a 59)
        numberPickerSeconds.minValue = 0
        numberPickerSeconds.maxValue = 59

        atrasCrearTurnos.setOnClickListener {
            val intent: Intent = Intent(this@CrearAtraccion, GestionAppTV::class.java)
            startActivity(intent)
            finish()
        }

        btnSubirInfo.setOnClickListener {
            Toast.makeText(this, "Función (Crear Atracción) disponible en próximas actualizaciones", Toast.LENGTH_LONG).show()
            /*
            val info = editTextNAtraccion.text.toString().trim() // quitamos espacios

            // Obtener Tiempo
            val minutes = numberPickerMinutes.value
            val seconds = numberPickerSeconds.value
            totalSeconds = (minutes * 60) + seconds

            if (info.isNotEmpty()) {
                FirebaseDatabase.getInstance().reference.child("Atracciones")
                    .orderByChild("Nombre").equalTo(info)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            Toast.makeText(this, "La atracción '$info' ya existe", Toast.LENGTH_SHORT).show()
                        } else {
                            val map: MutableMap<String, Any> = HashMap()
                            map["Nombre"] = info
                            map["Turno"] = 0
                            map["Tiempo"] = totalSeconds

                            databaseReference.child(info)
                                .setValue(map)
                                .addOnCompleteListener {
                                    FirebaseDatabase.getInstance()
                                        .getReference("Tiempo_Acumulado")
                                        .child("TiempoAcumulado_${info}")
                                        .setValue("0")
                                    FirebaseDatabase.getInstance()
                                        .getReference("TurnosActualesAtracciones")
                                        .child(info).setValue("0000")
                                    Toast.makeText(this, "Nueva atracción '$info' creada con éxito", Toast.LENGTH_SHORT).show()
                                    editTextNAtraccion.setText("")
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Error al subir la información", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al verificar atracción", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "No se debe dejar la casilla de texto vacía", Toast.LENGTH_SHORT).show()
            }
             */
        }

        RevListaAtraccion.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        listaAtracciones = arrayListOf<AtraccionModel>()
        RevListaAtraccion.visibility = View.GONE
        FirebaseDatabase.getInstance().reference.child("Atracciones")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot){
                    listaAtracciones.clear()
                    if (snapshot.exists()){
                        AvisoSinAtracciones.visibility = View.GONE
                        for (Snap in snapshot.children){
                            val data = Snap.getValue(AtraccionModel::class.java)
                            listaAtracciones.add(data!!)
                        }
                    }else{
                        AvisoSinAtracciones.visibility = View.VISIBLE
                    }

                    val adapter = AtraccionAdapter(listaAtracciones)
                    RevListaAtraccion.adapter = adapter
                    RevListaAtraccion.visibility = View.VISIBLE
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}