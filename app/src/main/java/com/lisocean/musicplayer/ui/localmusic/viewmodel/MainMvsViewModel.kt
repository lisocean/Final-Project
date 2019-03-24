package com.lisocean.musicplayer.ui.localmusic.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import com.lisocean.musicplayer.model.data.search.mainvideo.MainMvs
import com.lisocean.musicplayer.model.repository.MainMvsRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainMvsViewModel(private val repo : MainMvsRepo) : ViewModel() {
    val data = ObservableArrayList<MainMvs.DataBean>()
    init {

        loadData()
    }
    @SuppressLint("CheckResult")
    fun loadData(){
        repo.getMainMvs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t1, t2 ->
                if(t2 != null)
                    println(t2)
                data.addAll(t1.data ?: listOf())

            }
    }
}