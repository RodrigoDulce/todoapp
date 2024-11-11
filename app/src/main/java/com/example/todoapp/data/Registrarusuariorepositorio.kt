package com.example.todoapp.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Registrarusuariorepositorio {

    private lateinit var auth: FirebaseAuth

    fun registrarUsuario(email: String, contraseña: String) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("registrar", "usuario registrado")
                auth.signOut()
            }
                }.addOnFailureListener {
            Log.e("registrar", "error al registrar")
        }
    }
}