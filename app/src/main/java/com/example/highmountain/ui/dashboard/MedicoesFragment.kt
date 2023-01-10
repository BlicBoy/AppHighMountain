package com.example.highmountain.ui.dashboard

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.fragment.app.Fragment
import com.example.highmountain.databinding.FragmentMedicoesBinding


class MedicoesFragment : Fragment() {

    private var _binding : FragmentMedicoesBinding?= null
    private val binding get() = _binding!!





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding =  FragmentMedicoesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun checkGPSEnabled(): Boolean {
        if (!isLocationEnabled())
        return isLocationEnabled()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       when(requestCode){
           REQUEST_LOCATION_CODE -> {
               // If request is cancelled, the result arrays are empty.
               if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   // permission was granted, yay! Do the location-related task you need to do.
                   if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                       Toast.makeText(requireContext(), "permission granted", Toast.LENGTH_LONG).show()
                   }
               } else {
                   // permission denied, boo! Disable the functionality that depends on this permission.
                   Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_LONG).show()
               }
               return
           }
       }

    }




    companion object {

        val LOCATION_PERMISSION_CODE = 1000
        val REQUEST_LOCATION_CODE = 101

    }
}