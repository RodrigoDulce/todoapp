package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.RegistrarUsuarioServicio
import kotlinx.coroutines.launch

class LoginViewModel:ViewModel() {

    private val registrarUsuarioServicio=RegistrarUsuarioServicio()

    fun registroUsuario(email:String,contraseña:String){
        viewModelScope.launch {
            registrarUsuarioServicio.registrarUsuario(email,contraseña)
        }
    }
}