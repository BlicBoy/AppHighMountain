package com.example.highmountain.ui.dashboard

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Delete
import com.example.highmountain.R
import com.example.highmountain.databinding.FragmentDashboardBinding
import com.example.highmountain.databinding.RowPercursoBinding
import com.example.highmountain.ui.models.AppDatabase
import com.example.highmountain.ui.models.Percurso
import kotlinx.android.synthetic.main.row_percurso.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    var percurso = arrayListOf<Percurso>()

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

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

        binding.recyclerViewPercursos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewPercursos.adapter = adapter
        binding.recyclerViewPercursos.itemAnimator = DefaultItemAnimator()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        AppDatabase.getDatabase(requireContext())?.percursosDao()?.getAll()?.observe(viewLifecycleOwner, Observer {
            percurso = it as ArrayList<Percurso>
            adapter.notifyDataSetChanged()
        })
    }


    inner class PercursosAdapter : RecyclerView.Adapter<PercursosAdapter.ViewHolder>(){
        inner class ViewHolder(binding: RowPercursoBinding) : RecyclerView.ViewHolder(binding.root) {

            val textViewTituloPercurso : TextView = binding.textViewTitutloPercurso
            val textViewDataHoraPercurso : TextView = binding.textViewDataHoraPercurso
            val BotaoDelete : Button = binding.buttonDelete


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

            var percurso = percurso[position]

            holder.apply {
                textViewTituloPercurso.text = percurso.descricaoPercurso
                textViewDataHoraPercurso.text = percurso.dataPercurso

                //Fazer aqui depois as ações clicar etc...

                BotaoDelete.setOnClickListener {
                    lifecycleScope.launch(Dispatchers.IO){
                        AppDatabase.getDatabase(requireContext())?.percursosDao()?.delete(percurso)
                    }
                }
            }





        }

        override fun getItemCount(): Int {
            return percurso.size
        }

    }
}