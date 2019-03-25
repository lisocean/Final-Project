package com.lisocean.musicplayer.model.repository

import android.arch.lifecycle.ViewModel
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.model.remote.MusicService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RdSearchRepo(private val remote : MusicService) {

    fun RdGetTopPlaylist() =
        remote.RdGetTopPlaylist()
            .subscribeOn(Schedulers.io())
            .flatMap {
                val idList = it.playlists?.map { item ->
                    item.id
                } ?: listOf()
                return@flatMap  Single.just(idList)
            }
            .observeOn(AndroidSchedulers.mainThread())


    fun RdGetPlaylistDetail(id : Long) =
        remote.RdGetPlaylistDetail(id)
            .subscribeOn(Schedulers.io())
            .flatMap { rdDetail ->
                val songs = arrayListOf<SongInfo>()
                rdDetail.playlist?.tracks?.apply {
                    forEach {
                        songs.add(SongInfo().inject(it))
                    }
                }
                return@flatMap Single.just(songs)
            }
            .observeOn(AndroidSchedulers.mainThread())
}