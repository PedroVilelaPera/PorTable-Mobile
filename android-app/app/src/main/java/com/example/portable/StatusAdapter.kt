package com.example.portable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView

class StatusAdapter(
    private val listaStatus: MutableList<Status>,
    private val onDeleteClick: (Int) -> Unit,
    private val onDataChanged: () -> Unit
) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    inner class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val edtNome: EditText = itemView.findViewById(R.id.edt_nome_status)
        val edtValor: EditText = itemView.findViewById(R.id.edt_valor_status)
        val btnDelete: ImageView = itemView.findViewById(R.id.delete_status_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.status_layout, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val status = listaStatus[position]

        holder.edtNome.setText(status.nome)
        holder.edtValor.setText(status.valor.toString())

        val focusListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                status.nome = holder.edtNome.text.toString().limparEspacosExtras()
                holder.edtNome.setText(status.nome)

                val valorRaw = holder.edtValor.text.toString()
                    .replace(Regex("^0+(?!$)"), "")
                    .replace(Regex("[^0-9]"), "")

                val valorFinal = valorRaw.toIntOrNull() ?: 0
                status.valor = valorFinal
                holder.edtValor.setText(valorFinal.toString())

                onDataChanged()
            }
        }

        holder.edtNome.onFocusChangeListener = focusListener
        holder.edtValor.onFocusChangeListener = focusListener

        holder.edtNome.doAfterTextChanged { status.nome = it.toString() }

        holder.btnDelete.setOnClickListener {
            val contexto = holder.itemView.context
            val dialogView = LayoutInflater.from(contexto).inflate(R.layout.dialog_custom, null)

            val txtTitle = dialogView.findViewById<TextView>(R.id.dialog_title)
            val txtMessage = dialogView.findViewById<TextView>(R.id.dialog_message)
            val btnCancel = dialogView.findViewById<TextView>(R.id.btn_cancel)
            val btnConfirm = dialogView.findViewById<TextView>(R.id.btn_confirm)

            txtTitle.text = "!!! EXCLUIR STATUS !!!"
            txtMessage.text = "Deseja apagar '${if (status.nome.isBlank()) "..." else status.nome}'?"

            val builder = android.app.AlertDialog.Builder(contexto)
            builder.setView(dialogView)
            val dialog = builder.create()

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

    override fun getItemCount() = listaStatus.size
}