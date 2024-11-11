package com.example.todoapp.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

class BorrarTareaServicio {

    private val firebase = Firebase.firestore

    //Funcion que obtiene el ID de la tarea a borrar
    suspend fun obtenerIdtarea(idusuario: String, posicion: Int) {

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
        borrartarea(listaid,idusuario,posicion)
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