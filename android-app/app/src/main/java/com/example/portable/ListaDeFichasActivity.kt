package com.example.portable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.portable.databinding.ActivityListaDeFichasBinding

class ListaDeFichasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaDeFichasBinding

    private var fichas = mutableListOf<Ficha>(
        Ficha(
            1111,
            "a",
            1,
            10,
            mutableListOf<Barra>(Barra("Barra1", 1, 10)),
            mutableListOf<Status>(Status("Status1", 1)),
            mutableListOf<Pericia>(Pericia("Status1", 1)),
            mutableListOf<Habilidade>(Habilidade("Habilidade1", "desc.", "1d20 + 20"))
        ), Ficha(
            1111,
            "b",
            2,
            20,
            mutableListOf<Barra>(Barra("Barra1", 2, 20)),
            mutableListOf<Status>(Status("Status1", 2)),
            mutableListOf<Pericia>(Pericia("Status1", 2)),
            mutableListOf<Habilidade>(Habilidade("Habilidade1", "desc.", "2d10 + 20"))
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaDeFichasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}