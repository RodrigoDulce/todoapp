package com.example.todoapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.TareaModelo
import com.example.todoapp.databinding.ItemTareaBinding

class TareaAdaptador(val onClickDelete:(Int)->Unit, val onClickEditar:(Int)-> Unit,val onTaskComplete:(Int,Boolean)->Unit):RecyclerView.Adapter<TareaAdaptador.ViewHolder>(){
    private var listaTarea = mutableListOf<TareaModelo>()

    fun setTarea(tarea: MutableList<TareaModelo>) {
        listaTarea = tarea
    }

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){

        private val bindingAdaptador = ItemTareaBinding.bind(view)

        fun bind(tarea: TareaModelo, index : Int) {
            bindingAdaptador.tvTareaCarta.text=tarea.nombreTarea
            bindingAdaptador.tvFinalizacion.text=tarea.tareaFinalizacion
            bindingAdaptador.cbCompletado.isChecked = tarea.tareaCompleta
            bindingAdaptador.cbCompletado.setOnCheckedChangeListener { _, isChecked ->
                onTaskComplete(index,isChecked)
            }
            bindingAdaptador.btBorrarTarea.setOnClickListener {
                onClickDelete(index)
            }
            bindingAdaptador.btEditarTarea.setOnClickListener {
                onClickEditar(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val view=LayoutInflater.from(parent.context).inflate(R.layout.item_tarea,parent,false)
      return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaAdaptador.ViewHolder, position: Int) {
        val tarea = listaTarea[position]

        holder.bind(tarea, position)
    }


    override fun getItemCount(): Int {
        return if(listaTarea.size>0){
            listaTarea.size
        }else{
            0
        }
    }


}