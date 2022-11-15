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
import com.example.highmountain.ui.Preferences.PREF_CODE


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

        binding.buttonGuardarCode.setOnClickListener {
            PREF_CODE = binding.editTextCode.text.toString()
            Toast.makeText(requireContext(), "Codigo Registrado com Sucesso!", Toast.LENGTH_LONG).show()
        }

        binding.button.setOnClickListener {
            Toast.makeText(requireContext(), PREF_CODE, Toast.LENGTH_LONG).show()
        }


    }




    companion object {

    }
}