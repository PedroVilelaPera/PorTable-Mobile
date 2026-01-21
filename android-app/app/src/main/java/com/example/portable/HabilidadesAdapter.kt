package com.example.portable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView

class HabilidadesAdapter(
    private val listaHabilidades: MutableList<Habilidade>,
    private val onDeleteClick: (Int) -> Unit,
    private val onDataChanged: () -> Unit,
) : RecyclerView.Adapter<HabilidadesAdapter.HabilidadeViewHolder>() {

    inner class HabilidadeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val edtNome: EditText = itemView.findViewById(R.id.edt_nome_habilidade)
        val edtDescricao: EditText = itemView.findViewById(R.id.edt_descricao_habilidade)
        val edtDados: EditText = itemView.findViewById(R.id.edt_dados_habilidade)
        val btnDelete: ImageView = itemView.findViewById(R.id.delete_habilidade_btn)
        val btnCollapse: ImageView = itemView.findViewById(R.id.collapse_habilidade_btn)
        val sectionCollapsable: LinearLayout =
            itemView.findViewById(R.id.collapsable_section_habilidade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabilidadeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.habilidade_layout, parent, false)
        return HabilidadeViewHolder(view)
    }

    private val expandedPositions = mutableSetOf<Int>()

    override fun onBindViewHolder(holder: HabilidadeViewHolder, position: Int) {
        val habilidade = listaHabilidades[position]

        val isExpanded = expandedPositions.contains(position)
        holder.sectionCollapsable.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.btnCollapse.rotation = if (isExpanded) 180f else 0f

        holder.btnCollapse.setOnClickListener {
            val currentPos = holder.adapterPosition
            if (currentPos == RecyclerView.NO_POSITION) return@setOnClickListener

            if (expandedPositions.contains(currentPos)) {
                expandedPositions.remove(currentPos)
                holder.sectionCollapsable.visibility = View.GONE
                holder.btnCollapse.rotation = 0f
            } else {
                expandedPositions.add(currentPos)
                holder.sectionCollapsable.visibility = View.VISIBLE
                holder.btnCollapse.rotation = 180f
            }
        }

        holder.edtNome.setText(habilidade.nome)
        holder.edtDescricao.setText(habilidade.descricao)
        holder.edtDados.setText(habilidade.dados)

        val focusListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                habilidade.nome = holder.edtNome.text.toString().limparEspacosExtras()
                habilidade.descricao = holder.edtDescricao.text.toString().trim()
                habilidade.dados = holder.edtDados.text.toString().trim()
                holder.edtNome.setText(habilidade.nome)
                onDataChanged()
            }
        }

        holder.edtNome.onFocusChangeListener = focusListener
        holder.edtDescricao.onFocusChangeListener = focusListener
        holder.edtDados.onFocusChangeListener = focusListener

        holder.edtNome.doAfterTextChanged { habilidade.nome = it.toString() }
        holder.edtDescricao.doAfterTextChanged { habilidade.descricao = it.toString() }
        holder.edtDados.doAfterTextChanged { habilidade.dados = it.toString() }

        holder.btnDelete.setOnClickListener {
            val contexto = holder.itemView.context
            val dialogView = LayoutInflater.from(contexto).inflate(R.layout.dialog_custom, null)

            val btnConfirm = dialogView.findViewById<TextView>(R.id.btn_confirm)
            val btnCancel = dialogView.findViewById<TextView>(R.id.btn_cancel)
            val txtTitle = dialogView.findViewById<TextView>(R.id.dialog_title)
            val txtMessage = dialogView.findViewById<TextView>(R.id.dialog_message)

            txtTitle.text = "!!! EXCLUIR HABILIDADE !!!"
            txtMessage.text = "Tem certeza que deseja apagar a habilidade '${
                if (habilidade.nome.trim().isEmpty()) "..." else habilidade.nome
            }'?"

            val dialog = android.app.AlertDialog.Builder(contexto).setView(dialogView).create()

            btnCancel.setOnClickListener { dialog.dismiss() }
            btnConfirm.setOnClickListener {
                val currentPos = holder.adapterPosition
                if (currentPos != RecyclerView.NO_POSITION) {
                    onDeleteClick(currentPos)
                }
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    override fun getItemCount() = listaHabilidades.size
}