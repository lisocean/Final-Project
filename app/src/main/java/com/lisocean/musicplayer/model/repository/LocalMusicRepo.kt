package com.lisocean.musicplayer.model.repository

import com.lisocean.musicplayer.model.local.CpDatabase

//need to add dao
class LocalMusicRepo(private val cpLocal : CpDatabase) {
    /**
     * get content provider's local music
     * 获取content provider 的本地音乐
     */
    fun getLocalMusic() = cpLocal.getCpMusic()
}