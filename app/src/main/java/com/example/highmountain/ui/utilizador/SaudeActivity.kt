package com.example.highmountain.ui.utilizador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.highmountain.R
import com.example.highmountain.databinding.ActivitySaudeBinding
import com.example.highmountain.ui.models.DataSaude
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class SaudeActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    lateinit var binding : ActivitySaudeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saude)

        binding = ActivitySaudeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()




        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = Firebase.firestore
        db.collection("newUsers")
            .document(uid)
            .collection("Saude")
            .document(uid)
            .addSnapshotListener{ value , error ->
                val data = value?.let {
                    DataSaude.fromDoc(it)
                }
                binding.editTextTipoSangue.setText(data?.tipodeSangue)
                binding.editTextAlergias.setText(data?.alergias)
                binding.editTextDoencas.setText(data?.doencas)
            }

        val focusChange = object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if(!hasFocus){
                    when(view){
                        binding.editTextTipoSangue ->{
                            DataSaude.DataSaudeField(binding.editTextTipoSangue.text.toString(), "tipodeSangue")
                        }
                        binding.editTextAlergias->{
                            DataSaude.DataSaudeField(binding.editTextAlergias.text.toString(), "alergias")
                        }
                        binding.editTextDoencas->{
                            DataSaude.DataSaudeField(binding.editTextDoencas.text.toString(), "doencas")
                        }
                    }
                }

            }
        }

        binding.editTextTipoSangue.onFocusChangeListener = focusChange
        binding.editTextAlergias.onFocusChangeListener = focusChange
        binding.editTextDoencas.onFocusChangeListener = focusChange



    }



}