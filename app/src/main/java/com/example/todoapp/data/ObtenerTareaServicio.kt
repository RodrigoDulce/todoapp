package com.example.todoapp.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

class ObtenerTareaServicio {

    private val firebase = Firebase.firestore

    //Funcion que obtiene todas las tareas del usuario
    suspend fun obtenerTareas(idusuario: String): List<TareaModelo> {

        val listaTareas = mutableListOf<TareaModelo>()

        val listaFirebase = firebase.collection("usuario")
            .document(idusuario)
            .collection("tareas")
            .get().await()

        for (tareas in listaFirebase) {
            val nombretarea = tareas.getString("tarea")
            val fechafinalizacion = tareas.getString("fecha de finalizacion")
            val estadotarea = tareas.getBoolean("estado de la tarea")

            listaTareas.add(
                TareaModelo(
                    nombreTarea = nombretarea ?: "",
                    tareaFinalizacion = fechafinalizacion ?: "",
                    tareaCompleta = estadotarea ?: false
                )
            )
        }

        return listaTareas
    }
}