package com.example.todoapp.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

class ActualizarTarea {
private val firebase= Firebase.firestore
    suspend fun obteneractarea(idusuario: String, position: Int,estadotarea:Boolean) {

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
        actualizartarea(listaid,idusuario,position,estadotarea)
    }
    private fun actualizartarea(idtarea:List<String>,idusuario: String,position: Int,estadotarea:Boolean){
        val actualizaciontarea= hashMapOf<String,Any>(
            "estado de la tarea" to estadotarea
        )
        firebase.collection("usuario")
            .document(idusuario)
            .collection("tareas")
            .document(idtarea[position])
            .update(actualizaciontarea)
            .addOnSuccessListener {
                Log.e("Completar tarea", "Documento actualizado")
            }
            .addOnFailureListener { e ->
                Log.e("Completar tarea", "Error updating document", e)
            }
    }

    suspend fun obtenerTareaEditable(idUser: String, index: Int) : TareaModelo {
        val db = Firebase.firestore

        var listaTareas: TareaModelo? = null
        val idList = mutableListOf<String>()

        val docRef = db.collection("usuario").document(idUser).collection("tareas").get().await()

        docRef.forEach { id ->
            val taskId = CoroutineScope(Dispatchers.IO).async {
                listOf(id.id)
            }.await()

            idList.addAll(taskId)
        }

        val listaFirebase =
            db.collection("usuario").document(idUser).collection("tareas").get().await()

        for (tareas in listaFirebase) {
            val posicionTarea = idList[index]
            val idTask = tareas.getLong("id tarea")

            val nameTask = tareas.getString("tarea")
            val fechaFinalizacion = tareas.getString("fecha de finalizacion")

            if (idTask.toString() == posicionTarea) {
                listaTareas =
                    TareaModelo(
                        idTarea =  idTask.toString(),
                        nombreTarea =  nameTask ?: "",
                        tareaFinalizacion = fechaFinalizacion ?: "",
                    )
            }
        }

        return listaTareas ?: TareaModelo(
            nombreTarea = "",
            tareaFinalizacion = "",
        )
    }


    fun editarTareas(idTask: String, idUser: String, nombreTarea: String, fecha:String){
        val taskMap = hashMapOf<String, Any>(
            "tarea" to nombreTarea,
            "fecha de finalizacion" to fecha
        )

        val db = Firebase.firestore

        db.collection("usuario")
            .document(idUser)
            .collection("tareas")
            .document(idTask)
            .update(taskMap)
            .addOnSuccessListener {
                Log.w("Completar tarea", "Documento editada")
            }
            .addOnFailureListener { e ->
                Log.w("Completar tarea", "Error updating document", e)
            }
    }
}
