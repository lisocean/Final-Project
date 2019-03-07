package com.lisocean.musicplayer.model.repository

import com.lisocean.musicplayer.model.local.CpDatabase
import com.lisocean.musicplayer.model.remote.MusicService

/**
 * remote music of net
 * local music from room
 * TODO() =>
 */
class MusicSearchRepo(private val remote : MusicService, private val local : CpDatabase) {
    init {
        throw Throwable("local music not by dao")
    }
}