package com.lisocean.musicplayer.ui.search.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.model.data.search.MusicList
import com.lisocean.musicplayer.model.repository.MusicSearchRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex

class SearchViewModel(private val repo : MusicSearchRepo) : ViewModel() {

    val songs = ObservableArrayList<SongInfo>()
    val position = ObservableInt()
    val text = ObservableField<String>()
    /**
     * @param keyWork some thing to search
     * @param block when get the  result, callback some view function
     */
    @SuppressLint("CheckResult")
    fun search(keyWork :String, block : ((error : Throwable?)->Unit)? = null): Boolean {
        var CheckResult = true

        try {
            songs.clear()
            repo.getSearchResult(keyWork)
                .subscribe { t1 , t2 ->

                    t1.songs?.forEach {
                        songs.add(SongInfo().inject(it))
                    }
                    block?.invoke(t2)

//                    val x = t1
//                    t1.result?.songs?.forEachWithIndex { i, songsBean ->
//                        songs.add(songsBean)
//                    }
//                    search_count.set("Search results : find ${t1.result?.songCount}")
                }

        } catch (e: Throwable) {
            block?.invoke(e)
        }
        return  CheckResult
    }
}

