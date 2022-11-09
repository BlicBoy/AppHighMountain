package com.example.highmountain.ui.models

import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
import kotlin.collections.HashMap

data class User(
    var userId       : String?,
    var username     : String?,
    var email        : String?,
    var photoURL: String?,
    var genero : String?,
    var Datanasc : Date?,
    var morada : String?
)  {
     //fun
    fun toHasMap() : HashMap<String,Any?>{
        return hashMapOf(
            "userId" to userId,
            "username" to username,
            "email" to email,
            "photoURL" to photoURL,
            "genero" to genero,
            "Datanasc" to Datanasc,
            "morada" to morada
        )

    }

    companion object{

        fun fromDoc(doc: DocumentSnapshot): User {
            return User(
                doc.getString("userId" ),
                doc.getString("username"),
                doc.getString("email"),
                doc.getString("photoURL"),
                doc.getString("genero"),
                doc.getDate("Datanasc"),
                doc.getString("morada")
            )
        }

    }
}