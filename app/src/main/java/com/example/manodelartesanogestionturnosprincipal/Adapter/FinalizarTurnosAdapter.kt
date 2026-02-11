package com.example.manodelartesanogestionturnosprincipal.Adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.manodelartesanogestionturnosprincipal.Model.FinalizarTurnosModel
import com.example.manodelartesanogestionturnosprincipal.R
import com.google.firebase.database.FirebaseDatabase

class FinalizarTurnosAdapter (
    private var TurnosFinalizadosLista: List<FinalizarTurnosModel>
) : RecyclerView.Adapter<FinalizarTurnosAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.turnos_finalizados_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val LTurnos = TurnosFinalizadosLista[position]

        holder.tiempoTurno.text = LTurnos.Tiempo
        holder.personasTurno.text = LTurnos.NumeroPersonas
        holder.NombreAtraccion.text = LTurnos.Atraccion
        holder.TurnoAsignado.text = LTurnos.TurnoAsignado

        if (LTurnos.Estado.equals("Finalizado")){
            holder.TurnoFinalizado.visibility = View.VISIBLE
            holder.TurnoCancelado.visibility = View.GONE
            holder.TurnoEspera.visibility = View.GONE
        }else if (LTurnos.Estado.equals("Cancelado")){
            holder.TurnoFinalizado.visibility = View.GONE
            holder.TurnoCancelado.visibility = View.VISIBLE
            holder.TurnoEspera.visibility = View.GONE
        }else if (LTurnos.Estado.equals("En Espera")){
            holder.TurnoFinalizado.visibility = View.GONE
            holder.TurnoCancelado.visibility = View.GONE
            holder.TurnoEspera.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return TurnosFinalizadosLista.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tiempoTurno: TextView = itemView.findViewById(R.id.tiempoTurno)
        val personasTurno: TextView = itemView.findViewById(R.id.personasTurno)
        val NombreAtraccion: TextView = itemView.findViewById(R.id.NombreAtraccion)
        val TurnoAsignado: TextView = itemView.findViewById(R.id.TurnoAsignado)
        val TurnoFinalizado: LinearLayout = itemView.findViewById(R.id.TurnoFinalizado)
        val TurnoCancelado: LinearLayout = itemView.findViewById(R.id.TurnoCancelado)
        val TurnoEspera: LinearLayout = itemView.findViewById(R.id.TurnoEspera)
    }
}