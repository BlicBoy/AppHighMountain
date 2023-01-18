package com.example.highmountain.ui.utilizador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.highmountain.R
import com.example.highmountain.databinding.ActivityViewDataBinding
import com.example.highmountain.databinding.FragmentMedicoesBinding
import com.example.highmountain.databinding.RowPercursoBinding
import com.example.highmountain.databinding.RowparticipacoesclienteBinding
import com.example.highmountain.databinding.RowparticipantesBinding
import com.example.highmountain.ui.home.HomeFragment
import com.example.highmountain.ui.models.Participantes
import com.example.highmountain.ui.models.Percursos
import com.example.highmountain.ui.percursoAtivo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewDataActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var binding: ActivityViewDataBinding


    val PercursoParticipateList = arrayListOf<Participantes>()
    val adapter = percursoAtivoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data)

        supportActionBar?.hide()
        binding = ActivityViewDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth


        val db = Firebase.firestore
        db.collection("Participantes")
            .addSnapshotListener{value, error->
                PercursoParticipateList.clear()
                for (doc in value?.documents!!){
                    if(doc.getString("uIdParticipante") == auth.currentUser?.uid){
                        PercursoParticipateList.add(Participantes.fromDoc(doc))
                    }
                }
                adapter.notifyDataSetChanged()
            }


        binding.recyclerViewParticipacoes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewParticipacoes.adapter = adapter
        binding.recyclerViewParticipacoes.itemAnimator = DefaultItemAnimator()

        if(PercursoParticipateList.size <= 0){
            Toast.makeText(this,  auth.currentUser?.uid, Toast.LENGTH_SHORT).show()
        }

    }



    inner class  percursoAtivoAdapter : RecyclerView.Adapter<percursoAtivoAdapter.ViewHolder>(){

        inner class ViewHolder(binding: RowparticipacoesclienteBinding) : RecyclerView.ViewHolder(binding.root){
            val NamePercursi : TextView = binding.textViewParticName
            val buttonMore : Button =  binding.buttonseemedic
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(RowparticipacoesclienteBinding.inflate(LayoutInflater.from(parent.context) , parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var itemPercursos = PercursoParticipateList[position]

            holder.apply {
                NamePercursi.text = itemPercursos.nomePercurso
            }

        }

        override fun getItemCount(): Int {
            return  PercursoParticipateList.size
        }


    }
}