package com.example.highmountain.ui.models

import com.google.firebase.firestore.DocumentSnapshot

data class Participantes(
    var uIdParticipante : String?, //ser random
    var uIdPercurso : String?,
    var nomeParticipante : String?,
    var email : String?,
) {


    companion object{
        fun fromDoc(doc : DocumentSnapshot) : Participantes{
            return Participantes(
                doc.getString("uIdParticipante"),
                doc.getString("uIdPercurso"),
                doc.getString("nomeParticipante"),
                doc.getString("email")
            )
        }
    }



}