package com.lisocean.musicplayer.ui.localmusic.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.model.repository.RdSearchRepo

class RdViewModel(private val repo : RdSearchRepo) : ViewModel()  {

    val show_list1 = ObservableArrayList<SongInfo>()
    val show_list2 = ObservableArrayList<SongInfo>()
    val show_list3 = ObservableArrayList<SongInfo>()
    val show_list = listOf(show_list1,show_list2,show_list3)

    val list1 = arrayListOf<SongInfo>()
    val list2 = arrayListOf<SongInfo>()
    val list3 = arrayListOf<SongInfo>()
    val list = listOf(list1,list2,list3)
    init {
        loadId()
    }
    @SuppressLint("CheckResult")
    fun loadId(){
        repo.RdGetTopPlaylist()
            .subscribe { t1, t2 ->
                if(t2 != null)
                    println(t2)
                t1.forEachIndexed { index, l ->
                    loadData(l, list[index], show_list[index])
                }
            }
    }
    @SuppressLint("CheckResult")
    fun loadData(id : Long?, list : ArrayList<SongInfo>, show_list : ObservableArrayList<SongInfo>){
        repo.RdGetPlaylistDetail(id ?: 0)
            .subscribe { t1 : List<SongInfo>, t2 ->
                if(t2 != null)
                    println(t2)
                list.addAll(t1)
                for( i in 0..4){
                    show_list.add(list[i])
                }
            }
    }
}