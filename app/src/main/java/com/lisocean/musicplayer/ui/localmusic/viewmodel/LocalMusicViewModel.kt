package com.lisocean.musicplayer.ui.localmusic.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.lisocean.musicplayer.model.data.local.AudioMediaBean
import com.lisocean.musicplayer.model.repository.LocalMusicRepo
import org.jetbrains.anko.collections.forEachWithIndex

class LocalMusicViewModel(private val repo : LocalMusicRepo) : ViewModel() {
    val list = ObservableArrayList<MusicItemViewModel>()
    val playingId = ObservableInt()
    val isPlaying = ObservableBoolean()
    val title = ObservableField<String>()
    val artist = ObservableField<String>()
    val localAudioList = arrayListOf<AudioMediaBean>()
    init {
        loadData()
    }

    @SuppressLint("CheckResult")
    fun loadData(){
        repo.getLocalMusic().subscribe { t1, t2 ->
            if(t2 != null)
                println(t2)

            list.clear()
            localAudioList.clear()
            localAudioList.addAll(t1)
            t1.forEachWithIndex { i, audioMediaBean ->
                list.add(MusicItemViewModel(i, audioMediaBean))
            }
            if(!list.isNullOrEmpty()){
                playingId.set(0)
                title.set(list[playingId.get()].title)
                artist.set(list[playingId.get()].artist)
                isPlaying.set(false)
            }

        }
    }

}