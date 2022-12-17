package com.example.highmountain.ui.utilizador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.highmountain.R
import com.example.highmountain.databinding.ActivitySaudeBinding
import com.example.highmountain.ui.models.DataSaude
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class SaudeActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    lateinit var binding : ActivitySaudeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saude)

        supportActionBar?.hide()

        var tipoSangue = binding.editTextTipoSangue.text
        var alergias = binding.editTextAlergias.text
        var doencas = binding.editTextDoencas.text

        binding.buttonSalvarSaude.setOnClickListener{
            DataSaude(
                UUID.randomUUID().toString(),
                tipoSangue.toString(),
                alergias.toString(),
                doencas.toString()
            ).saveDataSaude {
                error ->

                error?.let {
                    Toast.makeText(this,"Erro!", Toast.LENGTH_LONG).show()
                }?: kotlin.run {
                    Toast.makeText(this,"Guardado com sucesso!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,ClienteActivity::class.java))
                }
            }
        }
    }
}