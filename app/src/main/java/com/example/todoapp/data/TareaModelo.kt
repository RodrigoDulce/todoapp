package com.example.todoapp.data

data class TareaModelo(
    var idTarea : String = "",
    var nombreTarea :String = "",
    var tareaFinalizacion : String = "",
    var tareaCompleta : Boolean = false
)