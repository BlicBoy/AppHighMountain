package com.example.highmountain.ui.utilizador

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.highmountain.R
import com.example.highmountain.databinding.ActivitySelecionarPercursoBinding
import com.example.highmountain.databinding.RowPercursoBinding
import com.example.highmountain.ui.models.Participantes


import com.example.highmountain.ui.models.Percursos
import com.example.highmountain.ui.models.newUsers

import com.google.android.material.bottomsheet.BottomSheetDialog

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class SelecionarPercursoActivity : AppCompatActivity() {


    lateinit var auth : FirebaseAuth
    lateinit var binding : ActivitySelecionarPercursoBinding

    var percursosList = arrayListOf<Percursos>()
    val adapter = SelectPercursoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecionar_percurso)

        supportActionBar?.hide()

        binding = ActivitySelecionarPercursoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val db = Firebase.firestore
        db.collection("newPercursos")
            .addSnapshotListener{value, error->
                percursosList.clear()
                for (doc in value?.documents!!){
                        percursosList.add(Percursos.fromDoc(doc))
                }
                adapter.notifyDataSetChanged()
            }

        binding.recyclerViewPercursoSelect.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewPercursoSelect.adapter = adapter
        binding.recyclerViewPercursoSelect.itemAnimator = DefaultItemAnimator()



    }

    override fun onDestroy() {
        super.onDestroy()
    }

    inner class SelectPercursoAdapter : RecyclerView.Adapter<SelectPercursoAdapter.ViewHolder>(){

        inner class ViewHolder(binding: RowPercursoBinding) : RecyclerView.ViewHolder(binding.root) {
            val textViewTituloPercurso : TextView = binding.textViewTitutloPercurso
            val textViewDataHoraPercurso : TextView = binding.textViewDataHoraPercurso
            val background = binding.cardViewPercursos
            val buttonInfo = binding.buttonmoreinfo
            val buttonEntrar = binding.buttonativarpercurso
        }

        override fun getItemCount(): Int {
            return  percursosList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(RowPercursoBinding.inflate(LayoutInflater.from(parent.context) , parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var itemPercursos = percursosList[position]

            holder.apply {


                buttonEntrar.setText("Inscrever Percurso")
                textViewTituloPercurso.text = itemPercursos.Nome
                textViewDataHoraPercurso.text = "Data de Inicio: " + itemPercursos.DataInicio

                val storage = Firebase.storage
                val storageRef = storage.reference
                var islandRef = storageRef.child("newUserPhotos/${itemPercursos.photoPercurso}")

                val ONE_MEGABYTE: Long = 10024 * 1024
                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                    val inputStream = it.inputStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    background.setBackgroundDrawable(BitmapDrawable(this@SelecionarPercursoActivity.resources, bitmap))

                    islandRef = storageRef.child("newUserPhotos/${itemPercursos.photoCriador}")
                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                        val input = it.inputStream()
                        var photoAdmin = BitmapFactory.decodeStream(input)
                        buttonInfo.setOnClickListener {
                            showDetailsPercurso(
                                bitmap,
                                itemPercursos.NomeCriador.toString(),
                                itemPercursos.DataCriacao.toString(),
                                itemPercursos.Descricao.toString(),
                                itemPercursos.DataInicio.toString(),
                                itemPercursos.HoraInicio.toString(),
                                photoAdmin
                            )

                        }

                        buttonEntrar.setOnClickListener {

                                newUsers.nameUserFromId { name ->
                                    Participantes(auth.currentUser?.uid.toString(),itemPercursos.id,name,auth.currentUser?.email).insertParticipation {
                                        error ->
                                        error?.let {
                                            Toast.makeText(this@SelecionarPercursoActivity, "Error", Toast.LENGTH_SHORT).show()
                                        }?: kotlin.run {
                                            Toast.makeText(this@SelecionarPercursoActivity, "Inscreveu-se no percurso!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                        }
                    }
                }
            }
        }


        private fun showDetailsPercurso(imagePercurso : Bitmap, nomeCriador : String, DataCriacaoPercurso : String, descricaoPercurso : String, datainicio: String, horaInicio: String, photoPerson : Bitmap){
            val dialog = BottomSheetDialog(this@SelecionarPercursoActivity)
            dialog.setContentView(R.layout.details_percursos)
            val photoPercurso : ImageView = dialog.findViewById<ImageView>(R.id.imageViewDetailsPercurso)!!
            val nomeAdmin : TextView = dialog.findViewById<TextView>(R.id.textViewDetailsPercursoAdminNome)!!
            val dataCriacao : TextView = dialog.findViewById<TextView>(R.id.editTextDataCriacaoPercurso)!!
            val descricao : TextView = dialog.findViewById<TextView>(R.id.textViewDetailsPercursoDescricao)!!
            val photoAdmin : ImageView = dialog.findViewById<ImageView>(R.id.imageViewDetailsPercursoAdmin)!!


            photoPercurso.setBackgroundDrawable(BitmapDrawable(this@SelecionarPercursoActivity.resources, imagePercurso))
            nomeAdmin.text = nomeCriador
            dataCriacao.text = "Data de Criação de Percurso: " + DataCriacaoPercurso
            descricao.text = "Data de Inicio: " + datainicio + "\nHora de Inicio:"+ horaInicio+"\nDescrição:"+descricaoPercurso
            photoAdmin.setImageBitmap(photoPerson)


            dialog.show()
        }

    }
}