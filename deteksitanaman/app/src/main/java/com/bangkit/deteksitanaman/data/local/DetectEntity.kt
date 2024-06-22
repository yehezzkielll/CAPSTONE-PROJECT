package com.bangkit.deteksitanaman.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "detect_history")
data class DetectEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val scanImage: String,

    val plantName: String,

    val plantDesc: String,

    val plantType: String
)