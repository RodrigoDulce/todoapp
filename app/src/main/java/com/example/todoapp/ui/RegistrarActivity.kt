package com.example.todoapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivityRegistrarBinding

class RegistrarActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRegistrarBinding
    private val loginViewModel:LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistroSesion.setOnClickListener {
            registrarUsuario()
        }
    }

    //Funcion para registrar a un usuario
    private fun registrarUsuario(){

        val email = binding.etRegistroCorreo.text.toString()
        val contraseña = binding.etContraseAAUsuario.text.toString()
        val confirmarcontraseña=binding.etConfirmarContraseA.text.toString()

        if (email.isEmpty() || !isValidEmail(email)){
            binding.etRegistroCorreo.error="ingrese correctamente el email"

        }else if(contraseña.isEmpty()||contraseña.length<6) {
            binding.etContraseAAUsuario.error="la contraseña tiene que tener 6 o mas caracteres"

        }else if(confirmarcontraseña.isEmpty()||confirmarcontraseña!=contraseña){
            binding.etConfirmarContraseA.error="la contraseña no es igual"

        }else{
            loginViewModel.registroUsuario(email, contraseña)
            Toast.makeText(applicationContext,"se registro con exito",Toast.LENGTH_SHORT).show()
            irIniciarSesion()
        }
    }

    private fun irIniciarSesion(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    //Comprueba que el campo email sea un email correcto
    private fun isValidEmail(correo: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    }
}