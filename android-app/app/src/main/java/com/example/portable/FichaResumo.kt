package com.example.portable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FichaResumo(
    val id: Int,
    val nome: String,
) : Parcelable