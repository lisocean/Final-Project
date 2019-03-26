package com.lisocean.musicplayer.ui.base

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.service.PlayingService
import com.lisocean.musicplayer.service.Presenter
import org.greenrobot.eventbus.EventBus

/**
 * @use : the class with function of media service
*/
abstract class BaseActivity : AppCompatActivity() {

    lateinit var presenter: Presenter


    protected val conn by lazy { PlayingConnection() }


    /***
     * start service for once
     */
    protected fun startService(){
        //register eventBus

        /**
         * start service
         */
        val intent = Intent(this,  PlayingService::class.java)

        bindService(intent, conn, Context.BIND_AUTO_CREATE)
        startService(intent)
    }

    protected fun bindService(){
        //register eventBus

        val intent = Intent(this,  PlayingService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    abstract fun initWhenConn()
    inner class PlayingConnection : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            presenter = p1 as Presenter
            initWhenConn()
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}