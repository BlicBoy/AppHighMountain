package com.example.highmountain.ui.models

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


data class newUsers(
    var uId : String?,
    var FirstName : String?,
    var LastName : String?,
    var numeroTelemovel : String?,
    var photoURL : String?,
    var dataNascimento : String?,
    var sexualidade : String?,
    var role : String?,
    var tipodeSangue : String?,
    var doencas : String?,
    var alergias: String?,

)
{

    //functions
    fun toHasMap() : HashMap<String, Any?>{
        return hashMapOf(
            "uId" to uId,
            "FirstName" to FirstName,
            "LastName" to LastName,
            "numeroTelemovel" to numeroTelemovel,
            "photoURL" to photoURL,
            "dataNascimento" to dataNascimento,
            "sexualidade" to sexualidade,
            "role" to role,
            "tipodeSangue" to tipodeSangue,
            "doencas" to doencas,
            "alergias" to alergias
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
                doc.getString("photoURL"),
                doc.getString("dataNascimento"),
                doc.getString("sexualidade"),
                doc.getString("role"),
                doc.getString("tipodeSangue"),
                doc.getString("doencas"),
                doc.getString("alergias")
                )
        }


        fun nameUserFromId(callback : (name:String?) ->Unit){
            val uid = FirebaseAuth.getInstance().currentUser!!.uid

            val db = Firebase.firestore
            db.collection("newUsers").document(uid)
                .addSnapshotListener { value, error ->
                    val user = value?.let {
                        newUsers.fromDoc(it)
                    }

                    callback(user?.FirstName.toString())
                }
        }
    }
}