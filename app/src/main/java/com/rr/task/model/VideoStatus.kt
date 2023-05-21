package com.rr.task.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video")
data class VideoStatus(

    @PrimaryKey
    val _id : String,
    val status : String? = null
)
