package com.example.highmountain.ui.notifications

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.highmountain.databinding.FragmentCodeBinding
import com.example.highmountain.ui.PREF_CODE

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
            sendCodePin(binding.editTextCode.text.toString())
            Toast.makeText(requireContext(), "Codigo Registrado com Sucesso!", Toast.LENGTH_LONG).show()
        }
        binding.btLimparCodigo.setOnClickListener {
            deleteCodePin()
            Toast.makeText(requireContext(), "Pin apagado com sucesso!", Toast.LENGTH_LONG).show()
        }
    }

    private fun sendCodePin(pin : String){
        Log.d(TAG, "Pin guardado")
        requireContext().PREF_CODE = pin
    }
    private fun deleteCodePin(){
        Log.d(TAG, "Pin apagado")
        requireContext().PREF_CODE = "apagar"
    }


    companion object {
        private const val TAG = "Code pin"
    }
}