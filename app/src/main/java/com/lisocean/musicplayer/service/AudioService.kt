package com.lisocean.musicplayer.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.helper.PlayMode
import com.lisocean.musicplayer.model.data.local.AudioMediaBean
import org.greenrobot.eventbus.EventBus
import kotlin.random.Random

@SuppressLint("Registered")
class AudioService : Service(){
    var mediaPlayer : MediaPlayer? = null
    var list : ArrayList<AudioMediaBean>? = null
    var position : Int = -2
 //   var notification: Notification? = null
 //   var manager:NotificationManager? = null
    val FROM_PRE= 1
    val FROM_NEXT= 2
    val FROM_STATE= 3
    val FROM_CONTENT= 4
    private val binder by lazy { AudioBinder() }
    var mode = PlayMode.MODE_ALL
    val sp by lazy { getSharedPreferences("config",Context.MODE_PRIVATE) }
    override fun onCreate() {
        super.onCreate()
        //获取播放模式
        mode = PlayMode.values()[sp.getInt("mode", 0)]
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //判断进入service的方法
        val from = intent?.getIntExtra("from", -1)

        when(from){
            FROM_PRE->{binder.playPre()}
            FROM_NEXT->{binder.playNext()}
            FROM_CONTENT->{binder.notifyUpdateUi()}
            FROM_STATE->{binder.updatePlayState()}
            else->{
                val pos = intent?.getIntExtra("position", -1) ?: -1 // 想要播放的position
                if(pos!=position){//想要播放的条目和正在播放条目不是同一首
                    position = pos
                    println("intent=$intent")
                    //获取集合以及position
                    list = intent?.getParcelableArrayListExtra<AudioMediaBean>("list")
                    //开始播放音乐
                    binder.playItem()
                }else{
                    //主动通知界面更新
                    binder.notifyUpdateUi()
                }
            }
        }

        return START_NOT_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }
    @Suppress("DEPRECATION")
    inner class AudioBinder:Binder(), Iservice, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
        override fun playPosition(p2: Int) {
            position = p2
            playItem()
        }

        /**
         * 播放上一曲和下一曲
         */
        override fun playPre() {
            list?.let {
                //获取要播放歌曲position
                when(mode){
                    PlayMode.MODE_RANDOM -> position = Random.nextInt(it.size-1)
                    else->{
                        if( position==0 ){
                            position = it.size-1
                        }else{
                            position--
                        }
                    }
                }
                playItem()
            }
        }

        override fun playNext() {
            list?.let {
                position = when(mode){
                    PlayMode.MODE_RANDOM-> Random.nextInt(it.size-1)
                    else-> (position+1)%it.size
                }
            }
            playItem()
        }

        override fun getPlayList(): List<AudioMediaBean>? {
            return list
        }

        /**
         * 更改播放模式
         */
        override fun updatePlayMode() {
            mode = when (mode) {
                PlayMode.MODE_ALL -> PlayMode.MODE_SINGLE
                PlayMode.MODE_SINGLE -> PlayMode.MODE_RANDOM
                PlayMode.MODE_RANDOM -> PlayMode.MODE_ALL
            }
            sp.edit().putInt("mode", mode.ordinal).apply()
        }

        override fun getPlayMode(): PlayMode {
            return mode
        }

        override fun seekTo(p1: Int) {
            mediaPlayer?.seekTo(p1)
        }

        override fun getDuration(): Int {
            return mediaPlayer?.duration ?: 0
        }

        override fun getProgress(): Int {
            return mediaPlayer?.currentPosition ?: 0
        }

        override fun updatePlayState() {
            //切换播放状态
            isPlaying()?.let{
                if (it) {
                    //播放 暂停
                    pause()
                } else {
                    //暂停 播放
                    start()
                }
            }
        }
        /**
         * 暂停
         */
        private fun pause(){
            mediaPlayer?.pause()
            EventBus.getDefault().post(list?.get(position))
            //更新图标
        //    notification?.contentView?.setImageViewResource(R.id.state,R.mipmap.btn_audio_pause_normal)
            //重新显示
         //   manager?.notify(1,notification)
        }

        /**
         * 开始
         */
        private fun start(){
            mediaPlayer?.start()
            EventBus.getDefault().post(list?.get(position))
            //更新图标
    //        notification?.contentView?.setImageViewResource(R.id.state,R.mipmap.btn_audio_play_normal)
            //重新显示
      //      manager?.notify(1,notification)
        }



        override fun isPlaying(): Boolean? {

            return mediaPlayer?.isPlaying
        }

        override fun onCompletion(mp: MediaPlayer?) {
            //自动播放下一曲
            autoPlayNext()
        }

        /**
         * 根据播放模式自动播放下一曲
         */
        private fun autoPlayNext() {
            when(mode){
                PlayMode.MODE_ALL-> position = (position + 1) % (list?.size ?: 1)
                PlayMode.MODE_RANDOM -> position = Random.nextInt(list?.size ?: 1)
                else -> position //无操作
            }

            playItem()
        }

        override fun onPrepared(mp: MediaPlayer?) {
            //播放音乐
            mediaPlayer?.start()
            //通知界面更新
            notifyUpdateUi()
            //显示通知
        }
        val id = "channel_01"
        val name = "channel_0101"
        /**
         * 下一曲点击事件
         */
        private fun getNextPendingIntent(): PendingIntent? {
            val intent = Intent(this@AudioService,AudioService::class.java)//点击主体进入当前界面中
            intent.putExtra("from",FROM_NEXT)
            return PendingIntent.getService(this@AudioService,2,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        /**
         * 播放暂停按钮点击事件
         */
        private fun getStatePendingIntent(): PendingIntent? {
            val intent = Intent(this@AudioService,AudioService::class.java)//点击主体进入当前界面中
            intent.putExtra("from",FROM_STATE)
            return PendingIntent.getService(this@AudioService,3,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        /**
         * 上一曲点击事件
         */
        private fun getPrePendingIntent(): PendingIntent? {
            val intent = Intent(this@AudioService, AudioService::class.java)//点击主体进入当前界面中
            intent.putExtra("from",FROM_PRE)
            return PendingIntent.getService(this@AudioService,4,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        /**
         * 通知界面更新
         */
        fun notifyUpdateUi() {
            //发送端
            EventBus.getDefault().post(list?.get(position))
        }

        override fun playItem() {
            if(mediaPlayer != null){
                mediaPlayer?.reset()
                mediaPlayer?.release()
                mediaPlayer = null
            }
            mediaPlayer = MediaPlayer()
            mediaPlayer?.let {
                it.setOnPreparedListener(this)
                it.setOnCompletionListener(this)
                it.setDataSource("file://" + list?.get(position)?.data)
                it.prepareAsync()
            }
        }
    }


}