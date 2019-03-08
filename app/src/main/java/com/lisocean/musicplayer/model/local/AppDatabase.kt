package com.lisocean.musicplayer.model.local

import android.arch.persistence.room.*
import android.content.Context
import com.lisocean.musicplayer.helper.Constants

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