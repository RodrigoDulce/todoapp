package com.example.todoapp.ui

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
        enableEdgeToEdge()
        binding=ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnRegistroSesion.setOnClickListener {
            registrarusuario()
        }
    }
    private fun registrarusuario(){

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
            loginViewModel.registrousuario(email, contraseña)
            Toast.makeText(applicationContext,"se registro con exito",Toast.LENGTH_SHORT).show()


        }
    }


    private fun isValidEmail(correo: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    }
}