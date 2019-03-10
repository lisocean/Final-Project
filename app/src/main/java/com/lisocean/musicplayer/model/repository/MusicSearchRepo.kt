package com.lisocean.musicplayer.model.repository

import com.lisocean.musicplayer.model.local.CpDatabase
import com.lisocean.musicplayer.model.remote.MusicService

/**
 * remote music of net
 * local music from room
 * TODO() =>
 */
class MusicSearchRepo(private val remote : MusicService) {
    /**
     * get search result
     */
    fun getSearchResult(string: String) = remote.getMusicByKeyWords(string)

}