package com.example.portable

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portable.LoginActivity.Companion.USUARIO_ID_SESSAO
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

    private var idLogado: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaDeFichasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idVindoDoIntent = intent.getIntExtra("USER_ID", -1)
        if (idVindoDoIntent != -1) {
            USUARIO_ID_SESSAO = idVindoDoIntent
        }
        idLogado = LoginActivity.USUARIO_ID_SESSAO

        listaFichas = arrayListOf()


        binding.logoutBtn.setOnClickListener {
            exibirDialogLogout()
        }

        binding.addFicha.setOnClickListener {
            val gson = Gson()

            val novaFicha = Ficha(
                0, "Novo Personagem", 0, 1,
                mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf()
            )

            val fichaParaEnviar = FichaResponse(
                id = 0,
                usuario_id = idLogado,
                dados_json = gson.toJson(novaFicha)
            )

            RetrofitClient.instance.createSheet(fichaParaEnviar).enqueue(object : Callback<FichaResponse> {
                override fun onResponse(call: Call<FichaResponse>, response: Response<FichaResponse>){
                    if (response.isSuccessful){
                        val fichaCriada = response.body()

                        novaFicha.id = fichaCriada?.id ?: 0

                        val intent = Intent(this@ListaDeFichasActivity, SeccaoPrincipalFichaActivity::class.java)
                        intent.putExtra("FICHA_SELECIONADA", novaFicha)
                        startActivity(intent)

                        Toast.makeText(this@ListaDeFichasActivity, "Ficha salva com sucesso!", Toast.LENGTH_SHORT).show()
                        buscarFichas(idLogado) // Recarrega a lista ao fundo
                    }
                }
                override fun onFailure(call: Call<FichaResponse>, t: Throwable) {
                    exibirAvisoErro("ERRO DE CONEXÃO", "Não foi possível criar a ficha no servidor.")
                }
            })
        }

        fichaAdapter = FichaAdapter(
            fichas = listaFichas,
            onFichaClick = { idDaFicha ->
                Toast.makeText(this, "Carregando detalhes...", Toast.LENGTH_SHORT).show()

                RetrofitClient.instance.getSheet(idDaFicha).enqueue(object : Callback<FichaResponse>{
                    override fun onResponse(call: Call<FichaResponse>, response: Response<FichaResponse>) {
                        if (response.isSuccessful) {
                            val resBody = response.body()
                            val gson = Gson()

                            val fichaCarregada = gson.fromJson(resBody?.dados_json, Ficha::class.java)

                            fichaCarregada.id = resBody?.id ?: 0

                            val intent = Intent(this@ListaDeFichasActivity, SeccaoPrincipalFichaActivity::class.java)
                            intent.putExtra("FICHA_SELECIONADA", fichaCarregada)
                            startActivity(intent)
                        }
                    }
                    override fun onFailure(call: Call<FichaResponse>, t: Throwable) {
                        exibirAvisoErro("ERRO DE CONEXÃO", "Não foi possível carregar a ficha no servidor.")
                    }
                })
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

    override fun onResume() {
        super.onResume()
        if (idLogado != -1) {
            buscarFichas(idLogado)
        }
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
                        println("DEBUG: Ficha encontrada -> $nomePersonagem")
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
        dialogView.findViewById<TextView>(R.id.dialog_message).text = "Deseja encerrar a sessão e voltar para o login?"

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
        dialogView.findViewById<TextView>(R.id.dialog_message).text = "Tem certeza que deseja apagar esta ficha?"

        dialogView.findViewById<TextView>(R.id.btn_confirm).setOnClickListener {
            RetrofitClient.instance.deleteSheet(idParaDeletar).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        val index = listaFichas.indexOfFirst { it.id == idParaDeletar }
                        if (index != -1) {
                            listaFichas.removeAt(index)
                            fichaAdapter.notifyItemRemoved(index)
                            Toast.makeText(this@ListaDeFichasActivity, "Ficha eliminada!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    exibirAvisoErro("ERRO DE CONEXÃO", "Não foi possível carregar a ficha no servidor.")
                }
            })
            dialog.dismiss()
        }

        dialogView.findViewById<TextView>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}