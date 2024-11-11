package com.example.todoapp.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

class ActualizarTareaServicio {

    private val firebase = Firebase.firestore

    //Busca el id de la tarea para completar y le pasa el Id a la funcion 'completarTarea'
    suspend fun obtenerTarea(idUsuario: String, posicion: Int, estadoTarea: Boolean) {

        val listaid = mutableListOf<String>()

        val tareas = firebase.collection("usuario")
            .document(idUsuario)
            .collection("tareas")
            .get()
            .await()

        tareas.forEach { id ->
            val tareaid = CoroutineScope(Dispatchers.IO).async {
                listOf(id.id)
            }.await()
            listaid.addAll(tareaid)
        }
        completarTarea(listaid, idUsuario, posicion, estadoTarea)
    }

    //Funcion para marcar una tarea como completada
    private fun completarTarea(
        idTarea: List<String>,
        idUsuario: String,
        posicion: Int,
        estadoTarea: Boolean
    ) {
        val actualizaciontarea = hashMapOf<String, Any>(
            "estado de la tarea" to estadoTarea
        )
        firebase.collection("usuario")
            .document(idUsuario)
            .collection("tareas")
            .document(idTarea[posicion])
            .update(actualizaciontarea)
            .addOnSuccessListener {
                Log.e("Completar tarea", "Documento actualizado")
            }
            .addOnFailureListener { e ->
                Log.e("Completar tarea", "Error updating document", e)
            }
    }

    //Funcion que obtiene las tareas especificas que se desean editar
    suspend fun obtenerTareaEditable(idUsuario: String, posicion: Int): TareaModelo {
        val db = Firebase.firestore

        var listaTareas: TareaModelo? = null
        val idList = mutableListOf<String>()

        val docRef = db.collection("usuario").document(idUsuario).collection("tareas").get().await()

        docRef.forEach { id ->
            val taskId = CoroutineScope(Dispatchers.IO).async {
                listOf(id.id)
            }.await()

            idList.addAll(taskId)
        }

        val listaFirebase =
            db.collection("usuario").document(idUsuario).collection("tareas").get().await()

        for (tareas in listaFirebase) {
            val posicionTarea = idList[posicion]
            val idTask = tareas.getLong("id tarea")

            val nameTask = tareas.getString("tarea")
            val fechaFinalizacion = tareas.getString("fecha de finalizacion")

            if (idTask.toString() == posicionTarea) {
                listaTareas =
                    TareaModelo(
                        idTarea = idTask.toString(),
                        nombreTarea = nameTask ?: "",
                        tareaFinalizacion = fechaFinalizacion ?: "",
                    )
            }
        }

        return listaTareas ?: TareaModelo(
            nombreTarea = "",
            tareaFinalizacion = "",
        )
    }

    //Funcion que edita las tareas
    fun editarTareas(idTarea: String, idUsuario: String, nombreTarea: String, fechaTarea: String) {
        val mapaTarea = hashMapOf<String, Any>(
            "tarea" to nombreTarea,
            "fecha de finalizacion" to fechaTarea
        )

        val db = Firebase.firestore

        db.collection("usuario")
            .document(idUsuario)
            .collection("tareas")
            .document(idTarea)
            .update(mapaTarea)
            .addOnSuccessListener {
                Log.w("Completar tarea", "Documento editada")
            }
            .addOnFailureListener { e ->
                Log.w("Completar tarea", "Error updating document", e)
            }
    }
}
