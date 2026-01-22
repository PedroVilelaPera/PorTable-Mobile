package com.example.portable

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portable.databinding.ActivityListaDeFichasBinding

class ListaDeFichasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaDeFichasBinding
    private lateinit var listaFichas: ArrayList<FichaResumo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaDeFichasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listaFichas = intent.getParcelableArrayListExtra("LISTA_FICHAS") ?: arrayListOf(
            FichaResumo(1, "ABEL"),
            FichaResumo(2, "BELA"),
            FichaResumo(3, "CAIN")
        )

        binding.logoutBtn.setOnClickListener {
            exibirDialogLogout()
        }

        binding.addFicha.setOnClickListener {

            val id = 0 // PLACEHOLDER
            val novaFichaVazia = Ficha(
                id, "", 0, 1,
                mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf()
            )

            // TODO: TAREFA 1 - CRIAR NOVA FICHA NO BD

            val intent = Intent(this, SeccaoPrincipalFichaActivity::class.java)
            intent.putExtra("FICHA_SELECIONADA", novaFichaVazia)
            startActivity(intent)

            Toast.makeText(this, "Criando Ficha...", Toast.LENGTH_SHORT).show()
        }

        val fichaAdapter = FichaAdapter(
            fichas = listaFichas,
            onFichaClick = { id ->
                Toast.makeText(this, "Abrindo ficha ...", Toast.LENGTH_SHORT).show()

                // TODO: TAREFA 2 - PUXAR FICHA DO BD

                val id = 0 // PLACEHOLDER
                val fichaCarregada = Ficha( // PLACEHOLDER
                    id, "...", 0, 1,
                    mutableListOf(Barra("...", 10, 10)),
                    mutableListOf(Status("...", 10)),
                    mutableListOf(Pericia("...", 2)),
                    mutableListOf(Habilidade("...", "...", "..."))
                )

                val intent = Intent(this, SeccaoPrincipalFichaActivity::class.java)
                intent.putExtra("FICHA_SELECIONADA", fichaCarregada)
                startActivity(intent)
            },
            onDeleteClick = { idParaDeletar ->
                exibirDialogDeletar(idParaDeletar)
            }
        )

        binding.recyclerViewFichas.apply {
            adapter = fichaAdapter
            layoutManager = LinearLayoutManager(this@ListaDeFichasActivity)

            itemAnimator = null
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exibirDialogLogout()
            }
        })
    }

    private fun exibirDialogLogout() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogView.findViewById<TextView>(R.id.dialog_title).text = "!!! SAIR !!!"
        dialogView.findViewById<TextView>(R.id.dialog_message).text =
            "Deseja encerrar a sessão e voltar para o login?"

        dialogView.findViewById<TextView>(R.id.btn_confirm).setOnClickListener {
            dialog.dismiss()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        dialogView.findViewById<TextView>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun exibirDialogDeletar(idParaDeletar: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogView.findViewById<TextView>(R.id.dialog_title).text = "!!! DELETAR !!!"
        dialogView.findViewById<TextView>(R.id.dialog_message).text =
            "Tem certeza que deseja apagar esta ficha?"

        dialogView.findViewById<TextView>(R.id.btn_confirm).setOnClickListener {
            val index = listaFichas.indexOfFirst { it.id == idParaDeletar }
            if (index != -1) {
                listaFichas.removeAt(index)
                binding.recyclerViewFichas.adapter?.notifyItemRemoved(index)
                Toast.makeText(this, "Ficha deletada", Toast.LENGTH_SHORT).show()

                // TODO: TAREFA 3 - REMOVER DO BANCO DE DADOS AQUI
            }
            dialog.dismiss()
        }

        dialogView.findViewById<TextView>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}