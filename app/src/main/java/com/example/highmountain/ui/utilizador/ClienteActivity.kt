package com.example.highmountain.ui.utilizador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.highmountain.R
import com.example.highmountain.SplashActivity
import com.example.highmountain.databinding.ActivityClienteBinding
import com.example.highmountain.ui.PREF_CODE
import com.example.highmountain.ui.role_user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ClienteActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    lateinit var  binding: ActivityClienteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

        supportActionBar?.hide()

        binding = ActivityClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = Firebase.auth

        binding.buttonLogoutCliente.setOnClickListener {
            deletePreferences()
            auth.signOut()
            startActivity(Intent(this, SplashActivity::class.java))
        }

    }

    private fun deletePreferences() {
        this@ClienteActivity.PREF_CODE = "apagar"
        this@ClienteActivity.role_user = "apagar"
    }
}