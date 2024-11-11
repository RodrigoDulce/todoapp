package com.example.todoapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CargaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        val pantallaCarga=installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car)
        auth= Firebase.auth
        pantallaCarga.setKeepOnScreenCondition{true}

    }

    //Comprobacion de que un usuario haya iniciado sesion o haya cerrado sesion
    override fun onStart() {
        super.onStart()
        val usuario=auth.currentUser
        Handler(Looper.getMainLooper()).postDelayed({

                if (usuario!= null) {
                    irMenu()
                    finish()
                }else{
                    irlogin()
                }

        }, 3000)
    }

    private fun irMenu(){
        val intent=Intent(this,MenuActivity::class.java)
        startActivity(intent)
    }

    private fun irlogin(){
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
