package com.example.highmountain.ui.utilizador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.highmountain.R
import com.example.highmountain.databinding.ActivityClienteBinding
import com.example.highmountain.databinding.ActivityEditPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class EditPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityEditPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)

        supportActionBar?.hide()

        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var password = binding.editTextTextPasswordUpdate.text
        var passwordRepeat = binding.editTextTextPasswordUpdate2.text


        binding.buttonSaveUpdatePassword.setOnClickListener {
            if(password.isNullOrBlank() || passwordRepeat.isNullOrBlank()){
                Toast.makeText(this, "As passwords estão vazias!",Toast.LENGTH_SHORT).show()
            }else{
                if(password != passwordRepeat){
                    Toast.makeText(this, "As passwords não são iguais!",Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, passwordRepeat,Toast.LENGTH_SHORT).show()


                }else{
                    auth.currentUser?.updatePassword(password.toString())
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Password Editada com sucesso", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }
    }
}