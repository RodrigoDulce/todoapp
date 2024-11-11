package com.example.todoapp.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

class ObtenerTarea {

    private val firebase = Firebase.firestore

    suspend fun obtenertarea(idusuario: String): List<TareaModelo> {

        val listatareas = mutableListOf<TareaModelo>()

        val listafirebase = firebase.collection("usuario")
            .document(idusuario)
            .collection("tareas")
            .get().await()

        for (tareas in listafirebase) {
            val nombretarea = tareas.getString("tarea")
            val fechafinalizacion = tareas.getString("fecha de finalizacion")
            val estadotarea = tareas.getBoolean("estado de la tarea")

            listatareas.add(
                TareaModelo(
                    nombreTarea = nombretarea ?: "",
                    tareaFinalizacion = fechafinalizacion ?: "",
                    tareaCompleta = estadotarea ?: false
                )
            )
        }

        return listatareas
    }

    suspend fun obteneruidtarea(idusuario: String, position: Int) {

        val listaid = mutableListOf<String>()

        val tareas = firebase.collection("usuario")
            .document(idusuario)
            .collection("tareas")
            .get()
            .await()

        tareas.forEach { id ->
            val tareaid = CoroutineScope(Dispatchers.IO).async {
                listOf(id.id)
            }.await()
            listaid.addAll(tareaid)
        }
      borrartarea(listaid,idusuario,position)
    }
    private fun borrartarea(idtarea:List<String>,idusuario: String,position: Int){
        firebase.collection("usuario")
            .document(idusuario)
            .collection("tareas")
            .document(idtarea[position])
            .delete()
            .addOnSuccessListener {
                Log.e("Borrado Datos", "Documento borrado")
            }
            .addOnFailureListener { e ->
                Log.e("Borrado Datos", "Error en el borrado del documento", e)
            }
    }
}