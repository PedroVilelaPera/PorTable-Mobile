package com.example.portable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.portable.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: TAREFA 1 - BUSCAR AS FICHAS ATRELADAS AO USUARIO NO BANCO DE DADOS, FAZER UMA
        //  mUTABLElIST DE "FICHARESUMO"S E MANDAR PARA A TELA DE "LISTADEFICHASACTIVITY" EM INTENT
    }
}