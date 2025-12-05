package com.example.manodelartesanogestionturnosprincipal.Adapter

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.manodelartesanogestionturnosprincipal.DetallesAtraccion
import com.example.manodelartesanogestionturnosprincipal.Model.AtraccionModel
import com.example.manodelartesanogestionturnosprincipal.R
import com.example.manodelartesanogestionturnosprincipal.SubirTextoFirebase
import com.google.firebase.database.FirebaseDatabase

class AtraccionAdapter (
    private var atraccionLista: List<AtraccionModel>
) : RecyclerView.Adapter<AtraccionAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.atraccion_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val LAtraccion = atraccionLista[position]

        holder.nombreAtraccion.text = LAtraccion.Nombre

        holder.btnEliminarAtraccion.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Función (Eliminar Atracción) disponible en próximas actualizaciones",
                Toast.LENGTH_LONG)
                .show()
            /*
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Confirmar eliminación")
            builder.setMessage("¿Seguro deseas eliminar esta atraccion?")
            builder.setPositiveButton("Sí") { _, _ ->
                FirebaseDatabase.getInstance().getReference("Atracciones")
                    .child(LAtraccion.Nombre.toString())
                    .removeValue().addOnCompleteListener {
                        Toast.makeText(holder.itemView.context, "Atraccion Eliminada", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(holder.itemView.context, "Error", Toast.LENGTH_SHORT).show()
                    }
            }
            builder.setNegativeButton("Cancelar", null)
            builder.show()
             */
        }

        holder.SeleccionarAtraccion.setOnClickListener {

            val intent: Intent = Intent(holder.itemView.context, DetallesAtraccion::class.java)
            intent.putExtra("Nombre", LAtraccion.Nombre.toString())
            intent.putExtra("Tiempo", LAtraccion.Tiempo.toString())
            intent.putExtra("Turno", LAtraccion.Turno.toString())
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return atraccionLista.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val SeleccionarAtraccion: ConstraintLayout = itemView.findViewById(R.id.SeleccionarAtraccion)
        val btnEliminarAtraccion: ImageView = itemView.findViewById(R.id.btnEliminarAtraccion)
        val nombreAtraccion: TextView = itemView.findViewById(R.id.nombreAtraccion)
    }
}