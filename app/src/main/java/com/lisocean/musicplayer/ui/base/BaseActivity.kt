package com.lisocean.musicplayer.ui.base

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import com.lisocean.musicplayer.service.PlayingService
import com.lisocean.musicplayer.service.Presenter

/**
 * @use : the class with function of media service
 */
abstract class BaseActivity : AppCompatActivity() {
    protected var presenter : Presenter? = null
    private val conn by lazy { PlayingConnection() }

    /***
     * start service for once
     */
    protected fun startService(){
        /**
         * start service
         */
        val intent = Intent(this,  PlayingService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
        startService(intent)
    }

    protected fun bindService(){
        val intent = Intent(this,  PlayingService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    inner class PlayingConnection : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            presenter = p1 as Presenter
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        unbindService(conn)
    }
}