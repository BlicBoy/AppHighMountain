package com.example.highmountain.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.highmountain.databinding.FragmentPercursoAddBinding


class PercursoAddFragment : Fragment() {

    private var _binding : FragmentPercursoAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPercursoAddBinding.inflate(inflater,  container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGuardarPercurso.setOnClickListener {

            if(binding.editTextDatePercurso.text.isNullOrBlank() ||  binding.editTextTextMultiLineDescricao.text.isNullOrBlank()){
                Toast.makeText(requireContext(), "Não preencheu todos os campos",Toast.LENGTH_SHORT).show()
            }else{

                Toast.makeText(requireContext(), "Criado com sucesso!",Toast.LENGTH_SHORT).show()
                findNavController().popBackStack() //provisório aqui
                //fazer depois para inserir na base de dados
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}