package com.example.highmountain.ui.utilizador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.highmountain.R
import com.example.highmountain.databinding.ActivityMostramedicoesBinding
import com.example.highmountain.databinding.RowmedicoesclienteBinding
import com.example.highmountain.ui.models.Medicoes
import com.example.highmountain.ui.percursoMostraMedicoes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class mostramedicoesActivity : AppCompatActivity() {


    lateinit var binding : ActivityMostramedicoesBinding
    private lateinit var auth : FirebaseAuth


    var medicoesList = arrayListOf<Medicoes>()
    val adapter = MedicoesClienteAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostramedicoes)

        binding = ActivityMostramedicoesBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)


        auth = Firebase.auth
        val db = Firebase.firestore

        db.collection("Medicoes")
            .addSnapshotListener { value, error ->
                medicoesList.clear()
                for(doc in value?.documents!!){
                    if(doc.getString("uIdPartipante") == auth.currentUser?.uid.toString() && doc.getString("uIdPercurso") == this@mostramedicoesActivity.percursoMostraMedicoes){
                        
                    }
                }

            }
    }

    inner class MedicoesClienteAdapter : RecyclerView.Adapter<MedicoesClienteAdapter.ViewHolder>(){
        inner class ViewHolder(binding : RowmedicoesclienteBinding) : RecyclerView.ViewHolder(binding.root) {
            val idPercurso : TextView = binding.textViewIdPercursos
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

    }
}