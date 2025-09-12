package com.example.manodelartesanogestionturnosprincipal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.manodelartesanogestionturnosprincipal.Adapter.TextosAdapter
import com.example.manodelartesanogestionturnosprincipal.Model.TextosModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SubirTextoFirebase : AppCompatActivity() {

    private lateinit var databaseReference : DatabaseReference
    private lateinit var listaTexto : ArrayList<TextosModel>
    private lateinit var txtFecha : String
    private lateinit var RevTextos : RecyclerView
    private var estadoTexto: String = "Nuevo"
    private var textoIdSeleccionado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subir_texto_firebase)

        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val atrasSubirTxt = findViewById<ImageView>(R.id.atrasSubirTxt)
        val editTextContenido = findViewById<EditText>(R.id.editTextContenido)
        val btnSubirTxt = findViewById<Button>(R.id.btnSubirTxt)
        val btnEdit = findViewById<TextView>(R.id.btnEdit)
        RevTextos = findViewById(R.id.RevTextos)

        txtFecha = fechaActual

        databaseReference = FirebaseDatabase.getInstance()
            .getReference("carruselTexto")

        atrasSubirTxt.setOnClickListener {
            val intent: Intent = Intent(this@SubirTextoFirebase, GestionAppTV::class.java)
            startActivity(intent)
            finish()
        }

        btnEdit.setOnClickListener {
            editTextContenido.setText("")
        }

        btnSubirTxt.setOnClickListener {
            val texto = editTextContenido.text.toString()

            if (texto.isEmpty()) {
                Toast.makeText(this, "El texto no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val databaseReference = FirebaseDatabase.getInstance().getReference("TextosGuardados")

            if (estadoTexto == "Nuevo") {
                // 🔹 Crear un nuevo texto
                val id = databaseReference.push().key ?: return@setOnClickListener
                val nuevoTexto = TextosModel(id, txtFecha, texto)

                databaseReference.child(id).setValue(nuevoTexto)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Texto guardado", Toast.LENGTH_SHORT).show()
                        editTextContenido.setText("")
                        estadoTexto = "Nuevo"
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                    }

            } else if (estadoTexto == "Existente" && textoIdSeleccionado != null) {
                databaseReference.child(textoIdSeleccionado!!).child("Texto").setValue(texto)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Texto actualizado", Toast.LENGTH_SHORT).show()
                        editTextContenido.setText("")
                        estadoTexto = "Nuevo"  // volver a estado inicial
                        textoIdSeleccionado = null
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        RevTextos.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        listaTexto = arrayListOf<TextosModel>()
        RevTextos.visibility = View.GONE
        FirebaseDatabase.getInstance().reference.child("TextosGuardados")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot){
                    listaTexto.clear()
                    if (snapshot.exists()){
                        for (Snap in snapshot.children){
                            val data = Snap.getValue(TextosModel::class.java)
                            listaTexto.add(data!!)
                        }
                    }

                    val adapter = TextosAdapter(listaTexto) { textoSeleccionado ->
                        editTextContenido.setText(textoSeleccionado.Texto)
                        estadoTexto = "Existente"
                        textoIdSeleccionado = textoSeleccionado.Id
                    }

                    RevTextos.adapter = adapter
                    RevTextos.visibility = View.VISIBLE
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}