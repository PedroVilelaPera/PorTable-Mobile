package com.example.portable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portable.databinding.FragmentPericiasBinding

class PericiasFragment : Fragment() {

    private var _binding: FragmentPericiasBinding? = null
    private val binding get() = _binding!!
    private lateinit var periciasAdapter: PericiasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPericiasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityMae = activity as? SeccaoPrincipalFichaActivity

        // TODO: ---------------------------- GERENCIAMENTO DE PERICIAS ----------------------------

        val listaPericias = activityMae?.fichaCompleta?.pericias ?: mutableListOf()

        periciasAdapter = PericiasAdapter(
            listaPericias,
            onDeleteClick = { posicao ->
                if (posicao in listaPericias.indices) {
                    listaPericias.removeAt(posicao)
                    periciasAdapter.notifyItemRemoved(posicao)
                    periciasAdapter.notifyItemRangeChanged(posicao, listaPericias.size)
                    activityMae?.salvarDadosNoBanco()
                }
            },
            onDataChanged = {
                activityMae?.salvarDadosNoBanco()
            }
        )

        binding.recyclerViewPericias.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = periciasAdapter
            itemAnimator = null
        }

        binding.btnAddPericia.setOnClickListener {
            val novaPericia = Pericia("", 0)
            listaPericias.add(novaPericia)
            periciasAdapter.notifyItemInserted(listaPericias.size - 1)
            binding.recyclerViewPericias.scrollToPosition(listaPericias.size - 1)
            activityMae?.salvarDadosNoBanco()
        }

        // TODO: ---------------------------- GERENCIAMENTO DE FOCO ----------------------------

        binding.periciasContainer.setOnClickListener {
            (activity as? SeccaoPrincipalFichaActivity)?.tirarFocoGeral()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}