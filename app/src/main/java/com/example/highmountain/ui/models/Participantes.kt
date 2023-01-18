package com.example.highmountain.ui.models

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

data class Participantes(
    var uIdParticipante: String?, //ser random
    var uIdPercurso: String?,
    var nomePercurso : String?,
    var nomeParticipante: String?,
    var email: String?
) {

    fun toHasMap() : HashMap<String, Any?>{
        return hashMapOf(
            "uIdParticipante" to uIdParticipante,
            "uIdPercurso" to uIdPercurso,
            "nomePercurso" to nomePercurso,
            "nomeParticipante" to nomeParticipante,
            "email" to email
        )
    }

    fun insertParticipation(callback : (error:String?) -> Unit){
        val db = Firebase.firestore
        db.collection("Participantes")
            .document(UUID.randomUUID().toString())
            .set(toHasMap())
            .addOnSuccessListener {
                callback(null)
            }
            .addOnFailureListener {
                callback(it.toString())
            }
    }



    companion object{
        fun fromDoc(doc : DocumentSnapshot) : Participantes{
            return Participantes(
                doc.getString("uIdParticipante"),
                doc.getString("uIdPercurso"),
                doc.getString("nomePercurso"),
                doc.getString("nomeParticipante"),
                doc.getString("email")
            )
        }
    }



}