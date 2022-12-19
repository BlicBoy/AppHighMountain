package com.example.highmountain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.highmountain.databinding.ActivitySplashBinding
import com.example.highmountain.ui.PREF_CODE
import com.example.highmountain.ui.role_user
import com.example.highmountain.ui.utilizador.ClienteActivity
import com.example.highmountain.ui.utilizador.InfoClienteActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()


        lifecycleScope.launch(Dispatchers.IO){
            delay(2000)
            lifecycleScope.launch(Dispatchers.Main){
                val auth = Firebase.auth
             val currentUser = auth.currentUser
           if (currentUser != null){
              if(this@SplashActivity.role_user == "Cliente"){
                  startActivity(Intent(this@SplashActivity,ClienteActivity::class.java))
              }else{
                  if (this@SplashActivity.PREF_CODE.isNullOrBlank()){
                      if(this@SplashActivity.role_user == "Administrador"){
                          startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                      }else{
                          startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
                      }
                  }else{
                      startActivity(Intent(this@SplashActivity,CodeActivity::class.java))
                  }
              }
           }
           else{
              startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
              // startActivity(Intent(this@SplashActivity,InfoClienteActivity::class.java))
           }
                finish()
            }
        }
    }
}