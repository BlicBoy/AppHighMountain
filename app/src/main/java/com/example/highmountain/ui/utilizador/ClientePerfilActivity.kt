package com.example.highmountain.ui.utilizador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.highmountain.R
import com.example.highmountain.databinding.ActivityClientePerfilBinding
import com.google.firebase.auth.FirebaseAuth

class ClientePerfilActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var binding : ActivityClientePerfilBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_perfil)

        binding = ActivityClientePerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.hide()

    }
}