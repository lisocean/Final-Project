package com.lisocean.musicplayer

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModel
import android.os.Environment
import com.lisocean.musicplayer.di.appModule
import com.lisocean.musicplayer.di.viewModelModule
import com.lisocean.musicplayer.helper.constval.Constants
import com.lisocean.musicplayer.helper.utils.FileUtils
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger
import java.io.File

class MusicPlayerApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModule,logger = AndroidLogger(showDebug = BuildConfig.DEBUG))

        FileUtils.createDir(Environment.getExternalStorageDirectory().absolutePath + "/Android/data/app/com.lisocean.musicplayer/cached")
        FileUtils.CreateFile(Constants.path)
        FileUtils.CreateFile(Constants.currentSongPath)
    }
}