package com.lisocean.musicplayer.model.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.lisocean.musicplayer.model.data.local.SongInfo
import io.reactivex.Single


@Dao
interface SongInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(songs : List<SongInfo>)

    @Query("SELECT * FROM songs WHERE songid = :id")
    fun getSongById(id: Int): Single<SongInfo>

    @Query("SELECT * FROM songs")
    fun getAllSong() : Single<List<SongInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSong(song: SongInfo)
}