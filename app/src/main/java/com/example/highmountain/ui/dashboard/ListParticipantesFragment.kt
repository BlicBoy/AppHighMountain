package com.example.highmountain.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.highmountain.R
import com.example.highmountain.databinding.FragmentListparticipantesBinding
import com.example.highmountain.databinding.RowparticipantesBinding
import com.example.highmountain.ui.models.Participantes
import com.example.highmountain.ui.participanteAtivo
import com.example.highmountain.ui.percursoAtivo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ListParticipantesFragment : Fragment() {

    private var _binding : FragmentListparticipantesBinding?= null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    var participantesList = arrayListOf<Participantes>()
    val adapter = ParticipantesAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding =  FragmentListparticipantesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore
        db.collection("Participantes").addSnapshotListener { value, error ->
            participantesList.clear()
            for (doc in value?.documents!!){
                if(doc.getString("uIdPercurso") == this@ListParticipantesFragment.requireContext().percursoAtivo){
                    participantesList.add(Participantes.fromDoc(doc))
                }
            }

            adapter.notifyDataSetChanged()
        }

        binding.recyclerViewParticipantes.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewParticipantes.adapter = adapter
        binding.recyclerViewParticipantes.itemAnimator = DefaultItemAnimator()

        binding.buttonTerminarPercurso.setOnClickListener {
            requireContext().percursoAtivo = "apagar"
            requireContext().participanteAtivo = "apagar"
            findNavController().popBackStack()
        }

        binding.imageView11.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    inner class ParticipantesAdapter : RecyclerView.Adapter<ParticipantesAdapter.ViewHolder>(){

        inner class ViewHolder(binding:RowparticipantesBinding) : RecyclerView.ViewHolder(binding.root) {
            val textViewNomeParticipante : TextView = binding.textViewListParticipante
            val textViewEmailParticipante : TextView = binding.textViewListParticpanteEmail
            val buttonMedicoes : Button = binding.buttonfazermedicao
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                RowparticipantesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var itemParticipante = participantesList[position]

            holder.apply {
                textViewEmailParticipante.text = itemParticipante.email
                textViewNomeParticipante.text = itemParticipante.nomeParticipante

                buttonMedicoes.setOnClickListener {

                    requireContext().participanteAtivo = itemParticipante.uIdParticipante.toString()
                    findNavController().navigate(R.id.action_listParticipantesFragment_to_medicoesFragment)

                }
            }
        }

        override fun getItemCount(): Int {
            return participantesList.size
        }


    }



}