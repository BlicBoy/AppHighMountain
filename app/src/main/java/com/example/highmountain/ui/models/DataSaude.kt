package com.example.highmountain.ui.models

import android.os.health.UidHealthStats
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.DOMConfiguration
import javax.security.auth.callback.Callback

data class DataSaude(
    var tipodeSangue : String?,
    var alergias: String?,
    var doencas : String?,
) {

    fun toHaspMap() : HashMap<String, Any?>{
        return hashMapOf(
            "tipodeSangue" to tipodeSangue,
            "alergias" to alergias,
            "doencas" to doencas
        )
    }

    fun saveDataSaude(callback: (error:String?) -> Unit){

        val uId = FirebaseAuth.getInstance().currentUser!!.uid
        val db = Firebase.firestore
        db.collection("newUsers")
            .document(uId)
            .collection("Saude")
            .document(uId)
            .set(toHaspMap())
            .addOnSuccessListener {
                callback(null)
            }.addOnFailureListener {
                callback(it.toString())
            }
    }

    companion object{
        fun DataSaudeField (name : String , field : String){
            val uId = FirebaseAuth.getInstance().currentUser!!.uid
            val db = Firebase.firestore

            db.collection("newUsers")
                .document(uId)
                .collection("Saude")
                .document(uId)
                .update(field, name)
        }

        fun fromDoc(doc : DocumentSnapshot) : DataSaude{
            return DataSaude(
                doc.getString("tipodeSangue"),
                doc.getString("alergias"),
                doc.getString("doencas")
            )
        }



    }

}