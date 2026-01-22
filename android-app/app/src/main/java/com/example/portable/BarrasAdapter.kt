package com.example.portable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView

@Suppress("DEPRECATION")
class BarrasAdapter(
    private val listaBarras: MutableList<Barra>,
    private val onDeleteClick: (Int) -> Unit,
    private val onDataChanged: () -> Unit
) : RecyclerView.Adapter<BarrasAdapter.BarraViewHolder>() {

    inner class BarraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val edtNome: EditText = itemView.findViewById(R.id.edt_nome_barra)
        val btnDelete: ImageView = itemView.findViewById(R.id.delete_barra_btn)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progess_barr)
        val edtAtual: EditText = itemView.findViewById(R.id.edt_min_barra)
        val edtMax: EditText = itemView.findViewById(R.id.edt_max_barra)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarraViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.barra_layout, parent, false)
        return BarraViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarraViewHolder, position: Int) {
        val barra = listaBarras[position]

        holder.edtNome.setText(barra.nome)
        holder.edtAtual.setText(barra.valorAtual.toString())
        holder.edtMax.setText(barra.valorMaximo.toString())
        atualizarVisualBarra(holder, barra.valorAtual, barra.valorMaximo)

        val focusListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                barra.nome = holder.edtNome.text.toString().limparEspacosExtras()
                holder.edtNome.setText(barra.nome)

                val maxRaw = holder.edtMax.text.toString().replace(Regex("^0+(?!$)"), "")
                    .replace(Regex("[^0-9]"), "")
                val atualRaw = holder.edtAtual.text.toString().replace(Regex("^0+(?!$)"), "")
                    .replace(Regex("[^0-9]"), "")

                val max = (if (maxRaw.isEmpty()) 1 else maxRaw.toIntOrNull() ?: 1).coerceAtLeast(1)

                val atual = if (atualRaw.isEmpty()) 0 else atualRaw.toIntOrNull() ?: 0
                val atualLimitado = atual.coerceAtMost(max)

                holder.edtMax.setText(max.toString())
                holder.edtAtual.setText(atualLimitado.toString())

                barra.valorAtual = atualLimitado
                barra.valorMaximo = max

                atualizarVisualBarra(holder, atualLimitado, max)

                onDataChanged()
            }
        }

        holder.edtNome.onFocusChangeListener = focusListener
        holder.edtAtual.onFocusChangeListener = focusListener
        holder.edtMax.onFocusChangeListener = focusListener

        fun atualizarApenasDesenho() {
            val max = holder.edtMax.text.toString().toIntOrNull() ?: 1
            val atual = holder.edtAtual.text.toString().toIntOrNull() ?: 0

            holder.progressBar.max = max
            holder.progressBar.progress = atual.coerceAtMost(max)
        }

        holder.edtNome.doAfterTextChanged { barra.nome = it.toString() }
        holder.edtAtual.doAfterTextChanged { atualizarApenasDesenho() }
        holder.edtMax.doAfterTextChanged { atualizarApenasDesenho() }

        holder.btnDelete.setOnClickListener {
            val contexto = holder.itemView.context

            val dialogView = LayoutInflater.from(contexto).inflate(R.layout.dialog_custom, null)

            val txtTitle = dialogView.findViewById<TextView>(R.id.dialog_title)
            val txtMessage = dialogView.findViewById<TextView>(R.id.dialog_message)
            val btnCancel = dialogView.findViewById<TextView>(R.id.btn_cancel)
            val btnConfirm = dialogView.findViewById<TextView>(R.id.btn_confirm)

            txtTitle.text = "!!! EXCLUIR BARRA !!!"
            txtMessage.text =
                "Tem certeza que deseja apagar a barra '${if (barra.nome.isBlank()) "..." else barra.nome}'?"

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

    private fun atualizarVisualBarra(holder: BarraViewHolder, atual: Int, max: Int) {
        holder.progressBar.max = max
        holder.progressBar.progress = atual
    }

    override fun getItemCount() = listaBarras.size
}