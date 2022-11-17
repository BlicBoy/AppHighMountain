package com.example.highmountain.ui.models

import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.firebase.firestore.DocumentSnapshot


@Entity
data class Percurso(

    @PrimaryKey
    var uIdPercurso : String?,
    var dataPercurso : String?,
    var uIdCriador : String?,
    var descricaoPercurso : String?
)
{
    companion object{
        fun fromDoc(doc : DocumentSnapshot) : Percurso{
            return Percurso(
                doc.getString("uIdPercurso")!!,
                doc.getString("dataPercurso")!!,
                doc.getString("uIdCriador")!!,
                doc.getString("descricaoPercurso")!!
            )
        }
    }
}
@Dao
interface PercursoDao{
    @Query("SELECT * FROM percurso")
    fun getAll(): LiveData<List<Percurso>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(percurso : Percurso)
}