package com.lisocean.musicplayer.ui.musicplaying.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.model.repository.MusicSearchRepo

class MusicPlayingViewModel(listTemp : List<SongInfo>, positionTemp : Int, repo : MusicSearchRepo) : ViewModel() {
    //playing show_list
    val list = ObservableArrayList<SongInfo>()
    //current playing song
    val playingSong = ObservableField<SongInfo>()
    //position
    val position = ObservableInt()
    //current playing song's lyric
    val lyric = ObservableField<String>()
    //is playing
    val isPlaying = ObservableBoolean()
    init {

        list.addAll(listTemp)
        position.set(positionTemp)
        playingSong.set(list[position.get()])
        isPlaying.set(false)
    }

}