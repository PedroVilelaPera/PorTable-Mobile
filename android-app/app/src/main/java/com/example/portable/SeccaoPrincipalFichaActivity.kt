package com.example.portable

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.portable.databinding.ActivitySeccaoPrincipalFichaBinding
import com.google.android.material.tabs.TabLayoutMediator

class SeccaoPrincipalFichaActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeccaoPrincipalFichaBinding
    var fichaCompleta: Ficha? = null
    private var ultimaFichaSalva: String = ""
    private val handlerDebounce = Handler(Looper.getMainLooper())
    private var runnableSalvar: Runnable? = null

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
            mutableListOf(Habilidade("","",""))
        )

        val titulos = listOf("INFO", "PERÍCIAS", "HABILIDADES")

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        viewPager.adapter = FichaPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titulos[position]
        }.attach()

    }

    fun salvarDadosNoBanco() {
        runnableSalvar?.let { handlerDebounce.removeCallbacks(it) }

        runnableSalvar = Runnable {
            executarSalvamentoReal()
        }

        handlerDebounce.postDelayed(runnableSalvar!!, 5000)
    }

    private fun executarSalvamentoReal() {
        val fichaAtual = fichaCompleta ?: return
        val fichaStringAtual = fichaAtual.toString()

        if (fichaStringAtual == ultimaFichaSalva) {
            return
        }

        ultimaFichaSalva = fichaStringAtual
        Log.d("BD_UPDATE", "$fichaAtual")

        // TODO: LOGICA DE SALVAR NO BD
    }

    override fun onPause() {
        super.onPause()
        runnableSalvar?.let {
            handlerDebounce.removeCallbacks(it)
            executarSalvamentoReal()
        }
    }
}
