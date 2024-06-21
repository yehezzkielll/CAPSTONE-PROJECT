package com.bangkit.deteksitanaman.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DetectHistoryDao {

    @Insert
    suspend fun addScanToHistory(scanHistory: DetectEntity)

    @Query("SELECT * FROM detect_history")
    suspend fun getScanHistory(): List<DetectEntity>

    @Query("DELETE FROM detect_history WHERE detect_history.id = :id")
    suspend fun clearScanHistory(id: Int): Int

}