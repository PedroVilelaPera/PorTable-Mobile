package com.example.portable

data class Ficha (
    val id: Int,
    var nome: String,
    var foto: Int,
    var nivel: Int,
    var barras: MutableList<Barra>,
    var status: MutableList<Status>,
    var pericias: MutableList<Pericia>,
    var habilidades: MutableList<Habilidade>
)

data class Barra (
    var nome: String,
    var valorAtual: Int,
    var valorMaximo: Int
)

data class Status (
    var nome: String,
    var valor: Int,
)

data class Pericia (
    var nome: String,
    var valor: Int,
)

data class Habilidade (
    var nome: String,
    var descricao: String,
    var dados: String
)


