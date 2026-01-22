package com.example.portable

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

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