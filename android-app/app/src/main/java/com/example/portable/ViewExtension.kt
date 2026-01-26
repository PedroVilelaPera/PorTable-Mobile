package com.example.portable

import android.R.id.message
import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

fun EditText.configurarEnterParaTirarFoco() {
    this.setOnEditorActionListener { v, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)

            v.clearFocus()
            v.rootView.requestFocus()
            true
        } else {
            false
        }
    }
}

fun String.limparEspacosExtras(): String {
    return this.trim().replace("\\s+".toRegex(), " ")
}

fun AppCompatActivity.exibirAvisoErro(titulo: String, mensagem: String) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)
    val dialog = AlertDialog.Builder(this)
        .setView(dialogView)
        .create()

    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    dialogView.findViewById<TextView>(R.id.dialog_title).text = titulo
    dialogView.findViewById<TextView>(R.id.dialog_message).text = mensagem

    // Esconde botão cancelar
    val btnCancel = dialogView.findViewById<TextView>(R.id.btn_cancel)
    btnCancel.visibility = View.GONE

    // Modifica botão de confirmação para fechar o pop-up
    val btnConfirm = dialogView.findViewById<TextView>(R.id.btn_confirm)
    btnConfirm.text = "[ OK ]"
    btnConfirm.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}