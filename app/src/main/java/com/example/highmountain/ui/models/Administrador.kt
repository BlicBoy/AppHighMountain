package com.example.highmountain.ui.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.security.auth.callback.Callback

data class Administrador
    (
        var uId : String?,
        var FirstName : String?,
        var LastName : String?,
        var photoFilename : String?,
        var dataNascimento : String?,
        var numeroTelemovel : String?,
            ) {

        //functions
        fun toHasMap() : HashMap<String, Any?>{
            return hashMapOf(
                "uId" to uId,
                "FirstName" to FirstName,
                "LastName" to LastName,
                "photoFilename" to photoFilename,
                "dataNascimento" to dataNascimento,
                "numeroTelemovel" to numeroTelemovel
            )
        }

        fun sendAdmin(callback: (error:String?)->Unit){
            val db = Firebase.firestore
            db.collection("administradores")
                .add(toHasMap())
                .addOnSuccessListener {
                    callback(null)
                }
                .addOnFailureListener { e->
                    callback(e.toString())
                }
        }

    companion object{

        fun postField(name:String, field:String){
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val db = Firebase.firestore
            db.collection("administradores")
                .document(uid )
                .update(field,name)
        }

        fun fromDoc(doc: DocumentSnapshot) : Administrador{
            return Administrador(
                doc.getString("uId"),
                doc.getString("FirstName"),
                doc.getString("LastName"),
                doc.getString("photoFilename"),
                doc.getString("dataNascimento"),
                doc.getString("numeroTelemovel")
                )
        }
    }



}