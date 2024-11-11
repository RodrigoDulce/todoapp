package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Registrarusuariorepositorio
import kotlinx.coroutines.launch

class LoginViewModel:ViewModel() {
    private val registrarusuariorepositorio=Registrarusuariorepositorio()

    fun registrousuario(email:String,contraseña:String){
        viewModelScope.launch {
            registrarusuariorepositorio.registrarUsuario(email,contraseña)
        }
    }
}