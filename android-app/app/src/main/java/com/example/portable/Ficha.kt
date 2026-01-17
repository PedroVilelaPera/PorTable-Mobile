package com.example.portable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ficha (
    val id: Int,
    var nome: String,
    var foto: Int,
    var nivel: Int,
    var barras: MutableList<Barra>,
    var status: MutableList<Status>,
    var pericias: MutableList<Pericia>,
    var habilidades: MutableList<Habilidade>
) : Parcelable

@Parcelize
data class Barra (
    var nome: String,
    var valorAtual: Int,
    var valorMaximo: Int
) : Parcelable

@Parcelize
data class Status (
    var nome: String,
    var valor: Int,
) : Parcelable

@Parcelize
data class Pericia (
    var nome: String,
    var valor: Int,
) : Parcelable

@Parcelize
data class Habilidade (
    var nome: String,
    var descricao: String,
    var dados: String
) : Parcelable


