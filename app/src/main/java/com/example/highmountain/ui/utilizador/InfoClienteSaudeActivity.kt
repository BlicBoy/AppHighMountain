package com.example.highmountain.ui.utilizador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.highmountain.R
import com.example.highmountain.databinding.ActivityInfoClienteSaudeBinding
import com.example.highmountain.ui.models.DataSaude
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_info_cliente_saude.*

class InfoClienteSaudeActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var binding : ActivityInfoClienteSaudeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_cliente_saude)

        supportActionBar?.hide()

        binding = ActivityInfoClienteSaudeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val arraySpinner = listOf<String>("A+","A-","B+","B-","AB+-","AB-","O+","O-")
        val arrayAdapter = ArrayAdapter(this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arraySpinner)
        spinnertiposangue.adapter = arrayAdapter

        var alergias = binding.editTextalergiasInfoCliente.text
        var doencas = binding.editTextDoencasInfoCliente.text




        binding.buttonGuardarDataSaudeCliente.setOnClickListener {
            DataSaude(
                spinnertiposangue.selectedItem.toString(),
                alergias.toString(),
                doencas.toString()
            ).saveDataSaude { error ->
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