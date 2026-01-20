package com.example.portable

import android.app.Activity
import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun EditText.configurarEnterParaTirarFoco() {
    this.setOnEditorActionListener { v, actionId, event ->

        if (actionId == EditorInfo.IME_ACTION_DONE ||
            (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)
        ) {

            this.clearFocus()

            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.windowToken, 0)

            true
        } else {
            false
        }
    }
}

fun Activity?.tirarFocoGeral() {
    val viewFocada = this?.currentFocus

    if (viewFocada != null) {
        viewFocada.clearFocus()

        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(viewFocada.windowToken, 0)
    }
}

fun String.limparEspacosExtras(): String {
    return this.trim().replace("\\s+".toRegex(), " ")
}