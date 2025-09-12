package com.example.manodelartesanogestionturnosprincipal.Adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.manodelartesanogestionturnosprincipal.Model.TextosModel
import com.example.manodelartesanogestionturnosprincipal.R
import com.google.firebase.database.FirebaseDatabase

class TextosAdapter (
    private var textoLista: List<TextosModel>,
    private val onItemClick: (TextosModel) -> Unit
) : RecyclerView.Adapter<TextosAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.textos_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Latraccion = textoLista[position]

        val datoId = Latraccion.Id
        holder.txtFechaTxt.text = Latraccion.Fecha
        holder.txtContenidoTxt.text = Latraccion.Texto

        holder.btnEliminarTxt.setOnClickListener {
            // Mostrar AlertDialog antes de eliminar
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Confirmar eliminación")
            builder.setMessage("¿Seguro deseas eliminar este texto?")
            builder.setPositiveButton("Sí") { _, _ ->
                FirebaseDatabase.getInstance().getReference("TextosGuardados")
                    .child(datoId.toString())
                    .removeValue().addOnCompleteListener {
                        Toast.makeText(holder.itemView.context, "Texto Eliminado", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(holder.itemView.context, "Error", Toast.LENGTH_SHORT).show()
                    }
            }
            builder.setNegativeButton("Cancelar", null)
            builder.show()
        }

        holder.Contenido.setOnClickListener {
            onItemClick(Latraccion)
        }
    }

    override fun getItemCount(): Int {
        return textoLista.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Contenido: ConstraintLayout = itemView.findViewById(R.id.Contenido)
        val btnEliminarTxt: ImageView = itemView.findViewById(R.id.btnEliminarTxt)
        val txtFechaTxt: TextView = itemView.findViewById(R.id.txtFechaTxt)
        val txtContenidoTxt: TextView = itemView.findViewById(R.id.txtContenidoTxt)
    }
}