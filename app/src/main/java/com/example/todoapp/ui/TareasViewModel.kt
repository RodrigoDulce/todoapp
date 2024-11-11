package com.example.todoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.ActualizarTareaServicio
import com.example.todoapp.data.BorrarTareaServicio
import com.example.todoapp.data.GuardarTareaServicio
import com.example.todoapp.data.ObtenerTareaServicio
import com.example.todoapp.data.TareaModelo
import kotlinx.coroutines.launch

class TareasViewModel : ViewModel() {

    //Declaracion de objetos
    private val guardarTareaServicio = GuardarTareaServicio()
    private val obtenerTareaServicio = ObtenerTareaServicio()
    private val actualizarTareaServicio = ActualizarTareaServicio()
    private val borrarTareaServicio = BorrarTareaServicio()

    //Encapsulamiento
    private val _liveDataTarea = MutableLiveData<List<TareaModelo>>()
    val livadatatarea: LiveData<List<TareaModelo>> get() = _liveDataTarea

    private val _tareaEditable = MutableLiveData<TareaModelo?>()
    val tareaEditable :LiveData<TareaModelo?> get() = _tareaEditable

    fun guardarTarea(
        idTarea: Int,
        nombreTarea: String,
        fechaFinalizacion: String,
        estadoTarea: Boolean,
        idUsuario: String
    ) {
        viewModelScope.launch {
            guardarTareaServicio.guardarTarea(
               idTarea =  idTarea,
               nombreTarea =  nombreTarea,
               fechaFinalizacion = fechaFinalizacion,
               estadoTarea =  estadoTarea,
               idUsuario =  idUsuario
            )
        }
    }

    fun obtenertareas(idUsuario: String) {
        viewModelScope.launch {
            val resultado = obtenerTareaServicio.obtenerTareas(
                idusuario =  idUsuario
            )
            _liveDataTarea.value = resultado
        }
    }

    fun borrarTarea(idUsuario: String, posicion: Int) {
        viewModelScope.launch {
            borrarTareaServicio.obtenerIdtarea(
                idusuario =  idUsuario,
                posicion = posicion
            )
        }
    }

    fun completarTarea(idUsuario: String, posicion: Int, estadoTarea: Boolean) {
        viewModelScope.launch {
            actualizarTareaServicio.obtenerTarea(
                idUsuario =  idUsuario,
                posicion = posicion,
                estadoTarea =  estadoTarea)

        }
    }

    fun clearEditableTask() {
        _tareaEditable.value = null
    }

    fun obtenerTareaEditable(idUsuario: String, posicion: Int){
        viewModelScope.launch {
            val result = actualizarTareaServicio.obtenerTareaEditable(
                idUsuario = idUsuario,
                posicion = posicion)
            _tareaEditable.value = result
        }
    }

    fun editarTarea(idUsuario: String, idTarea : String , nombreTarea:String, fechaTarea:String) {
        viewModelScope.launch {
            actualizarTareaServicio.editarTareas(
               idTarea =  idTarea,
               idUsuario = idUsuario,
               nombreTarea = nombreTarea,
               fechaTarea = fechaTarea)
        }
    }
}