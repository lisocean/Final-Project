package com.lisocean.musicplayer.model.repository

import com.lisocean.musicplayer.model.remote.MusicService
import java.lang.Exception

/**
 * remote music of net
 * local music from room
 * TODO() =>
 */
class MusicRepository(private val remote : MusicService, private val local : MusicService) {
    init {
        throw Exception("local music not by dao")
    }
}