package com.lisocean.musicplayer.ui.localmusic.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.lisocean.musicplayer.helper.ex.readCurrentSong
import com.lisocean.musicplayer.helper.ex.readList
import com.lisocean.musicplayer.helper.ex.writeCurrentSong
import com.lisocean.musicplayer.helper.ex.writeList
import com.lisocean.musicplayer.model.data.local.AudioMediaBean
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.model.repository.LocalMusicRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.collections.forEachWithIndex

class LocalMusicViewModel(private val musicId : Int, private val repo : LocalMusicRepo) : ViewModel() {
    val list = ObservableArrayList<SongInfo>()

    val localAudioList = mutableListOf<AudioMediaBean>()
    val localAppList = arrayListOf<SongInfo>()

    val playingSongs = ObservableArrayList<SongInfo>()
    val isPlaying = ObservableBoolean()
    val position = ObservableInt()
    val currentSong = ObservableField<SongInfo>()
    val picUrl = ObservableField<String>()
    init {
        loadData()
        try {
            playingSongs.addAll(readList())
            currentSong.set(readCurrentSong())
            picUrl.set(currentSong.get()?.pictureUrl)
        }catch (e : Throwable){ }
    }

    @SuppressLint("CheckResult")
    fun loadData(){
        repo.getLocalMusic()
            .subscribe { t1, t2 ->
                if (t2 != null)
                    println(t2)

                list.clear()
                list.addAll(t1)
                isPlaying.set(false)
            }


    }
    @SuppressLint("CheckResult")
    fun addDataToApp(after: (()->Unit)? = null){
        repo.mapAudioToSongInfo(localAudioList)
            .subscribe { t1, _ ->
                t1?.let {
                    t1.forEach {songInfo ->
                        localAudioList.forEach { cPmusic ->
                            if(songInfo.name == cPmusic.title && songInfo.artists == cPmusic.artist)
                            {
                                songInfo.data = cPmusic.data
                                localAppList.add(songInfo)
                            }
                        }

                    }
                }
                repo.insertAll(localAppList)
                Runnable {
                    after?.invoke()
                }
            }
    }
    @SuppressLint("CheckResult")
    fun scanCpMusic(){
        repo.getllMusic().subscribe { t1, t2 ->
            if (t2 != null)
                println(t2)
            localAudioList.clear()
            localAudioList.addAll(t1)
            localAppList.forEach {songInfo->
                localAudioList.forEach { cPmusic->
                    if(songInfo.name == cPmusic.title && songInfo.artists == cPmusic.artist){
                        localAudioList.remove(cPmusic)
                    }
                }
            }
            addDataToApp()
        }
    }

}