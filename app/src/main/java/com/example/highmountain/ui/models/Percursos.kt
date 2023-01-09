package com.example.highmountain.ui.models

import com.google.firebase.firestore.DocumentSnapshot

data class Percursos
    (
    var id : String?,
    var Nome : String?,
    var Descricao : String?,
    var photoPercurso : String?,
    var DataCriacao : String?,
    var DataInicio: String?,
    var HoraInicio : String?,
    var IdCriador : String?,
    var NomeCriador : String?,
    var photoCriador : String?
    )
{
        companion object{
            fun fromDoc(doc : DocumentSnapshot) : Percursos{
                return Percursos(
                    doc.getString("id"),
                    doc.getString("Nome"),
                    doc.getString("Descricao"),
                    doc.getString("photoPercurso"),
                    doc.getString("DataCriacao"),
                    doc.getString("DataInicio"),
                    doc.getString("HoraInicio"),
                    doc.getString("IdCriador"),
                    doc.getString("NomeCriador"),
                    doc.getString("photoCriador")
                )
            }
        }
}




