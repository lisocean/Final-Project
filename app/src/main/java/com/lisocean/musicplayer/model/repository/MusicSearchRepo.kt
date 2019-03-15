package com.lisocean.musicplayer.model.repository

import android.annotation.SuppressLint
import com.facebook.stetho.inspector.elements.android.AndroidDocumentConstants
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.model.data.search.SongsDetail
import com.lisocean.musicplayer.model.local.dao.SongInfoDao
import com.lisocean.musicplayer.model.remote.MusicService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * remote music of net
 * local music from room
 * TODO() =>
 */
class MusicSearchRepo(private val remote : MusicService) {
    /**
     * get search result
     */
    fun getSearchResult(string: String, offset : Int = 0) : Single<SongsDetail> {
        val str = getSearchIds(string,offset)
            .subscribeOn(Schedulers.io())
            .blockingGet()
        return remote.getSongByIds(str)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getHotSearch() = remote.getHotSearch()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
    fun getLyricById(id : Int) = remote.getLyricById(id)

    fun getSongByIds(ids : List<Int>) =
        remote
            .getSongByIds(ids.joinToString { "$it" })
            .flatMap {
                val list = mutableListOf<SongInfo>()
                it.songs?.forEach { song ->
                    list.add(SongInfo().inject(song))
                }
                return@flatMap Single.just(list.toList())
            }
    @SuppressLint("CheckResult")
    private fun getSearchIds(string: String, offset : Int = 0) =
        remote.getMusicByKeyWords(string,offset)
            .flatMap {
                val str =
                it.result?.songs?.joinToString {song -> "${song.id}" } ?: ""
                return@flatMap Single.just(str)
            }


}