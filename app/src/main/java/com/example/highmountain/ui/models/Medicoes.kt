package com.example.highmountain.ui.models

import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
import kotlin.collections.HashMap

@Entity
data class Medicoes
    (
    @PrimaryKey
    var uIdMedicoes : String?, //Random
    var uIdPartipante : String?,
    var Latitude : String?,
    var Longitude : String?,
    var Altura : String?,
    var nivelOxigenio : String?,
    var batimentoCardiaco : String?,
    var dataMedicao : String?
    ) {

        //enviar dados firebase
        fun toHashMapMedicoes() : HashMap<String,Any?>{

            return hashMapOf(
                "uIdMedicoes" to uIdMedicoes,
                "Latitude" to Latitude,
                "Longitude" to Longitude,
                "Altura" to Altura,
                "nivelOxigenio" to nivelOxigenio,
                "barimentos"

            )

        }


        //Pegar Dados do Firebase
        companion object{
            fun fromDocMedicoes(doc : DocumentSnapshot) : Medicoes{
                return Medicoes(
                    doc.getString("uIdMedicoes"),
                    doc.getString("Latitude"),
                    doc.getString("Longitude"),
                    doc.getString("Altura"),
                    doc.getString("nivelOxigenio"),
                    doc.getString("batimentoCadiaco"),
                    doc.getString("dataMedicao")
                )
            }
        }


}


//Guardar Localmente
@Dao
interface MedicoesDao{
    @Query("SELECT * FROM medicoes")
    fun getAll(): LiveData<List<Medicoes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(medicoes : Medicoes)


}