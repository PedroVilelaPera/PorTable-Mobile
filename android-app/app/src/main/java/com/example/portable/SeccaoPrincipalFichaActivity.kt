package com.example.portable

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.portable.databinding.ActivitySeccaoPrincipalFichaBinding
import com.example.portable.model.FichaResponse
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SeccaoPrincipalFichaActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeccaoPrincipalFichaBinding
    var fichaCompleta: Ficha? = null
    private var ultimaFichaSalva: String = ""
    private val handlerDebounce = Handler(Looper.getMainLooper())
    private var runnableSalvar: Runnable? = null
    private lateinit var animacaoGirar: android.view.animation.Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeccaoPrincipalFichaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fichaCompleta = if (android.os.Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("FICHA_SELECIONADA", Ficha::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Ficha>("FICHA_SELECIONADA")
        } ?: Ficha(
            123456789,
            "",
            0,
            1,
            mutableListOf(Barra("", 1, 2)),
            mutableListOf(Status("", 0)),
            mutableListOf(Pericia("", 0)),
            mutableListOf(Habilidade("", "", ""))
        )

        ultimaFichaSalva = fichaCompleta.toString()

        val btnSair = binding.root.findViewById<android.widget.ImageView>(R.id.quit_btn)
        btnSair.setOnClickListener {
            exibirDialogSair()
        }

        val titulos = listOf("INFO", "PERÍCIAS", "HABILIDADES")

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        viewPager.adapter = FichaPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titulos[position]
        }.attach()

        val decorView = window.decorView
        decorView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = android.graphics.Rect()
            decorView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = decorView.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight < screenHeight * 0.15) {
                tirarFocoGeral()
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exibirDialogSair()
            }
        })

        animacaoGirar = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.girar)
    }

    override fun onPause() {
        super.onPause()
        runnableSalvar?.let {
            handlerDebounce.removeCallbacks(it)
            executarSalvamentoReal()
        }
    }

    private fun exibirDialogSair() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)

        val txtTitle = dialogView.findViewById<android.widget.TextView>(R.id.dialog_title)
        val txtMessage = dialogView.findViewById<android.widget.TextView>(R.id.dialog_message)
        val btnConfirm = dialogView.findViewById<android.widget.TextView>(R.id.btn_confirm)
        val btnCancel = dialogView.findViewById<android.widget.TextView>(R.id.btn_cancel)

        txtTitle.text = "!!! SAIR DA FICHA !!!"
        txtMessage.text = "Deseja salvar as alterações e voltar para a lista de fichas?"

        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnConfirm.setOnClickListener {
            dialog.dismiss()
            finalizarESair()
        }

        dialog.show()
    }

    private fun finalizarESair() {
        tirarFocoGeral()

        // Cancela qualquer salvamento pendente no Timer
        runnableSalvar?.let { handlerDebounce.removeCallbacks(it) }

        // Chama o salvamento real de forma que ele feche a activity ao terminar de salvar
        executarSalvamentoReal(deveSairAoFinalizar = true)
    }

    fun tirarFocoGeral() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            view.clearFocus()
        }
    }

    fun salvarDadosNoBanco() {
        runnableSalvar?.let { handlerDebounce.removeCallbacks(it) }

        runnableSalvar = Runnable {
            executarSalvamentoReal()
        }

        handlerDebounce.postDelayed(runnableSalvar!!, 3500)
    }

    private fun executarSalvamentoReal(deveSairAoFinalizar: Boolean = false) {
        val fichaAtual = fichaCompleta ?: return
        val fichaStringAtual = fichaAtual.toString()

        if (fichaStringAtual == ultimaFichaSalva) {
            if (deveSairAoFinalizar) sairRealmente()
            return
        }

        binding.imgLoading.visibility = View.VISIBLE
        binding.imgLoading.startAnimation(animacaoGirar)

        ultimaFichaSalva = fichaStringAtual

        ultimaFichaSalva = fichaStringAtual
        Log.d("BD_UPDATE", "$fichaAtual")

        val gson = Gson()
        val request = FichaResponse(
            id = fichaAtual.id,
            usuario_id = LoginActivity.USUARIO_ID_SESSAO, // Usando sua variável de sessão
            dados_json = gson.toJson(fichaAtual)
        )

        RetrofitClient.instance.updateSheet(fichaAtual.id, request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!isFinishing && !isDestroyed) {
                    binding.imgLoading.clearAnimation()
                    binding.imgLoading.visibility = View.INVISIBLE

                    if (response.isSuccessful) {
                        // Se o servidor confirmou, e o usuário quis sair, fechamos a tela
                        if (deveSairAoFinalizar) sairRealmente()
                    } else {
                        Toast.makeText(this@SeccaoPrincipalFichaActivity, "Erro ao salvar no servidor", Toast.LENGTH_SHORT).show()
                        if (deveSairAoFinalizar) sairRealmente()
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                if (!isFinishing && !isDestroyed) {
                    binding.imgLoading.clearAnimation()
                    binding.imgLoading.visibility = View.INVISIBLE
                    Toast.makeText(this@SeccaoPrincipalFichaActivity, "Falha na rede", Toast.LENGTH_SHORT).show()
                    if (deveSairAoFinalizar) sairRealmente()
                }
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            if (!isFinishing && !isDestroyed) {
                binding.imgLoading.clearAnimation()
                binding.imgLoading.visibility = View.INVISIBLE
            }
        }, 1500)
    }

    private fun sairRealmente() {
        val intent = android.content.Intent(this, ListaDeFichasActivity::class.java)
        intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}
