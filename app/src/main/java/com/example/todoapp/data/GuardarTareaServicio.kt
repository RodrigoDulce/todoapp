package com.example.todoapp.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class GuardarTareaServicio {

    //Funcion para guardar una tarea
    fun guardarTarea(
        idTarea: Int,
        nombreTarea: String,
        fechaFinalizacion: String,
        estadoTarea: Boolean,
        idUsuario: String
    ) {
    val firebase=Firebase.firestore
        val tareaMapa= hashMapOf<String,Any>(
            "id tarea" to idTarea,
            "tarea" to nombreTarea,
            "fecha de finalizacion" to fechaFinalizacion,
            "estado de la tarea" to estadoTarea
        )
    firebase.collection("usuario")
        .document(idUsuario)
        .collection("tareas")
        .document(idTarea.toString())
        .set(tareaMapa)
        .addOnSuccessListener {
            Log.d("tareas", "Tarea guardada")

        }
        .addOnFailureListener { e ->
            Log.w("tareas", "Error en guardar", e)
        }
    }
}


