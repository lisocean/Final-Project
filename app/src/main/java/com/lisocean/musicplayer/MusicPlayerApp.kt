package com.lisocean.musicplayer

import android.app.Application
import com.lisocean.musicplayer.di.appModule
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger

class MusicPlayerApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModule,logger = AndroidLogger(showDebug = BuildConfig.DEBUG))
    }
}