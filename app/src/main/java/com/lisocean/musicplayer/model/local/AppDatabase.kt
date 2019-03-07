package com.lisocean.musicplayer.model.local

import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.arch.persistence.room.*
import android.content.Context
import android.provider.MediaStore
import com.lisocean.musicplayer.model.data.AudioMediaBean
import com.lisocean.musicplayer.util.Constants

// @Database(entities =  [AudioMediaBean::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile private var INSTANCE : AppDatabase? = null
        fun getInstance(context: Context) : AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also {INSTANCE = it  }
                }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, Constants.DB_NAME)
                .build()
    }

}