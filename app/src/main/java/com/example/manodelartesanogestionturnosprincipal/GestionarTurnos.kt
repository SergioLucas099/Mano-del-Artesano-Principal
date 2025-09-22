package com.example.manodelartesanogestionturnosprincipal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.manodelartesanogestionturnosprincipal.Adapter.AtraccionAdapter
import com.example.manodelartesanogestionturnosprincipal.Adapter.VerAtraccionAdapter
import com.example.manodelartesanogestionturnosprincipal.Model.AtraccionModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GestionarTurnos : AppCompatActivity() {

    private lateinit var listaAtracciones : ArrayList<AtraccionModel>
    private lateinit var txtTiempo: TextView
    var contador = 1
    var tiempoTotal = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestionar_turnos)

        val databaseReference = FirebaseDatabase.getInstance().getReference("Atracciones")
        val atrasGestionTurnos = findViewById<ImageView>(R.id.atrasGestionTurnos)
        val RevVerAtraccion = findViewById<RecyclerView>(R.id.RevVerAtraccion)
        val AtraccionSeleccionada = findViewById<TextView>(R.id.AtraccionSeleccionada)
        val disminuir = findViewById<ImageView>(R.id.disminuir)
        val ContadorPersonas = findViewById<TextView>(R.id.ContadorPersonas)
        val edtxNumero = findViewById<TextInputEditText>(R.id.edtxNumero)
        val agregar = findViewById<ImageView>(R.id.agregar)
        txtTiempo = findViewById(R.id.txtTiempo)
        val btnSubirInfo = findViewById<Button>(R.id.btnSubirInfo)

        var valorId = ""
        var valorNombre = ""
        var valorTurno = ""


        fun actualizarTiempo(contador: Int): String {
            val tiempoTotal = contador * 20 // segundos totales
            val minutos = tiempoTotal / 60
            val segundos = tiempoTotal % 60
            return String.format("%02d:%02d", minutos, segundos) // devuelve en formato mm:ss
        }

        fun mostrarTiempo(contador: Int): String {
            val tiempoTotal = contador * 20
            return if (tiempoTotal < 60) {
                "${actualizarTiempo(contador)} seg"
            } else {
                "${actualizarTiempo(contador)} min"
            }
        }

        atrasGestionTurnos.setOnClickListener {
            val intent: Intent = Intent(this@GestionarTurnos, GestionAppTV::class.java)
            startActivity(intent)
            finish()
        }

        disminuir.setOnClickListener {
            if (contador > 1) {
                contador--
                ContadorPersonas.text = contador.toString()
            } else {
                contador = 1
                ContadorPersonas.text = "1"
            }
            txtTiempo.text = "Tiempo: ${mostrarTiempo(contador)}"
        }

        agregar.setOnClickListener {
            contador++
            ContadorPersonas.text = contador.toString()
            txtTiempo.text = "Tiempo: ${mostrarTiempo(contador)}"
        }

        RevVerAtraccion.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        listaAtracciones = arrayListOf<AtraccionModel>()
        RevVerAtraccion.visibility = View.GONE
        FirebaseDatabase.getInstance().reference.child("Atracciones")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot){
                    listaAtracciones.clear()
                    if (snapshot.exists()){
                        for (Snap in snapshot.children){
                            val data = Snap.getValue(AtraccionModel::class.java)
                            listaAtracciones.add(data!!)
                        }
                    }

                    val adapter = VerAtraccionAdapter(listaAtracciones){ textoSeleccionado ->
                        AtraccionSeleccionada.setText(textoSeleccionado.Nombre)
                        valorId = textoSeleccionado.Id.toString()
                        valorNombre = textoSeleccionado.Nombre.toString()
                        valorTurno = textoSeleccionado.Turno.toString()
                    }
                    RevVerAtraccion.adapter = adapter
                    RevVerAtraccion.visibility = View.VISIBLE
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        btnSubirInfo.setOnClickListener {
            val BD = FirebaseDatabase.getInstance().getReference("TurnosEnEspera")
            val numeroTelefonico = edtxNumero.text?.toString()?.trim() ?: ""
            val IdTurnoE = BD.push().key.toString()
            val nombreAtraccion = AtraccionSeleccionada.text.toString()

            if (nombreAtraccion == "Nada Selecionado") {
                Toast.makeText(this, "Primero seleccionar una atracción", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val atraccionRef = FirebaseDatabase.getInstance()
                .getReference("Atracciones")
                .child(valorId)

            atraccionRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val ultimoTurno = snapshot.child("Turno").getValue(Int::class.java) ?: 0
                    val nuevoTurno = ultimoTurno + 1

                    // Aquí ya tienes el turno asignado
                    val turnoFormateado = String.format("%04d", nuevoTurno) // ejemplo: 0001, 0002

                    val map: MutableMap<String, Any> = HashMap()
                    map["Id"] = IdTurnoE
                    map["Atraccion"] = valorNombre
                    map["TurnoAsignado"] = turnoFormateado
                    map["NumeroPersonas"] = ContadorPersonas.text.toString()
                    map["NumeroTelefonico"] = numeroTelefonico
                    map["Tiempo"] = actualizarTiempo(contador)

                    BD.child(valorNombre).child(IdTurnoE).setValue(map).addOnSuccessListener {
                        AtraccionSeleccionada.setText("Nada Selecionado")
                        edtxNumero.setText("")
                        ContadorPersonas.text = "1"
                        txtTiempo.text = "Tiempo: 00:20 seg"
                        contador = 1
                        Toast.makeText(this, "Turno asignado: $turnoFormateado", Toast.LENGTH_SHORT).show()
                    }

                    // Guardar nuevo turno en Firebase
                    atraccionRef.child("Turno").setValue(nuevoTurno)
                } else {
                    // Si la atracción no tiene campo UltimoTurno aún, se crea con 1
                    atraccionRef.child("Turno").setValue(1)
                }
            }
        }
    }
}

//CONTENIDO NUEVO DEL PROYECTO