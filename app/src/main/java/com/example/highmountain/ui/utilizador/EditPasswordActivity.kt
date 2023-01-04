package com.example.highmountain.ui.utilizador

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.highmountain.R
import com.example.highmountain.databinding.ActivityEditPasswordBinding
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class EditPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityEditPasswordBinding
    lateinit var credential: AuthCredential

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)

        supportActionBar?.hide()

        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = Firebase.auth


        binding.buttonSaveUpdatePassword.setOnClickListener {
            var password = binding.editTextTextPasswordUpdate.text
             var passwordRepeat = binding.editTextTextPasswordUpdate2.text
            var oldPassword = binding.editTextTextOldPassword.text


            if(password.isNullOrBlank() || passwordRepeat.isNullOrBlank() || oldPassword.isNullOrBlank()){
                Toast.makeText(this, "As passwords estão vazias!",Toast.LENGTH_SHORT).show()
            }else{
                credential = EmailAuthProvider.getCredential(auth.currentUser?.email.toString(), oldPassword.toString())
                if(password.toString() != passwordRepeat.toString()){
                    Toast.makeText(this, "As passwords não são iguais!",Toast.LENGTH_SHORT).show()

                }else{
                    auth.currentUser?.reauthenticate(credential)
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                auth.currentUser?.updatePassword(password.toString())
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this, "Password Editada com sucesso", Toast.LENGTH_LONG).show()
                                            startActivity(Intent(this , ClienteActivity::class.java))
                                        }else{
                                            Toast.makeText(this, "Ocurreu um erro", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }else{
                                Toast.makeText(this, "Password Antiga Errada", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }



    }
}