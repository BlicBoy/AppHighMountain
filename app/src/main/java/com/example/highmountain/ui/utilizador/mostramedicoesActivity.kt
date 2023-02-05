package com.example.highmountain.ui.utilizador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
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

        val imageViewBack: ImageView = findViewById(R.id.imageView17)

        imageViewBack.setOnClickListener {
            onBackPressed()
        }


        auth = Firebase.auth
        val db = Firebase.firestore

        db.collection("Medicoes")
            .addSnapshotListener { value, error ->
                medicoesList.clear()
                for(doc in value?.documents!!){
                    if(doc.getString("uIdPartipante") == auth.currentUser?.uid.toString() && doc.getString("uIdPercurso") == this@mostramedicoesActivity.percursoMostraMedicoes){
                        medicoesList.add(Medicoes.fromDocMedicoes(doc))
                    }
                }
                adapter.notifyDataSetChanged()
            }

            binding.recyclerMedicoes.layoutManager = LinearLayoutManager(this@mostramedicoesActivity,LinearLayoutManager.VERTICAL,false)
            binding.recyclerMedicoes.adapter = adapter
            binding.recyclerMedicoes.itemAnimator = DefaultItemAnimator()
    }

    inner class MedicoesClienteAdapter : RecyclerView.Adapter<MedicoesClienteAdapter.ViewHolder>(){
        inner class ViewHolder(binding : RowmedicoesclienteBinding) : RecyclerView.ViewHolder(binding.root) {
            val idPercurso : TextView = binding.textViewIdPercursos
            val localizacao : TextView = binding.textViewLocalizacao
            val oxigenio : TextView = binding.textViewoxigenio
            val pulsacao : TextView = binding.textViewpulsacao
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(RowmedicoesclienteBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var itemMedicoesCliente = medicoesList[position]

            holder.apply {
                idPercurso.text = itemMedicoesCliente.uIdPercurso
                localizacao.text = "Localização: Latitude:"+ itemMedicoesCliente.Latitude +  " Longitude:"+itemMedicoesCliente.Longitude+" Altura:"+ itemMedicoesCliente.Altura
                oxigenio.text = "Oxigenio: "+itemMedicoesCliente.nivelOxigenio
                pulsacao.text = "Pulsação: "+itemMedicoesCliente.batimentoCardiaco
            }
        }

        override fun getItemCount(): Int {
            return medicoesList.size
        }

    }
}