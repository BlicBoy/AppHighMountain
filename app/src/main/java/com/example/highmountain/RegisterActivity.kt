package com.example.highmountain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.highmountain.ui.utilizador.ClienteActivity
import com.example.highmountain.ui.utilizador.InfoClienteActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        val email = findViewById<TextView>(R.id.editTextTextEmailAddressClient).text
        val password = findViewById<TextView>(R.id.editTextTextPasswordCliente).text
        val button = findViewById<Button>(R.id.buttonRegister)
        auth = Firebase.auth


        button.setOnClickListener {
            if(email.isNullOrBlank() || password.isNullOrBlank()){
                Toast.makeText(this, "Erro, falta preencher campos!", Toast.LENGTH_SHORT).show()
            }else{
                auth.createUserWithEmailAndPassword(email.toString(),password.toString())
                    .addOnCompleteListener(this) {
                        if(it.isSuccessful){
                            Log.d(TAG,"Registo Feito com sucesso")
                            startActivity(Intent(this@RegisterActivity,InfoClienteActivity::class.java))
                        }else{
                            Log.d(TAG,"Erro!")
                            Toast.makeText(this,
                                "Utilizador ou password inválido!",
                                Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            }
        }

    }
    companion object{
        val TAG = "Register"
    }
}