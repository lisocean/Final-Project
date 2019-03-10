package com.lisocean.musicplayer.ui.search.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.lisocean.musicplayer.model.data.search.MusicList
import com.lisocean.musicplayer.model.repository.MusicSearchRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex

class SearchViewModel(private val repo : MusicSearchRepo) : ViewModel() {

    val songs = ObservableArrayList<MusicList.ResultBean.SongsBean>()
    val position = ObservableInt()
    val text = ObservableField<String>()
    val search_count = ObservableField<String>()
    @SuppressLint("CheckResult")
    fun search(keyWork :String): Boolean {
        var CheckResult = true
        try {
            repo.getSearchResult(keyWork)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t1, _ ->

                    println(t1)
                    val x = t1
                    t1.result?.songs?.forEachWithIndex { i, songsBean ->
                        songs.add(songsBean)
                    }
                    search_count.set("Search results : find ${t1.result?.songCount}")
                }

        } catch (e: Exception) {
            println("88")
        }
        return  CheckResult
    }
}

