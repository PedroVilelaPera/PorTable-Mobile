package com.example.portable
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.portable.databinding.ActivityLoginBinding
import com.example.portable.exibirAvisoErro
import com.example.portable.model.LoginRequest
import com.example.portable.model.LoginResponse
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.jvm.java


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
        val email = binding.edittextInputEmail.text.toString()
        val senha = binding.edittextInputSenha.text.toString()

        val req = LoginRequest(email,senha)

        RetrofitClient.instance.login(req).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val resBody = response.body()

                    if (resBody?.success == true) {
                        val intent = Intent(this@LoginActivity, ListaDeFichasActivity::class.java)
                        intent.putExtra("USER_ID", response.body()?.userId)
                        startActivity(intent)
                        finish()
                    } else {
                        // Erro de credênciais
                        exibirAvisoErro("!!! ERRO !!!", "E-mail ou senha incorretos.")
                    }
                } else {
                    // Erro de Servidor (404 ou 500)
                    exibirAvisoErro("ERRO NO SERVIDOR", "Ocorreu um problema técnico. Tente mais tarde.")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Erro de conexão (Ip errado ou sem internet)
                exibirAvisoErro("ERRO NA CONEXÃO", "Não foi possível alcançar o servidor. Verifique sua rede.")
            }
        })
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

    companion object {
        var USUARIO_ID_SESSAO: Int = -1
    }
}