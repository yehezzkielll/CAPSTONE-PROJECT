package com.bangkit.deteksitanaman.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DetectEntity::class],
    version = 1
)
abstract class DetectHistoryDatabase: RoomDatabase() {

    companion object {
        private var Instance: DetectHistoryDatabase? = null

        fun getDatabase(context: Context): DetectHistoryDatabase? {
            if (Instance == null) {
                synchronized(DetectHistoryDatabase::class.java) {
                    Instance = Room.databaseBuilder(context.applicationContext, DetectHistoryDatabase::class.java, "detect_history").build()
                }
            }
            return Instance
        }
    }
    abstract fun detectHistoryDao(): DetectHistoryDao
}