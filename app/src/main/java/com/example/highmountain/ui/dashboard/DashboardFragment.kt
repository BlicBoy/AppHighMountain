package com.example.highmountain.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.highmountain.R
import com.example.highmountain.databinding.FragmentDashboardBinding
import com.example.highmountain.databinding.RowPercursoBinding
import com.example.highmountain.ui.models.Percursos
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.row_percurso.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {



    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    var percursosList = arrayListOf<Percursos>()
    val adapter = PercursosAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddPercurso.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dashboard_to_percursoAddFragment)
        }


        val db = Firebase.firestore
        db.collection("newPercursos")
            .addSnapshotListener{value, error->
                percursosList.clear()
                for (doc in value?.documents!!){
                    percursosList.add(Percursos.fromDoc(doc))
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

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var itemPercursos = percursosList[position]


            holder.apply {

                textViewTituloPercurso.text = itemPercursos.Nome
                textViewDataHoraPercurso.text = "Data de Inicio: " + itemPercursos.DataInicio
            }


        }

        override fun getItemCount(): Int {
            return percursosList.size
        }

    }
}