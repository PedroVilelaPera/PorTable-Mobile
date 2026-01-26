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
import com.example.portable.model.FichaResponse
import com.example.portable.model.LoginResponse
import com.google.gson.Gson
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaDeFichasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaDeFichasBinding
    private lateinit var listaFichas: ArrayList<FichaResumo>
    private lateinit var fichaAdapter: FichaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaDeFichasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idLogado = intent.getIntExtra("USER_ID", -1)

        listaFichas = intent.getParcelableArrayListExtra("LISTA_FICHAS") ?: arrayListOf()


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

        fichaAdapter = FichaAdapter(
            fichas = listaFichas,
            onFichaClick = { idDaFichaClicada ->
                // Por enquanto, apenas avisamos qual ID foi clicado
                Toast.makeText(this, "Abrindo ficha de ID: $idDaFichaClicada", Toast.LENGTH_SHORT).show()

                // TODO: Aqui no futuro faremos a chamada para buscar os detalhes da ficha $idDaFichaClicada
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

        if (idLogado != -1) {
            buscarFichas(idLogado)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exibirDialogLogout()
            }
        })
    }

    private fun buscarFichas(usuario_id: Int) {
        RetrofitClient.instance.getSheets(usuario_id).enqueue(object : Callback<List<FichaResponse>> {
            override fun onResponse(call: Call<List<FichaResponse>>, response: Response<List<FichaResponse>>) {
                if (response.isSuccessful) {
                    val resBody = response.body()

                    val gson = Gson()

                    val fichasMapeadas = resBody?.map { ficha ->
                        val map = gson.fromJson(ficha.dados_json, Map::class.java)
                        val nomePersonagem = map["nome"]?.toString() ?: "Sem Nome"

                        FichaResumo(ficha.id, nomePersonagem)
                    } ?: emptyList()

                    fichaAdapter.updateData(fichasMapeadas)

                } else {
                    exibirAvisoErro("ERRO NO SERVIDOR", "Falha ao carregar fichas. Tente mais tarde.")
                }
            }
            override fun onFailure(call: Call<List<FichaResponse>>, t: Throwable) {
                // Erro de conexão (Ip errado ou sem internet)
                exibirAvisoErro("ERRO NA CONEXÃO", "Não foi possível alcançar o servidor. Verifique sua rede.")
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