package com.example.highmountain

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.highmountain.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        supportActionBar?.hide()

        navView.menu.performIdentifierAction(R.id.navigation_dashboard,0)

        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }

       locationRequestUser()
    }

    fun locationRequestUser(){
        val locationRequest = LocationRequest.create().apply {
            interval = 3000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val task = LocationServices.getSettingsClient(applicationContext)
            .checkLocationSettings(builder.build())


        task
            .addOnSuccessListener {
                    response ->

                val states = response.locationSettingsStates
                if(states!!.isLocationPresent){
                    //Toast.makeText(applicationContext, "GPS DETECTADO COM SUCESSO!", Toast.LENGTH_SHORT).show()
                }
            }

            .addOnFailureListener { e ->
                val statusCode = (e as ResolvableApiException).statusCode
                if(statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                    try {
                        Toast.makeText(applicationContext, "GPS NÃO DETECTADO!", Toast.LENGTH_SHORT).show()
                        e.startResolutionForResult(this, 100)

                    }catch (sendEx: IntentSender.SendIntentException){}
                }
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100){
            if(resultCode == RESULT_OK){
               startActivity(intent)
                overridePendingTransition(0, 1)
            }
        }
    }
}