package com.example.highmountain.ui.dashboard

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import  android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.highmountain.databinding.FragmentMedicoesBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MedicoesFragment : Fragment() {

    private var _binding : FragmentMedicoesBinding?= null
    private val binding get() = _binding!!
    private lateinit var datagps : TextView
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding =  FragmentMedicoesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datagps = binding.textViewDatagps
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLocation()





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
                var longitude =it.longitude
                var latitude = it.latitude
                var altitude = Math.round(it.altitude.toFloat() - 56F)
              datagps.setText("Longitude:" + longitude.toString()+"Latitude:  "+ latitude.toString()+ "Altura: " + altitude.toString())
            }
        }
    }


    companion object {

    }
}