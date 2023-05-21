package com.rr.s2sparent.database.roomdatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.rr.task.model.VideoStatus


@Dao
interface VideoDao {

    @Insert(onConflict = REPLACE)
     suspend fun addStatus(videoStatus : VideoStatus)


    @Query("SELECT * FROM video")
    suspend fun getStatus() : List<VideoStatus>
}