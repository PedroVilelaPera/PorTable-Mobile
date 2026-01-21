package com.example.portable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView

class PericiasAdapter(
    private val listaPericias: MutableList<Pericia>,
    private val onDeleteClick: (Int) -> Unit,
    private val onDataChanged: () -> Unit
) : RecyclerView.Adapter<PericiasAdapter.PericiaViewHolder>() {

    inner class PericiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnDelete: ImageView = itemView.findViewById(R.id.delete_pericia_btn)
        val edtNome: EditText = itemView.findViewById(R.id.edt_nome_pericia)
        val edtValor: EditText = itemView.findViewById(R.id.edt_valor_pericia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PericiaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pericia_layout, parent, false)
        return PericiaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PericiaViewHolder, position: Int) {
        val pericia = listaPericias[position]

        holder.edtNome.setText(pericia.nome)
        holder.edtValor.setText(pericia.valor.toString())

        val focusListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                pericia.nome = holder.edtNome.text.toString().limparEspacosExtras()

                val valorRaw = holder.edtValor.text.toString().replace(Regex("^0+(?!$)"), "").replace(Regex("[^0-9]"), "")
                pericia.valor = valorRaw.toIntOrNull() ?: 0

                holder.edtNome.setText(pericia.nome)
                holder.edtValor.setText(pericia.valor.toString())

                onDataChanged()
            }
        }

        holder.edtNome.onFocusChangeListener = focusListener
        holder.edtValor.onFocusChangeListener = focusListener

        holder.edtNome.doAfterTextChanged { pericia.nome = it.toString() }
        holder.edtValor.doAfterTextChanged {
            pericia.valor = it.toString().toIntOrNull() ?: 0
        }

        holder.btnDelete.setOnClickListener {
            val contexto = holder.itemView.context
            val dialogView = LayoutInflater.from(contexto).inflate(R.layout.dialog_custom, null)

            val txtTitle = dialogView.findViewById<TextView>(R.id.dialog_title)
            val txtMessage = dialogView.findViewById<TextView>(R.id.dialog_message)
            val btnCancel = dialogView.findViewById<TextView>(R.id.btn_cancel)
            val btnConfirm = dialogView.findViewById<TextView>(R.id.btn_confirm)

            txtTitle.text = "!!! EXCLUIR PERÍCIA !!!"
            txtMessage.text = "Tem certeza que deseja apagar '${pericia.nome.ifEmpty { "..." }}'?"

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

    override fun getItemCount() = listaPericias.size
}