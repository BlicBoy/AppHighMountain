package com.example.highmountain.ui.dashboard

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.highmountain.R
import com.example.highmountain.databinding.FragmentListPercursoBinding
import com.example.highmountain.databinding.RowPercursoBinding
import com.example.highmountain.ui.models.Percursos
import com.example.highmountain.ui.percursoAtivo
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ListPercursoFragment : Fragment() {

    private var _binding : FragmentListPercursoBinding? = null
    private val binding get() = _binding!!

    var percursosList = arrayListOf<Percursos>()
    val adapter = PercursosAdapter()


    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=  FragmentListPercursoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        val db = Firebase.firestore
        db.collection("newPercursos")
            .addSnapshotListener{value, error->
                percursosList.clear()
                for (doc in value?.documents!!){
                    if(doc.getString("IdCriador") == auth.currentUser?.uid){
                        percursosList.add(Percursos.fromDoc(doc))
                    }
                }
                adapter.notifyDataSetChanged()
            }

        binding.recyclerViewPercursos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewPercursos.adapter = adapter
        binding.recyclerViewPercursos.itemAnimator = DefaultItemAnimator()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class PercursosAdapter : RecyclerView.Adapter<PercursosAdapter.ViewHolder>(){
        inner class ViewHolder(binding: RowPercursoBinding) : RecyclerView.ViewHolder(binding.root) {

            val textViewTituloPercurso : TextView = binding.textViewTitutloPercurso
            val textViewDataHoraPercurso : TextView = binding.textViewDataHoraPercurso
            val background = binding.cardViewPercursos
            val buttonInfo = binding.buttonmoreinfo
            val buttonActivatePercursos = binding.buttonativarpercurso


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                RowPercursoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }



        @SuppressLint("SuspiciousIndentation")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var itemPercursos = percursosList[position]

                holder.apply {


                    if(this@ListPercursoFragment.requireContext().percursoAtivo == itemPercursos.id){
                        buttonActivatePercursos.setText("Ativado")
                    }else{
                        buttonActivatePercursos.setText("Ativar Percurso")
                    }

                    textViewTituloPercurso.text = itemPercursos.Nome
                    textViewDataHoraPercurso.text = "Data de Inicio: " + itemPercursos.DataInicio
                    //background.setBackgroundResource(R.drawable.mountains)

                    val storage = Firebase.storage
                    val storageRef = storage.reference
                    var islandRef = storageRef.child("newUserPhotos/${itemPercursos.photoPercurso}")

                    val ONE_MEGABYTE: Long = 10024 * 1024
                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                        val inputStream = it.inputStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        background.setBackgroundDrawable(BitmapDrawable(context?.resources, bitmap))

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

                            buttonActivatePercursos.setOnClickListener{
                                    this@ListPercursoFragment.requireContext().percursoAtivo = itemPercursos.id
                                    Toast.makeText(requireContext(), "Ativo Percurso: " + itemPercursos.Nome ,Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.action_listPercursoFragment_to_medicoesFragment)

                            }

                        }
                    }
            }

        }


        private fun showDetailsPercurso(imagePercurso : Bitmap, nomeCriador : String, DataCriacaoPercurso : String, descricaoPercurso : String, datainicio: String, horaInicio: String, photoPerson : Bitmap ){
            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(R.layout.details_percursos)
            val photoPercurso : ImageView = dialog.findViewById<ImageView>(R.id.imageViewDetailsPercurso)!!
            val nomeAdmin : TextView = dialog.findViewById<TextView>(R.id.textViewDetailsPercursoAdminNome)!!
            val dataCriacao : TextView = dialog.findViewById<TextView>(R.id.editTextDataCriacaoPercurso)!!
            val descricao : TextView = dialog.findViewById<TextView>(R.id.textViewDetailsPercursoDescricao)!!
            val photoAdmin : ImageView = dialog.findViewById<ImageView>(R.id.imageViewDetailsPercursoAdmin)!!


            photoPercurso.setBackgroundDrawable(BitmapDrawable(context?.resources, imagePercurso))
            nomeAdmin.text = nomeCriador
            dataCriacao.text = "Data de Criação de Percurso: " + DataCriacaoPercurso
            descricao.text = "Data de Inicio: " + datainicio + "\nHora de Inicio:"+ horaInicio+"\nDescrição:"+descricaoPercurso
            photoAdmin.setImageBitmap(photoPerson)


            dialog.show()
        }

        override fun getItemCount(): Int {
            return percursosList.size
        }

    }
}