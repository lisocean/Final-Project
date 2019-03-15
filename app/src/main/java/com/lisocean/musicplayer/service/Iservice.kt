package com.lisocean.musicplayer.service

import com.lisocean.musicplayer.helper.PlayMode
import com.lisocean.musicplayer.model.data.local.AudioMediaBean

interface Iservice {
    fun updatePlayState()
    fun isPlaying():Boolean?
    fun getDuration(): Int
    fun getProgress(): Int
    fun seekTo(p1: Int)
    fun updatePlayMode()
    fun  getPlayMode(): PlayMode
    fun playPre()
    fun playNext()
    fun getPlayList(): List<AudioMediaBean>?
    fun playPosition(p2: Int)
    fun playItem()
}
