package com.sonkajarvi.calculator.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "history")
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val expr: String
)

@Dao
interface EntryDAO {
    @Query("SELECT * from history")
    fun getAll(): Flow<List<Entry>>

    @Insert
    suspend fun insert(e: Entry): Long

    @Delete
    suspend fun delete(id: Int) {}
}
