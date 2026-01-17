package com.example.portable

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portable.databinding.ActivityListaDeFichasBinding

@Suppress("DEPRECATION")
class ListaDeFichasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaDeFichasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaDeFichasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addFicha.setOnClickListener {
            // TODO: TAREFA 1 - CRIAR FICHA NO BD E SIMULTANEAMENTE REDIRECIONAR COM INTENT PARA
            //  "SECCAOPRINCIPALACTIVITY" COM UMA FICHA VAZIA

            Toast.makeText(this, "Criando Ficha", Toast.LENGTH_SHORT).show()
        }

        val listaFichas =
            intent.getParcelableArrayListExtra("LISTA_FICHAS") ?: arrayListOf(
                FichaResumo(1, "GRIG"),
                FichaResumo(2, "GROG"),
                FichaResumo(3, "GREG")
            )

        val fichaAdapter = FichaAdapter(
            fichas = listaFichas,
            onFichaClick = { id ->

                Toast.makeText(this, "Abrindo ficha de ID: $id", Toast.LENGTH_SHORT).show()

                // TODO: TAREFA 2 - BUSCAR FICHA DO BD COM O ID DA FICHA E FAZER O INTENT (PARCELABLE)
                //  PARA A ACTIVITY "SECCAOPRINCIPALACTIVITY"
            },
            onDeleteClick = { idParaDeletar ->

                val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)

                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                builder.setView(dialogView)
                val dialog = builder.create()

                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                val btnCancel = dialogView.findViewById<TextView>(R.id.btn_cancel)
                val btnConfirm = dialogView.findViewById<TextView>(R.id.btn_confirm)

                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }

                btnConfirm.setOnClickListener {
                    val index = listaFichas.indexOfFirst { it.id == idParaDeletar }
                    if (index != -1) {
                        listaFichas.removeAt(index)
                        binding.recyclerViewFichas.adapter?.notifyItemRemoved(index)

                        Toast.makeText(this, "Ficha deletada", Toast.LENGTH_SHORT).show()

                        // TODO: TAREFA 3 - TIRAR FICHA DO BANCO DE DADOS (... É SÓ ISSO)
                    }
                    dialog.dismiss()
                }

                dialog.show()
            }
        )

        binding.recyclerViewFichas.apply {
            adapter = fichaAdapter
            layoutManager = LinearLayoutManager(this@ListaDeFichasActivity)
        }
    }
}