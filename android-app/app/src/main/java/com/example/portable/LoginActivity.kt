package com.example.portable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.portable.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.main.setOnClickListener {
            tirarFocoGeral()
        }

        binding.edittextInputSenha.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                tirarFocoGeral()
                executarNavegacaoLogin()
                true
            } else false
        }

        binding.bntLogin.setOnClickListener {
            executarNavegacaoLogin()
        }

        configurarAutoScroll()

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
    }

    private fun tirarFocoGeral() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }

    private fun executarNavegacaoLogin() {

        // TODO: BUSCAR FICHAS NO BD

        val listaMock = arrayListOf( // PLACEHOLDER (USE FichaResumo)
            FichaResumo(1, "ABEL"),
            FichaResumo(2, "BELA"),
            FichaResumo(3, "CAIN")
        )

        val intent = Intent(this, ListaDeFichasActivity::class.java)
        intent.putParcelableArrayListExtra("LISTA_FICHAS", listaMock)
        startActivity(intent)
        finish()
    }

    private fun configurarAutoScroll() {
        val listener = { _: android.view.View, hasFocus: Boolean ->
            if (hasFocus) {
                binding.main.postDelayed({
                    binding.main.scrollTo(0, binding.bntLogin.bottom)
                }, 101)
            }
        }
        binding.edittextInputEmail.setOnFocusChangeListener(listener)
        binding.edittextInputSenha.setOnFocusChangeListener(listener)
    }
}