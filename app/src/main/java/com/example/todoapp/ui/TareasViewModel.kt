package com.example.todoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.ActualizarTarea
import com.example.todoapp.data.GuardarTareaServicio
import com.example.todoapp.data.ObtenerTarea
import com.example.todoapp.data.TareaModelo
import kotlinx.coroutines.launch

class TareasViewModel : ViewModel() {

    private val guardarTareaServicio = GuardarTareaServicio()
    private val obtenerTarea = ObtenerTarea()
    private val actualizarTarea = ActualizarTarea()

    private val _livedatatarea = MutableLiveData<List<TareaModelo>>()
    val livadatatarea: LiveData<List<TareaModelo>> get() = _livedatatarea

    private val _tareaEditable = MutableLiveData<TareaModelo?>()
    val tareaEditable :LiveData<TareaModelo?> get() = _tareaEditable

    fun guardartarea(
        idTarea: Int,
        nombreTarea: String,
        fechaFinalizacion: String,
        estadoTarea: Boolean,
        idUsuario: String
    ) {
        viewModelScope.launch {
            guardarTareaServicio.guardartarea(
                idTarea,
                nombreTarea,
                fechaFinalizacion,
                estadoTarea,
                idUsuario
            )
        }
    }

    fun obtenertareas(idUsuario: String) {
        viewModelScope.launch {
            val resultado = obtenerTarea.obtenertarea(idUsuario)
            _livedatatarea.value = resultado
        }
    }

    fun borrartarea(idUsuario: String, index: Int) {
        viewModelScope.launch {
            obtenerTarea.obteneruidtarea(idUsuario, index)
        }
    }

    fun completartarea(idUsuario: String, index: Int, estadoTarea: Boolean) {
        viewModelScope.launch {
            actualizarTarea.obteneractarea(idUsuario,index,estadoTarea)

        }
    }

    fun clearEditableTask() {
        _tareaEditable.value = null
    }

    fun obtenerTareaEditable(idUser: String, index: Int){
        viewModelScope.launch {
            val result = actualizarTarea.obtenerTareaEditable(idUser,index)
            _tareaEditable.value = result
        }
    }

    fun editarTarea(idUsuario: String, idTarea : String , nombreTarea:String, fechaTarea:String) {
        viewModelScope.launch {
            actualizarTarea.editarTareas(idTarea, idUsuario, nombreTarea, fechaTarea)
        }
    }
}