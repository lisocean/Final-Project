package com.lisocean.musicplayer.ui.search

import android.annotation.SuppressLint
import com.lisocean.musicplayer.model.repository.MusicSearchRepo
import org.jetbrains.anko.collections.forEachWithIndex

class SearchViewModel(private val repo : MusicSearchRepo) {

    @SuppressLint("CheckResult")
    fun search(keyWork :String): Boolean {
        var CheckResult = true
        repo.getSearchResult(keyWork)
            .doOnSuccess {
                it.result?.songs?.forEachWithIndex { i, songsBean ->

                }

            }.doOnError {
                println(it)
                CheckResult = false
            }
        return CheckResult
    }

}