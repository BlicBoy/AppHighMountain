package com.example.highmountain

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.SparseArray
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.highmountain.databinding.ActivityCodeBinding
import com.example.highmountain.ui.PREF_CODE
import kotlinx.android.synthetic.main.activity_code.*


class CodeActivity : AppCompatActivity() {//, View.OnClickListener {

    lateinit var binding : ActivityCodeBinding

   // private var keyValues = SparseArray<String>()
   // var code = StringBuilder()

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