package com.example.portable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portable.databinding.FragmentHabilidadesBinding

class HabilidadesFragment : Fragment() {

    private var _binding: FragmentHabilidadesBinding? = null
    private val binding get() = _binding!!
    private lateinit var habilidadesAdapter: HabilidadesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabilidadesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityMae = activity as? SeccaoPrincipalFichaActivity

        // TODO: ---------------------------- HABILIDADES DO PERSONAGEM ----------------------------

        val listaHabilidades = activityMae?.fichaCompleta?.habilidades ?: mutableListOf()

        habilidadesAdapter = HabilidadesAdapter(
            listaHabilidades,
            onDeleteClick = { posicao ->
                if (posicao in listaHabilidades.indices) {
                    activityMae?.tirarFocoGeral()

                    listaHabilidades.removeAt(posicao)

                    habilidadesAdapter.notifyItemRemoved(posicao)
                    habilidadesAdapter.notifyItemRangeChanged(posicao, listaHabilidades.size)

                    activityMae?.salvarDadosNoBanco()
                }
            },
            onDataChanged = {
                activityMae?.salvarDadosNoBanco()
            }
        )

        binding.recyclerViewHabilidades.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = habilidadesAdapter
            itemAnimator = null
        }

        binding.btnAddHabilidade.setOnClickListener {
            val novaHab = Habilidade("", "", "")
            listaHabilidades.add(novaHab)
            habilidadesAdapter.notifyItemInserted(listaHabilidades.size - 1)
            binding.recyclerViewHabilidades.scrollToPosition(listaHabilidades.size - 1)
            activityMae?.salvarDadosNoBanco()
        }

        // TODO: ---------------------------- GERENCIAMENTO DE FOCO ----------------------------

        binding.habilidadesContainer.setOnClickListener {
            activityMae?.tirarFocoGeral()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}