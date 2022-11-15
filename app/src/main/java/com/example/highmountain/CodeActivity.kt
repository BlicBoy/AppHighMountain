package com.example.highmountain

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast


import com.example.highmountain.databinding.ActivityCodeBinding


import com.example.highmountain.ui.PREF_CODE



class CodeActivity : AppCompatActivity() {

    lateinit var binding : ActivityCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code)

        binding = ActivityCodeBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        binding.buttonEntrar.setOnClickListener {
          if (this@CodeActivity.PREF_CODE == binding.editTextNumberPasswordInicio.text.toString()){
              startActivity(Intent(this@CodeActivity,MainActivity::class.java))
          }else{
              Toast.makeText(baseContext, "Erro",Toast.LENGTH_LONG).show()
          }
        }
    }
}