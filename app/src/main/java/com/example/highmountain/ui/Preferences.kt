package com.example.highmountain.ui

import android.content.Context

object Preferences {
    var PREF_CODE = "pref-code"
}

var Context.PREF_CODE : String?
    get(){
        val sharedPref = getSharedPreferences(Preferences.PREF_CODE, Context.MODE_PRIVATE) ?:  return null
        return sharedPref.getString(Preferences.PREF_CODE, "")
    }
    set(value){
        val sharedPref = getSharedPreferences(Preferences.PREF_CODE, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            if (value != null) {
                putString(Preferences.PREF_CODE, value.toString())
            }
            commit()
        }
    }