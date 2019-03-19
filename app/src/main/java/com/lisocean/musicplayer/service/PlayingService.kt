package com.lisocean.musicplayer.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import com.lisocean.musicplayer.helper.constval.Constants
import com.lisocean.musicplayer.helper.constval.PlayMode
import com.lisocean.musicplayer.model.data.local.SongInfo
import org.jetbrains.anko.collections.forEachWithIndex
import kotlin.random.Random

/**
 * @use : bind -> start -> updateSongs -> play song
 *
 */
class PlayingService : Service() {
    private var mediaPlayer : MediaPlayer? = null
    //song map
    private var list = arrayListOf<SongInfo>()
    //the song is playing
    private var playingSong = SongInfo()
    private var position : Int = -2
    companion object {
        //about notification
        private const val FROM_NEXT = 1
        private const val FROM_STATE = 2
        private const val FROM_CONTENT = 3
    }

    //current playing mode
    private var mode = PlayMode.MODE_ALL

    private val sp by lazy {
        getSharedPreferences(Constants.CONFIG_INFO, Context.MODE_PRIVATE)
    }

    override fun onCreate() {
        super.onCreate()
        mode = PlayMode.values()[sp.getInt("mode", 0)]
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_NOT_STICKY
    }
    //interface
    private val binder by lazy { PlayingBinder() }
    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }
    inner class PlayingBinder :
        Binder(),
        Presenter,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {


        override fun updateSongs(songs: List<SongInfo>) {
            list.clear()
            list.addAll(songs)
        }

        override fun getPlayingSongs(): List<SongInfo> {
            return list.toList()
        }

        override fun delSong(song: SongInfo): Boolean {
            list.forEach {
                if(it.id == song.id){
                    list.remove(it)
                    return true
                }
            }
            return false
        }

        override fun playItem() {
            try {
                playingSong(list[position])
            }catch (e : Throwable){}
        }

        /**
         * @return false : Need to update list Source
         */
        override fun playingSong(song: SongInfo) : Boolean{
            if(mediaPlayer != null){
                mediaPlayer?.reset()
                mediaPlayer?.release()
                mediaPlayer = null
            }
            mediaPlayer = MediaPlayer()
            mediaPlayer?.let {
                it.reset()
                it.setOnPreparedListener(this)
                it.setOnCompletionListener(this)
                if(song.data.isEmpty()){
                    it.setDataSource(applicationContext, Uri.parse(Constants.HOST_REPLACE_API + song.id))
                }else{
                    it.setDataSource("file://${song.data}")
                }
                it.prepareAsync()
            }
            playingSong = song
            list.forEachWithIndex {index ,songInfo ->
                if(songInfo.id == song.id){
                    position = index
                    return true
                }
            }
            return false
        }

        override fun getPlayingSong(): SongInfo {
            return playingSong
        }

        override fun playPre() {
            list.apply {
                when(mode){
                    PlayMode.MODE_RANDOM -> position = Random.nextInt(this.size -  1)
                    else ->{
                        if(position == 0)
                            position = this.size - 1
                        else
                            position --
                    }
                }
                playItem()
            }
        }

        override fun playNext() {
            when(mode){
                PlayMode.MODE_RANDOM-> Random.nextInt(list.size-1)
                else-> (position + 1) % list.size
            }
            playItem()
        }

        override fun updatePlayingState() {
            mediaPlayer?.isPlaying?.let {
                if(it)
                    pause()
                else
                    playing()
            }
        }

        override fun pause() {
            mediaPlayer?.pause()
        }

        override fun playing() {
            mediaPlayer?.start()
        }

        override fun getPlayingMode(): PlayMode {
            return mode
        }

        override fun setPlayingMode(mode: PlayMode) {
            this@PlayingService.mode = mode
            sp.edit().putInt("mode", mode.ordinal).apply()
        }

        override fun getDuration(): Int {
            return mediaPlayer?.duration ?: 0
        }

        override fun getProgress(): Int {
            return mediaPlayer?.currentPosition ?: 0
        }

        override fun seekTo(p1: Int) {
            mediaPlayer?.seekTo(p1)
        }

        override fun onPrepared(mp: MediaPlayer?) {
            mediaPlayer?.start()
        }

        override fun onCompletion(mp: MediaPlayer?) {
            autoPlayNext()
        }

        private fun autoPlayNext() {
            when(mode){
                PlayMode.MODE_ALL-> position = (position + 1) % (list.size )
                PlayMode.MODE_RANDOM -> position = Random.nextInt(list.size)
                else -> position //无操作
            }
            playItem()
        }

    }
}