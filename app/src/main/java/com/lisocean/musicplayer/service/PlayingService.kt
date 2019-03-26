package com.lisocean.musicplayer.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.helper.constval.Constants
import com.lisocean.musicplayer.helper.constval.PlayMode
import com.lisocean.musicplayer.helper.ex.readCurrentSong
import com.lisocean.musicplayer.helper.ex.readList
import com.lisocean.musicplayer.helper.ex.writeCurrentSong
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.ui.localmusic.MainActivity
import com.lisocean.musicplayer.ui.musicplaying.MusicPlayingActivity
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
    var notification: Notification? = null
    var manager: NotificationManager? = null
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
        list.addAll(readList())
        playingSong = readCurrentSong()
        var i = -1
        list.forEachWithIndex { index, songInfo ->
            if(playingSong.id == songInfo.id)
                i = index
        }
        position = i
   //     mode = PlayMode.values()[sp.getInt("mode", 0)]
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val from = intent?.getIntExtra("from", -1)
        when(from){
            FROM_CONTENT ->{}
            FROM_NEXT -> {
                binder.autoPlayNext()
            }
            FROM_STATE -> {

                if(mediaPlayer?.isPlaying == true)
                {
                    binder.pause()
                }
                else
                {
                    binder.playing()
                }
            }
        }
        return START_NOT_STICKY
    }
    //interface
    private val binder by lazy { PlayingBinder() }
    override fun onBind(intent: Intent?): IBinder? {
        return binder.apply {

        }
    }

    @Suppress("DEPRECATION")
    inner class PlayingBinder :
        Binder(),
        Presenter,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
        override fun isPlaying() :Boolean {
            return mediaPlayer?.isPlaying ?: false
        }

        override fun getPosition() : Int{
            return position
        }

        fun initAll(){
            list.clear()
            list.addAll(readList())
            playingSong = readCurrentSong()
            var i = -1
            list.forEachWithIndex { index, songInfo ->
                if(playingSong.id == songInfo.id)
                    i = index
            }
            position = i
        }
        private var listener :(() -> Unit)? = null
        override fun setOnFinishListener(listener: () -> Unit) {
            this.listener = listener
        }
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
                initAll()
                playingSong(playingSong)
            }catch (e : Throwable){}
        }

        /**
         * @return false : Need to update show_list Source
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
            if(mediaPlayer?.isPlaying == true)
                mediaPlayer?.pause()
            notification?.contentView?.setImageViewResource(R.id.play_button, R.mipmap.play_btn)
            manager?.notify(212, notification)
        }

        override fun playing() {
            if(mediaPlayer == null)
                playItem()
            else if(mediaPlayer?.isPlaying == false)
                mediaPlayer?.start()
            notification?.contentView?.setImageViewResource(R.id.play_button, R.mipmap.pause_btn)
            manager?.notify(212, notification)
        }
        private fun Drawable.toBitmap() : Bitmap{
            return (this as BitmapDrawable).toBitmap()
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
            showNotifications()
        }
        val id = "channel_001"
        val name = "channel_00101"

        private fun showNotifications() {
            manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notification = getNotification()
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW)
                manager?.createNotificationChannel(mChannel)
            }
            manager?.notify(212, notification)
        }

        private fun getNotification(): Notification? {
            return NotificationCompat.Builder(this@PlayingService)
                .setTicker("playing ${playingSong.name}")
                .setChannelId(id)
                .setSmallIcon(R.mipmap.ic_action_search_small)
                .setCustomContentView(getRemoteView())
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setContentIntent(getPendingIntent())
                .build()
        }
        private fun getRemoteView(): RemoteViews? {
            val remoteViews = RemoteViews(packageName, R.layout.notification)
            remoteViews.setTextViewText(R.id.textOfSong, playingSong.name)
            remoteViews.setTextViewText(R.id.textOfArtist, playingSong.artists)
            Glide.with(applicationContext)
                .load(playingSong.pictureUrl)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        remoteViews.setImageViewBitmap(R.id.image, (resource as BitmapDrawable).bitmap)
                    }


                })

            remoteViews.setOnClickPendingIntent(R.id.play_button, getStatePendingIntent())
            remoteViews.setOnClickPendingIntent(R.id.playNext, getNextPendingIntent())
            return remoteViews
        }

        private fun getStatePendingIntent(): PendingIntent? {
            val intent = Intent(this@PlayingService, PlayingService::class.java)
            intent.putExtra("from", FROM_STATE)
            return PendingIntent.getService(this@PlayingService,3,intent, PendingIntent.FLAG_UPDATE_CURRENT)

        }

        private fun getNextPendingIntent(): PendingIntent? {
            val intent = Intent(this@PlayingService, PlayingService::class.java)
            intent.putExtra("from",FROM_NEXT)
            return PendingIntent.getService(this@PlayingService,2,intent, PendingIntent.FLAG_UPDATE_CURRENT)

        }

        private fun getPendingIntent(): PendingIntent? {
            val intentM = Intent(this@PlayingService, MainActivity::class.java)
            val intentA = Intent(this@PlayingService, MusicPlayingActivity::class.java)
            val intents = arrayOf(intentM, intentA)
            return PendingIntent.getActivities(this@PlayingService, 1, intents, PendingIntent.FLAG_UPDATE_CURRENT)

        }


        private var isFirst = false
        override fun onCompletion(mp: MediaPlayer?) {
            autoPlayNext()
            //updateUi

        }

        public fun autoPlayNext() {
            if(list.size == 0)
                return
            if(!isFirst){
                isFirst = true
                return
            }
            when(mode){
                PlayMode.MODE_ALL-> position = (position + 1) % (list.size)
                PlayMode.MODE_RANDOM -> position = Random.nextInt(list.size)
                else -> position //无操作
            }
            writeCurrentSong(list[position])
            playItem()
            com.lisocean.musicplayer.helper.ex.run {
                listener?.invoke()
            }

        }

    }
}