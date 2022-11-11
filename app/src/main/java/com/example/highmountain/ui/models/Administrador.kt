package com.example.highmountain.ui.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Administrador
    (
        var uId : String?,
        var FirstName : String?,
        var LastName : String?,
        var urlPhoto : String?,
        var dataNascimento : String?,
        var numeroTelemovel : String?,
            ) {

        //functions
        fun toHasMap() : HashMap<String, Any?>{
            return hashMapOf(
                "uId" to uId,
                "FirstName" to FirstName,
                "LastName" to LastName,
                "urlPhoto" to urlPhoto,
                "dataNascimento" to dataNascimento,
                "numeroTelemovel" to numeroTelemovel
            )
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
                doc.getString("urlPhoto"),
                doc.getString("dataNascimento"),
                doc.getString("numeroTelemovel")
                )
        }
    }



}