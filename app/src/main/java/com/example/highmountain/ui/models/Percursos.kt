package com.example.highmountain.ui.models

import com.google.firebase.firestore.DocumentSnapshot

data class Percursos
    (
    var id : String?,
    var Nome : String?,
    var Descricao : String?,
    var DataCriacao : String?,
    var DataInicio: String?,
    var HoraInicio : String?,
    var Criador : String?
    )
{
        companion object{
            fun fromDoc(doc : DocumentSnapshot) : Percursos{
                return Percursos(
                    doc.getString("id"),
                    doc.getString("Nome"),
                    doc.getString("Descricao"),
                    doc.getString("DataCriacao"),
                    doc.getString("DataInicio"),
                    doc.getString("HoraInicio"),
                    doc.getString("Criador")
                )
            }
        }
}




