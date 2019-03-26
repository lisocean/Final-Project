package com.lisocean.musicplayer.service

import com.lisocean.musicplayer.helper.constval.PlayMode
import com.lisocean.musicplayer.model.data.local.SongInfo

interface Presenter {
    //show_list
    fun updateSongs(songs : List<SongInfo>)
    fun getPlayingSongs() : List<SongInfo>
    fun delSong(song : SongInfo) : Boolean

    //playingSong
    fun playItem()
    fun playingSong(song: SongInfo) : Boolean
    fun getPlayingSong() : SongInfo
    fun playPre()
    fun playNext()
    fun getPosition() : Int


    //playing state
    fun updatePlayingState()
    fun pause()
    fun playing()
    fun isPlaying() :Boolean
    fun setOnFinishListener(listener : () -> Unit)
    //playing mode state
    fun getPlayingMode () : PlayMode
    fun setPlayingMode (mode : PlayMode)

    //progress
    fun getDuration() : Int
    fun getProgress() : Int
    fun seekTo(p1 : Int)

}