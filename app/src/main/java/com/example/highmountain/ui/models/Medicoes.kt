package com.example.highmountain.ui.models

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Entity
data class Medicoes
    (
    @PrimaryKey
    var uIdMedicoes : String,
    var uIdUtilizador : String,
    var uIdAdministrador : String,
    var nivelOxigenio : String,
    var nivelCardio  : String,
    var DataMedicao : Date,
    ) {


}

@Dao
interface MedicoesDao{
    @Query("SELECT * FROM medicoes")
    fun getAll(): LiveData<List<Medicoes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(medicoes : Medicoes)


}