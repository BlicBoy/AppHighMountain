package com.example.highmountain.ui.models

import com.google.firebase.firestore.DocumentSnapshot
import java.sql.Timestamp
import java.util.*

data class Clientes(
    var uIdCliente : String?,
    var nomeCliente : String?,
    var emailCliente : String?,
    var urlPhoto : String?,
    var dataNascimento : String?,
    var descricaoCliente : String?
    )
{

    fun toHashMap() : HashMap<String,Any?> {
        return hashMapOf(
            "uIdCliente"    to uIdCliente,
            "nomeCliente"   to nomeCliente,
            "emailCliente"     to emailCliente,
            "urlPhoto" to urlPhoto,
            "dataNascimento" to dataNascimento,
            "descricaoCliente" to descricaoCliente
        )
    }

    companion object{
        fun fromDoc(doc : DocumentSnapshot) : Clientes{
            return Clientes(
                doc.getString("uIdCliente")!!,
                doc.getString("nomeCliente")!!,
                doc.getString("emailCliente")!!,
                doc.getString("urlPhoto")!!,
                doc.getString("dataNascimento")!!,
                doc.getString("descricaoCliente")!!
                )
        }
    }
}