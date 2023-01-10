package com.example.highmountain.ui.models

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.widget.Toast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY


object Location {


    fun locationRequestUser(){
        val locationRequest = LocationRequest.create().apply {
            interval = 3000
            priority = PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val task = LocationServices.getSettingsClient(Activity())
            .checkLocationSettings(builder.build())


        task
            .addOnSuccessListener {
                response ->

                val states = response.locationSettingsStates
                if(states!!.isLocationPresent){
                    Toast.makeText(Activity(), "GPS DETECTADO COM SUCESSO!", Toast.LENGTH_SHORT).show()
                }
            }

            .addOnFailureListener { e ->
                val statusCode = (e as ResolvableApiException).statusCode
                if(statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                    try {
                        Toast.makeText(Activity(), "GPS NÃO DETECTADO!", Toast.LENGTH_SHORT).show()

                    }catch (sendEx: IntentSender.SendIntentException){}
                }
            }

    }
}