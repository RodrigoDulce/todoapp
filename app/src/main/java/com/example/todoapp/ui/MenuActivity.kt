package com.example.todoapp.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.data.TareaModelo
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.databinding.ActivityMenuBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWebException
import com.google.firebase.auth.auth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MenuActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMenuBinding
    private lateinit var tareaAdaptador: TareaAdaptador
    private val itemAdaptador= mutableListOf<TareaModelo>()
    private lateinit var auth : FirebaseAuth
    private val tareasViewModel:TareasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializacion de recyclerview
        iniciarRecyclerview()

        //Accion del boton 'faAgregarTarea'
        binding.faAgregarTarea.setOnClickListener {
            setTarea()
        }

        //Accion del boton 'faCerrarSesion'
        binding.faCerrarSesion.setOnClickListener {
            auth = Firebase.auth

            auth.signOut()
            irInicioSesion()
        }

        cargarTarea()
    }

    //Funcion que inicializa el contenido del
    private fun iniciarRecyclerview(){
        auth=Firebase.auth
        tareaAdaptador=TareaAdaptador(onClickDelete = {position->
            tareasViewModel.borrarTarea(auth.uid.toString(),position)
            borrarItem(position)
        },
            onTaskComplete = {position , estadoTarea ->
                tareasViewModel.completarTarea(auth.uid.toString(),position,estadoTarea)
            },
            onClickEditar ={ posicion ->
                tareasViewModel.obtenerTareaEditable(auth.uid.toString(),posicion)
                tareasViewModel.tareaEditable.observe(this){ tarea ->
                    tarea?.let {
                        editarTarea(it.idTarea, it.nombreTarea, it.tareaFinalizacion )
                        tareasViewModel.clearEditableTask()
                    }
                }

            })
        binding.rvTareas.layoutManager=LinearLayoutManager(applicationContext)
        binding.rvTareas.adapter=tareaAdaptador


    }

    //Funcion que abre el dialogo para editar las tareas
    @SuppressLint("SetTextI18n")
    private fun editarTarea(
        idTareaActualizable: String,
        tareaAnterior: String,
        fechaAnterior: String
    ) {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialogo_tarea)
        dialog.setTitle("agregar tarea")
        dialog.show()

        val btnGuardarTarea = dialog.findViewById<Button>(R.id.btnGuardarTarea)
        val btnCancelarTarea = dialog.findViewById<Button>(R.id.btnCerrarDialogo)
        val nombreTarea = dialog.findViewById<EditText>(R.id.etNombreTarea)
        val calendario = dialog.findViewById<CalendarView>(R.id.cvFechaFinalizacion)
        val currentDate = Calendar.getInstance().timeInMillis
        var selectedDate = currentDate

        calendario.minDate = currentDate

        btnGuardarTarea.text = "Editar Tarea"
        nombreTarea.setText(tareaAnterior)
        val fechaFormateada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val date: Date? = fechaFormateada.parse(fechaAnterior)

        date?.let {
            //pone la fecha en el calendario
            calendario.date = it.time
        }

        calendario.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            selectedDate = cal.timeInMillis // Actualiza la fecha seleccionada
        }

        btnGuardarTarea.setOnClickListener {

            val idUsuario = FirebaseAuth.getInstance()


            if (nombreTarea.text.toString().isEmpty()) {
                Toast.makeText(this, "coloque correctamente el nombre", Toast.LENGTH_SHORT)
                    .show()
            } else {

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = selectedDate
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1 // +1 porque los meses son base 0
                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

                val fecha = "${dayOfMonth}/${month}/${year}"

                tareasViewModel.editarTarea(
                    idUsuario = idUsuario.uid.toString(),
                    idTarea = idTareaActualizable,
                    nombreTarea = nombreTarea.text.toString(),
                    fechaTarea = fecha
                )

                Toast.makeText(
                    this,
                    "tarea editada correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                cargarTarea()
                comprobarTarea()
                nombreTarea.setText("")
            }
        }
        btnCancelarTarea.setOnClickListener {
            dialog.cancel()
        }

    }

    //Funcion que abre el dialogo para crear una tarea
    @SuppressLint("SetTextI18n")
    private fun setTarea() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialogo_tarea)
        dialog.setTitle("agregar tarea")
        dialog.show()

        val btnGuardarTarea = dialog.findViewById<Button>(R.id.btnGuardarTarea)
        val btnCancelarTarea = dialog.findViewById<Button>(R.id.btnCerrarDialogo)
        val calendario = dialog.findViewById<CalendarView>(R.id.cvFechaFinalizacion)
        val currentDate = Calendar.getInstance().timeInMillis
        var selectedDate = currentDate

        calendario.minDate = currentDate

        calendario.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            selectedDate = cal.timeInMillis // Actualiza la fecha seleccionada
        }

        btnGuardarTarea.setOnClickListener {
            val idTarea = System.currentTimeMillis().hashCode()
            val idUsuario = FirebaseAuth.getInstance()
            val nombreTarea = dialog.findViewById<EditText>(R.id.etNombreTarea)

            if (nombreTarea.text.toString().isEmpty()) {
                Toast.makeText(this, "coloque correctamente el nombre", Toast.LENGTH_SHORT)
                    .show()
            } else {

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = selectedDate
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1 // +1 porque los meses son base 0
                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

                val fecha = "${dayOfMonth}/${month}/${year}"

                tareasViewModel.guardarTarea(
                    idTarea,
                    nombreTarea.text.toString(),
                    fecha,
                    false,
                    idUsuario.uid.toString()
                )

                Toast.makeText(
                    this,
                    "tarea guardada correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                cargarTarea()
                comprobarTarea()
                nombreTarea.setText("")
            }
        }
        btnCancelarTarea.setOnClickListener {
            dialog.cancel()
        }

    }

    //Comprueba que en el recyclerview haya un item y oculta el texto
    private fun comprobarTarea() {
        if (itemAdaptador.isEmpty()) {
            binding.tvTextoTarea.visibility = View.VISIBLE
            binding.rvTareas.visibility = View.GONE // Si no hay tareas, oculta el RecyclerView
        } else {
            binding.tvTextoTarea.visibility = View.GONE
            binding.rvTareas.visibility = View.VISIBLE // Si hay tareas, muestra el RecyclerView
        }
    }

    //Funcion para borrar un item del recyclerview cuando se borra una tarea
    @SuppressLint("NotifyDataSetChanged")
    private fun borrarItem(index:Int){

        if (::tareaAdaptador.isInitialized){
            itemAdaptador.removeAt(index)
            tareaAdaptador.setTarea(itemAdaptador)
            tareaAdaptador.notifyDataSetChanged()
            comprobarTarea()
        }
    }

    //Funcion que carga todas las tareas que tenga el usuario
    @SuppressLint("NotifyDataSetChanged")
    private fun cargarTarea(){

        val idUsuario=FirebaseAuth.getInstance()

        tareasViewModel.obtenertareas(idUsuario.uid.toString())

        tareasViewModel.livadatatarea.observe(this){ tarea ->
            itemAdaptador.clear()
            itemAdaptador.addAll(tarea)
            tareaAdaptador.setTarea(itemAdaptador)
            tareaAdaptador.notifyDataSetChanged()
            comprobarTarea()
        }

    }

    //Funcion para ir al inicio de sesion
    private fun irInicioSesion(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}