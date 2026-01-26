package com.example.portable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FichaAdapter(
    private val fichas: MutableList<FichaResumo>,
    private val onFichaClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<FichaAdapter.FichaViewHolder>() {

    inner class FichaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNome: TextView = view.findViewById(R.id.txt_nome_item)
        val btnDelete: ImageView = view.findViewById(R.id.btn_delete_ficha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FichaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.btn_selecao_de_ficha, parent, false)
        return FichaViewHolder(view)
    }

    override fun onBindViewHolder(holder: FichaViewHolder, position: Int) {
        val ficha = fichas[position]

        holder.txtNome.text = ficha.nome

        holder.txtNome.setOnClickListener { onFichaClick(ficha.id) }
        holder.btnDelete.setOnClickListener { onDeleteClick(ficha.id) }
    }

    override fun getItemCount() = fichas.size

    fun updateData(newFichas: List<FichaResumo>) {
        this.fichas.clear()
        this.fichas.addAll(newFichas)
        notifyDataSetChanged()
    }
}