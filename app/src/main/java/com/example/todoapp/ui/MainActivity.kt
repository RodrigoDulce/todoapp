package com.example.todoapp.ui

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val loginViewModel:LoginViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        binding.tvClickRegistrarse.setOnClickListener {
            val startActivity = Intent(applicationContext, RegistrarActivity::class.java)
            startActivity(startActivity)
        }

        binding.btnIniciarSesion.setOnClickListener {
            iniciosesion()
        }
    }
    private fun iniciosesion() {
        val email = binding.etCorreoUsuario.text.toString()
        val contraseña = binding.etContraseAUsuario.text.toString()
        if (email.isEmpty() || !isValidEmail(email)){
            binding.etCorreoUsuario.error="coloque correctamente el correo"
        }else if (contraseña.isEmpty()||contraseña.length<6){
            binding.etContraseAUsuario.error="coloque correctamente la contraseña"
        }else{
            auth = Firebase.auth
            auth.signInWithEmailAndPassword(email, contraseña)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.e("Iniciar Sesion", "Inicio de sesion completo")
                        startActivity(Intent(this, MenuActivity::class.java))
                        finish()
                    }
                }
                .addOnFailureListener { error ->
                    Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun isValidEmail(correo: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    }

    public override fun onStart() {
        super.onStart()
        auth = Firebase.auth
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}