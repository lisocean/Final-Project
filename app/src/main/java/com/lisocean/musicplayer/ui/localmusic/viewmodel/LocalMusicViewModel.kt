package com.lisocean.musicplayer.ui.localmusic.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import com.lisocean.musicplayer.model.data.local.AudioMediaBean
import com.lisocean.musicplayer.model.repository.LocalMusicRepo
import org.jetbrains.anko.collections.forEachWithIndex

class LocalMusicViewModel(private val repo : LocalMusicRepo) : ViewModel() {
    val list = ObservableArrayList<MusicItemViewModel>()


    @SuppressLint("CheckResult")
    fun loadData(){
        repo.getLocalMusic().subscribe { t1, t2 ->
            list.clear()
            t1.forEachWithIndex { i, audioMediaBean ->
                list.add(MusicItemViewModel(i, audioMediaBean))
            }
        }
    }

}