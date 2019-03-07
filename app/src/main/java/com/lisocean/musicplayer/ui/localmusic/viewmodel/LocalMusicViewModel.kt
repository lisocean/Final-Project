package com.lisocean.musicplayer.ui.localmusic.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import com.lisocean.musicplayer.model.data.local.AudioMediaBean
import com.lisocean.musicplayer.model.repository.LocalMusicRepo

class LocalMusicViewModel(private val id : Int,private val repo : LocalMusicRepo) : ViewModel() {
    val list = ObservableArrayList<AudioMediaBean>()

    @SuppressLint("CheckResult")
    fun loadData(){
        repo.getLocalMusic().doOnSuccess {
            list.addAll(it)
        }
    }

}