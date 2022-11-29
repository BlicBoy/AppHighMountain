package com.example.highmountain.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.highmountain.R
import com.example.highmountain.databinding.FragmentPercursoAddBinding
import com.example.highmountain.ui.models.AppDatabase
import com.example.highmountain.ui.models.Percurso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID


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
                lifecycleScope.launch(Dispatchers.IO){
                    AppDatabase.getDatabase(requireContext())?.percursosDao()?.insert(
                        Percurso(
                            UUID.randomUUID().toString(),
                            binding.editTextDatePercurso.text.toString(),
                            FirebaseAuth.getInstance().currentUser!!.uid.toString(),
                            binding.editTextTextMultiLineDescricao.text.toString()
                        )
                    )

                }
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