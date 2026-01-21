package com.example.portable

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portable.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var barrasAdapter: BarrasAdapter
    private lateinit var statusAdapter: StatusAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("DiscouragedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityMae = activity as? SeccaoPrincipalFichaActivity
        val fichaDaActivity = activityMae?.fichaCompleta

        // TODO: ---------------------------- IMAGEM DA FICHA ----------------------------

        val listaDeImagens = mutableListOf<Int>()
        var i = 1

        while (true) {
            val resId =
                resources.getIdentifier("presetpic$i", "drawable", requireContext().packageName)

            if (resId != 0) {
                listaDeImagens.add(resId)
                i++
            } else {
                break
            }
        }

        var indiceAtual = fichaDaActivity?.foto ?: 0
        binding.imgPersonagem.setImageResource(listaDeImagens[indiceAtual])

        fun atualizarImagemEFicha() {
            binding.imgPersonagem.setImageResource(listaDeImagens[indiceAtual])
            activityMae?.fichaCompleta?.foto = indiceAtual

            (activity as? SeccaoPrincipalFichaActivity)?.salvarDadosNoBanco()
        }

        binding.changeForthBtn.setOnClickListener {
            indiceAtual = if (indiceAtual < listaDeImagens.size - 1) indiceAtual + 1 else 0
            atualizarImagemEFicha()
        }

        binding.changeBackBtn.setOnClickListener {
            indiceAtual = if (indiceAtual > 0) indiceAtual - 1 else listaDeImagens.size - 1
            atualizarImagemEFicha()
        }

        // TODO: ---------------------------- NOME DO PERSONAGEM ----------------------------

        var nomeAtual = fichaDaActivity?.nome ?: "..."
        binding.edtNomePersonagem.setText(nomeAtual)

        binding.edtNomePersonagem.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                binding.edtNomePersonagem.setText(
                    binding.edtNomePersonagem.text.toString().limparEspacosExtras()
                )
                nomeAtual = binding.edtNomePersonagem.text.toString()
                activityMae?.fichaCompleta?.nome = nomeAtual

                (activity as? SeccaoPrincipalFichaActivity)?.salvarDadosNoBanco()
            }
        }

        binding.edtNomePersonagem.configurarEnterParaTirarFoco()

        // TODO: ---------------------------- BARRAS DO PERSONAGEM ----------------------------

        val listaDeBarras = activityMae?.fichaCompleta?.barras ?: mutableListOf()

        barrasAdapter = BarrasAdapter(
            listaDeBarras,
            onDeleteClick = { posicao ->
                if (posicao >= 0 && posicao < listaDeBarras.size) {
                    listaDeBarras.removeAt(posicao)
                    barrasAdapter.notifyItemRemoved(posicao)
                    barrasAdapter.notifyItemRangeChanged(posicao, listaDeBarras.size)

                    (activity as? SeccaoPrincipalFichaActivity)?.salvarDadosNoBanco()
                }
            },
            onDataChanged = {
                (activity as? SeccaoPrincipalFichaActivity)?.salvarDadosNoBanco()
            }
        )

        binding.recyclerViewBarras.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = barrasAdapter
            itemAnimator = null
        }

        binding.recyclerViewBarras.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = barrasAdapter
        }

        binding.btnAddBarra.setOnClickListener {
            val novaBarra = Barra("", 1, 2)
            listaDeBarras.add(novaBarra)
            activityMae?.fichaCompleta?.barras = listaDeBarras
            barrasAdapter.notifyItemInserted(listaDeBarras.size - 1)
            binding.recyclerViewBarras.scrollToPosition(listaDeBarras.size - 1)
            (activity as? SeccaoPrincipalFichaActivity)?.salvarDadosNoBanco()
        }

        // TODO: ---------------------------- NÍVEL DO PERSONAGEM ----------------------------

        binding.edtNivelPersonagem.setText(fichaDaActivity?.nivel?.toString() ?: "0")

        binding.edtNivelPersonagem.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val textoNivel = binding.edtNivelPersonagem.text.toString()

                val nivelLimpo =
                    textoNivel.replace(Regex("^0+(?!$)"), "").replace(Regex("[^0-9]"), "")
                val nivelFinal = nivelLimpo.toIntOrNull() ?: 0

                binding.edtNivelPersonagem.setText(nivelFinal.toString())
                activityMae?.fichaCompleta?.nivel = nivelFinal

                (activity as? SeccaoPrincipalFichaActivity)?.salvarDadosNoBanco()
            }
        }

        binding.edtNivelPersonagem.configurarEnterParaTirarFoco()

        // TODO: ---------------------------- STATUS DO PERSONAGEM ----------------------------

        val listaStatus = activityMae?.fichaCompleta?.status ?: mutableListOf()

        statusAdapter = StatusAdapter(
            listaStatus,
            onDeleteClick = { posicao ->
                if (posicao >= 0 && posicao < listaStatus.size) {
                    listaStatus.removeAt(posicao)
                    statusAdapter.notifyItemRemoved(posicao)
                    statusAdapter.notifyItemRangeChanged(posicao, listaStatus.size)

                    (activity as? SeccaoPrincipalFichaActivity)?.salvarDadosNoBanco()
                }
            },
            onDataChanged = {
                (activity as? SeccaoPrincipalFichaActivity)?.salvarDadosNoBanco()
            }
        )

        binding.recyclerViewStatus.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = statusAdapter
            itemAnimator = null
        }

        binding.recyclerViewStatus.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = statusAdapter
        }

        binding.btnAddStatus.setOnClickListener {
            val novoStatus = Status("", 0)
            listaStatus.add(novoStatus)

            activityMae?.fichaCompleta?.status = listaStatus

            statusAdapter.notifyItemInserted(listaStatus.size - 1)
            binding.recyclerViewStatus.scrollToPosition(listaStatus.size - 1)

            (activity as? SeccaoPrincipalFichaActivity)?.salvarDadosNoBanco()
        }

        // TODO: ---------------------------- GERENCIAMENTO DE FOCO ----------------------------

        binding.mainContainer.setOnClickListener {
            activityMae?.tirarFocoGeral()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



