package com.lisocean.musicplayer.model.repository

import com.lisocean.musicplayer.di.remoteModule
import com.lisocean.musicplayer.model.data.local.AudioMediaBean
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.model.local.CpDatabase
import com.lisocean.musicplayer.model.local.dao.SongInfoDao
import com.lisocean.musicplayer.model.remote.MusicService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LocalMusicRepo(private val cpLocal : CpDatabase, private val local : SongInfoDao ,private val remote : MusicService) {
    /**
     * get content provider's local music
     * 获取content provider 的本地音乐
     */
    fun getLocalMusic() :Single<List<SongInfo>>{

        return local.getAllSong()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                val array = arrayListOf<SongInfo>()
                val cpMusic = cpLocal.getCpMusic().blockingGet()
                array.addAll(it)
                array.addAll(cpMusic.filter {aud ->
                    array.forEach { song ->
                        if(aud.data == song.data)
                            return@filter false
                    }
                    true
                }.map {audio ->
                    SongInfo().inject(audio)
                })
                return@map array.toList()
            }
    }
    fun getllMusic() = cpLocal.getCpMusic()
    fun insertAll(list:List<SongInfo>) = local.insertAll(list)
    fun insertData(songInfo: SongInfo) = local.insertSong(songInfo)

    /**
     * adapter the local music
     * long time
     *
     */
    fun mapAudioToSongInfo(audioMediaBeans: List<AudioMediaBean>) : Single<List<SongInfo>>
        = getSongByIds(mapAudioBeanToName(audioMediaBeans))

    private fun getSearchId(name : String) :Single<Int>{
        return remote.getMusicByKeyWords(name)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map {
                return@map it.result?.songs?.get(0)?.id
            }
    }
    private fun mapAudioBeanToName(audioMediaBeans: List<AudioMediaBean>) : List<Int> =
        audioMediaBeans
            .map { it.title }
            .map { getSearchId(it).blockingGet() }



    private fun getSongByIds(ids : List<Int>) =
        remote
            .getSongByIds(ids.joinToString { "$it" })
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMap {
                val list = mutableListOf<SongInfo>()
                it.songs?.forEach { song ->
                    list.add(SongInfo().inject(song))
                }
                return@flatMap Single.just(list.toList())
            }
}