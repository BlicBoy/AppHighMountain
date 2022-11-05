package com.example.highmountain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.highmountain.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btReg.setOnClickListener {
            val nome = binding.etRegNome.text.toString()
            val email = binding.etEmailReg.text.toString()
            val password = binding.etPassReg.text.toString()
            val passwordRepetir = binding.etRepPassReg.text.toString()


            if (nome.isNullOrBlank()||email.isNullOrBlank() || password.isNullOrBlank() || passwordRepetir.isNullOrBlank()){
                Toast.makeText(this, "Não foram preenchidos todos os campos", Toast.LENGTH_LONG).show()
            }else{
                if(password != passwordRepetir){
                    Toast.makeText(this,"As passwords não são iguais!", Toast.LENGTH_LONG).show()
                }else{
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this,MainActivity::class.java))
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Error",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }

    companion object{
        const val TAG = "RegisterActivity"
    }

}