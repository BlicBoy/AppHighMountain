package com.example.highmountain.ui

import android.content.Context

object Preferences {
    const val PREF_CODE = "pref_code"
    const val role_user = "role_user"
}

var Context.PREF_CODE : String?
    get(){
        val sharedPref = getSharedPreferences(Preferences.PREF_CODE, Context.MODE_PRIVATE) ?:  return null
        return sharedPref.getString(Preferences.PREF_CODE, "")
    }
    set(value){
        val sharedPref = getSharedPreferences(Preferences.PREF_CODE, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            if(value == "apagar"){
                clear()
            }else if (value != null) {
                putString(Preferences.PREF_CODE, value.toString())
            }
            commit()
        }
    }


var Context.role_user : String?
    get(){
        val sharedPref = getSharedPreferences(Preferences.role_user, Context.MODE_PRIVATE) ?:  return null
        return sharedPref.getString(Preferences.role_user, "")
    }
    set(value){
        val sharedPref = getSharedPreferences(Preferences.role_user, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            if(value == "apagar"){
              clear()
            } else if (value != null) {
                putString(Preferences.role_user, value.toString())
            }
            commit()
        }
    }



