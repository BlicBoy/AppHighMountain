package com.example.highmountain.ui.notifications

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.highmountain.R
import com.example.highmountain.databinding.FragmentCodeBinding



class CodeFragment : Fragment() {

    private var _binding : FragmentCodeBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCodeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = activity?.getSharedPreferences("code", Context.MODE_PRIVATE)
        var editor = sharedPreferences?.edit()

        binding.buttonGuardarCode.setOnClickListener {
            editor?.putString("code", binding.editTextCode.text.toString())
            editor?.commit()
            Toast.makeText(requireContext(), "Codigo Registrado com Sucesso!", Toast.LENGTH_LONG).show()
        }

        binding.button.setOnClickListener {
            var code = sharedPreferences?.getString("code","0000")
            Toast.makeText(requireContext(), code, Toast.LENGTH_LONG).show()
        }


    }




    companion object {

    }
}