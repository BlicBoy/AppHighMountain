package com.example.highmountain.ui.utilizador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.VERBOSE
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.highmountain.R
import com.example.highmountain.databinding.ActivitySaudeBinding
import com.example.highmountain.ui.models.DataSaude
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_info_cliente_saude.*
import kotlinx.android.synthetic.main.activity_saude.*
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

        val arraySpinner = listOf<String>("A+","A-","B+","B-","AB+-","AB-","O+","O-")
        val arrayAdapter = ArrayAdapter(this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arraySpinner)
        spinnertiposanguesaude.adapter = arrayAdapter



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
                var position : String = data?.tipodeSangue.toString()
                var spinnerPosition = arrayAdapter.getPosition(position)
                spinnertiposanguesaude.setSelection(spinnerPosition)

                binding.editTextAlergias.setText(data?.alergias)
                binding.editTextDoencas.setText(data?.doencas)
            }

        val focusChange = object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if(!hasFocus){
                    when(view){
                        spinnertiposanguesaude->{
                            Log.w("Teste" , "Aqui!")
                            DataSaude.DataSaudeField(spinnertiposanguesaude.selectedItem.toString(), "tipodeSangue")
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

        spinnertiposanguesaude.onFocusChangeListener = focusChange
        binding.editTextAlergias.onFocusChangeListener = focusChange
        binding.editTextDoencas.onFocusChangeListener = focusChange



    }



}