package com.example.highmountain.ui.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


data class newUsers(
    var uId : String?,
    var FirstName : String?,
    var LastName : String?,
    var numeroTelemovel : String?,
    // var photoURL : String?,
    var dataNascimento : String?,
    var sexualidade : String?,
    var role : String?,
)
{

    //functions
    fun toHasMap() : HashMap<String, Any?>{
        return hashMapOf(
            "uId" to uId,
            "FirstName" to FirstName,
            "LastName" to LastName,
            "numeroTelemovel" to numeroTelemovel,
          //  "photoURL" to photoURL,
            "dataNascimento" to dataNascimento,
            "sexualidade" to sexualidade,
            "role" to role
        )
    }

    fun sendNewUser(callback : (error:String?) ->Unit){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val db = Firebase.firestore
        db.collection("newUsers").document(uid)
            .set(toHasMap())
            .addOnSuccessListener {
                callback(null)
            }
            .addOnFailureListener { e->
                callback(e.toString())
            }
    }
    companion object{



        fun newUserField(name: String, field: String){
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val db = Firebase.firestore
            db.collection("newUsers")
                .document(uid)
                .update(field, name)
        }


        fun fromDoc(doc : DocumentSnapshot) : newUsers{
            return newUsers(
                doc.getString("uId"),
                doc.getString("FirstName"),
                doc.getString("LastName"),
                doc.getString("numeroTelemovel"),
            //    doc.getString("photoURL"),
                doc.getString("dataNascimento"),
                doc.getString("sexualidade"),
                doc.getString("role")
                )
        }
    }
}