package com.elizabe.intid.data

import androidx.room.*


@Dao
interface IconDao {
    @Query("SELECT * FROM icon_table")
    fun getAll():List<IconWithData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(iconWithData: IconWithData)

    @Delete
    suspend fun delete(iconWithData: IconWithData)

    @Query("DELETE FROM icon_table")
    suspend fun deleteAll()
}