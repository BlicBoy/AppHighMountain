package com.example.highmountain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.highmountain.databinding.ActivityLoginBinding
import com.example.highmountain.ui.role_user
import com.example.highmountain.ui.utilizador.ClienteActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityLoginBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        auth = Firebase.auth


        binding.btLogin.setOnClickListener {
            val email = binding.editTextTextEmailLogin.text.toString()
            val password = binding.editTextTextPasswordLogin.text.toString()

            if(email.isNullOrBlank() || password.isNullOrBlank())
            {
                Toast.makeText(baseContext,"Não foram preenchidos todos os campos",Toast.LENGTH_LONG).show()
            }
            else
            {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")

                            redirectRole()

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Error",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }

        binding.buttonRegisterLogin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    fun redirectRole (){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = Firebase.firestore
        db.collection("newUsers").document(uid)
            .addSnapshotListener { documento, error ->
                if (documento != null) {
                    this@LoginActivity.role_user = documento.getString("role")
                   if(documento.getString("role") == "Cliente"){
                       startActivity(Intent(this, ClienteActivity::class.java))
                   }else{
                       if(documento.getString("role") == "Administrador"){
                           startActivity(Intent(this, MainActivity::class.java))
                       }else{
                          Toast.makeText(baseContext, "Error",
                              Toast.LENGTH_SHORT).show()
                       }
                   }
                }
            }
    }

    companion object{
        const val TAG = "LoginActivity"
    }
}