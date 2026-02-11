package com.example.manodelartesanogestionturnosprincipal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.manodelartesanogestionturnosprincipal.Adapter.FinalizarTurnosAdapter
import com.example.manodelartesanogestionturnosprincipal.Model.FinalizarTurnosModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReiniciarTurnos : AppCompatActivity() {

    private lateinit var listaTurnosFinalizados : ArrayList<FinalizarTurnosModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reiniciar_turnos)

        val atrasReinicio = findViewById<ImageView>(R.id.atrasReinicio)
        val RevTurnosAcumulados = findViewById<RecyclerView>(R.id.RevTurnosAcumulados)
        val AvisoSinTurnosAcumulados = findViewById<LinearLayout>(R.id.AvisoSinTurnosAcumulados)
        val btnReiniciar = findViewById<Button>(R.id.btnReiniciar)

        atrasReinicio.setOnClickListener {
            val intent: Intent = Intent(this@ReiniciarTurnos, GestionAppTV::class.java)
            startActivity(intent)
            finish()
        }

        AvisoSinTurnosAcumulados.visibility = View.GONE

        RevTurnosAcumulados.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        listaTurnosFinalizados = arrayListOf<FinalizarTurnosModel>()
        RevTurnosAcumulados.visibility = View.GONE
        FirebaseDatabase.getInstance().reference.child("TurnosAcumulados")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot){
                    listaTurnosFinalizados.clear()
                    if (snapshot.exists()){
                        AvisoSinTurnosAcumulados.visibility = View.GONE
                        for (Snap in snapshot.children){
                            val data = Snap.getValue(FinalizarTurnosModel::class.java)
                            listaTurnosFinalizados.add(data!!)
                        }
                    }else{
                        AvisoSinTurnosAcumulados.visibility = View.VISIBLE
                    }

                    val adapter = FinalizarTurnosAdapter(listaTurnosFinalizados)
                    RevTurnosAcumulados.adapter = adapter
                    RevTurnosAcumulados.visibility = View.VISIBLE
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        btnReiniciar.setOnClickListener {

            val dialog = AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿Qué deseas hacer?")
                .setCancelable(false)
                .setNegativeButton("Cancelar") { _, _ ->
                    Toast.makeText(this, "Acción Cancelada", Toast.LENGTH_SHORT).show()
                }
                .setPositiveButton("Aceptar") { _, _ ->

                    FirebaseDatabase.getInstance().getReference("Atracciones")
                        .child("Mano Del Artesano")
                        .child("Turno")
                        .setValue(0)

                    FirebaseDatabase.getInstance().getReference("TiempoAcumulado")
                        .setValue("00:00")

                    FirebaseDatabase.getInstance().getReference("TurnosActualesAtracciones")
                        .child("Mano Del Artesano")
                        .setValue("00:00")

                    FirebaseDatabase.getInstance().getReference("TurnosAcumulados")
                        .removeValue()

                    FirebaseDatabase.getInstance().getReference("DatosLlamadoTurno")
                        .removeValue()

                    FirebaseDatabase.getInstance().getReference("LlamandoTurno")
                        .removeValue()

                    Toast.makeText(this, "Sistema de turnos reiniciado", Toast.LENGTH_SHORT).show()
                }
                .create()

            dialog.show()

        }
    }
}