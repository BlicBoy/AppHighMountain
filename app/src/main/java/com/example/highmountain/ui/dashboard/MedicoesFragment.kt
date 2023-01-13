package com.example.highmountain.ui.dashboard

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.highmountain.R
import com.example.highmountain.databinding.FragmentMedicoesBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.lang.Integer.parseInt
import kotlin.math.roundToInt

class MedicoesFragment : Fragment() {

    private var _binding : FragmentMedicoesBinding? = null
    private val binding get() = _binding!!


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latitude = 0.0
    private var longitude = 0.0
    private var altitude = 0
    private  var rndsOxig = 0
    private var rndsBati = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMedicoesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLocation()

        val bundle = this.arguments
        if (bundle != null) {
            val documentId = bundle.getString("uIdUtilizador", "")
        }


        binding.buttonSimular.setOnClickListener {
            simulateDataMedicoes()

            var run = validationData(rndsOxig, rndsBati)

            if(run){
                binding.textViewSeguir.setTextColor(Color.parseColor("#0fa602"))
                binding.textViewSeguir.text = "Pode Seguir"
                //Toast.makeText(requireContext(), "Pode Seguir", Toast.LENGTH_SHORT).show()

            }else{
                binding.textViewSeguir.setTextColor(Color.parseColor("#a60202"))

                binding.textViewSeguir.text = "Não aconselhavel"
               // Toast.makeText(requireContext(), "Não aconselhavel", Toast.LENGTH_SHORT).show()
            }
        }


        binding.buttonvalidarmedioes.setOnClickListener {
            val oxigenio = binding.editTextPercentagemOxigenio.text
            if(!oxigenio.isNullOrBlank()){
                var run = validationData(parseInt(oxigenio.toString()), altitude)

                if(run){
                    binding.textViewSeguir.setTextColor(Color.parseColor("#0fa602"))
                    binding.textViewSeguir.text = "Pode Seguir"
                }else{
                    binding.textViewSeguir.setTextColor(Color.parseColor("#a60202"))
                    binding.textViewSeguir.text = "Não aconselhavel"
                }
            }else{
                Toast.makeText(requireContext(), "Tem de preencher pelo menos o campo do oxigénio", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun simulateDataMedicoes(){
         rndsOxig = (78..99).random()
         rndsBati = (70..140).random()

        binding.editTextPercentagemOxigenio.setText(rndsOxig.toString())
        binding.editTextMedicoesNumBatimentos.setText(rndsBati.toString())
    }

    private fun validationData(nivelOxigenio : Int, alt : Int) : Boolean{

        if (nivelOxigenio in 98 .. 100 && alt in 0 .. 562){
            return true
        }else if (nivelOxigenio in 95 .. 97 && alt in 562 .. 2335){
            return true
        }else if (nivelOxigenio in 92 .. 95 && alt in 2335 .. 3250){
            return true
        }else if (nivelOxigenio in 88 .. 95 && alt in 2335 .. 3950){
            return true
        }else if (nivelOxigenio in 85 .. 90 && alt in 3950 .. 4500){
            return true
        }else if (nivelOxigenio in 78 .. 85 && alt in 4500 .. 5100){
            return true
        }

        return false


    }

    private fun getLocation(){
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }



        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if(it != null){
                longitude =it.longitude
                latitude = it.latitude
                altitude = Math.round(it.altitude - 56).toInt()

                binding.textViewaltitude.text = (((it.altitude-56)*10000.0).roundToInt() / 10000.0).toString() + " m"


            }
        }
    }




}