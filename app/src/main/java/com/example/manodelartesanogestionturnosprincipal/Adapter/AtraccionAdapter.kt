package com.example.manodelartesanogestionturnosprincipal.Adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.manodelartesanogestionturnosprincipal.Model.AtraccionModel
import com.example.manodelartesanogestionturnosprincipal.R
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

        val datoId = LAtraccion.Id.toString()
        holder.nombreAtraccion.text = LAtraccion.Nombre

        holder.btnEliminarAtraccion.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Confirmar eliminación")
            builder.setMessage("¿Seguro deseas eliminar esta atraccion?")
            builder.setPositiveButton("Sí") { _, _ ->
                FirebaseDatabase.getInstance().getReference("Atracciones")
                    .child(datoId)
                    .removeValue().addOnCompleteListener {
                        Toast.makeText(holder.itemView.context, "Atraccion Eliminada", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(holder.itemView.context, "Error", Toast.LENGTH_SHORT).show()
                    }
            }
            builder.setNegativeButton("Cancelar", null)
            builder.show()
        }

        holder.SeleccionarAtraccion.setOnClickListener {
            val dialogView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.dialog_detalles_atraccion, null)

            val txtDetNomAtrac = dialogView.findViewById<TextView>(R.id.txtDetNomAtrac)
            txtDetNomAtrac.text = LAtraccion.Nombre

            val txtDetTiempAtrac = dialogView.findViewById<EditText>(R.id.txtDetTiempAtrac)
            txtDetTiempAtrac.setText(LAtraccion.Tiempo?.toString() ?: "")

            val btnActualizarAtracc = dialogView.findViewById<Button>(R.id.btnActualizarAtracc)

            val builder = androidx.appcompat.app.AlertDialog.Builder(holder.itemView.context)
            builder.setView(dialogView)
            val dialog = builder.create()

            btnActualizarAtracc.setOnClickListener {
                val nuevoTiempo = txtDetTiempAtrac.text.toString().toIntOrNull() ?: 0

                FirebaseDatabase.getInstance()
                    .getReference("Atracciones")
                    .child(LAtraccion.Id.toString())
                    .child("Tiempo")
                    .setValue(nuevoTiempo)
                    .addOnCompleteListener {
                        Toast.makeText(
                            holder.itemView.context,
                            "Tiempo Atracción Actualizado",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()
                    }.addOnFailureListener {
                        Toast.makeText(
                            holder.itemView.context,
                            "Error al actualizar tiempo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            dialog.show()
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