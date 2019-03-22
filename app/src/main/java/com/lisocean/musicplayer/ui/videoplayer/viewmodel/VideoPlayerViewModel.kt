package com.lisocean.musicplayer.ui.videoplayer.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.databinding.*
import com.lisocean.musicplayer.helper.utils.getAudioMediaBean
import com.lisocean.musicplayer.model.data.search.MvDetail
import com.lisocean.musicplayer.model.data.search.MvRelate
import com.lisocean.musicplayer.model.repository.MvSearchRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class VideoPlayerViewModel(mvid : Int ,val repo : MvSearchRepo) : ViewModel() {
    val currentId = ObservableInt()
    val mvData = ObservableField<MvDetail.DataBean>()
    val thumbUrl = ObservableField<String>()
    val isPlaying = ObservableBoolean()
    val isPause = ObservableBoolean()
    val relateDatas = ObservableArrayList<MvRelate.DataBean>()
    init{
        currentId.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                sender?.let {
                    if(it is ObservableInt){
                        loadData(mvid)

                    }
                }
            }

        })
        currentId.set(mvid)
    }
    @SuppressLint("CheckResult")
    fun loadData(mvid : Int){
        repo.getDetailById(mvid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t1 : MvDetail, t2 ->
                if(t2 != null)
                    println(t2)
                mvData.set(t1.data)
                thumbUrl.set(mvData.get()?.cover)
            }
        repo.getRelateDatasById(mvid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t1 : MvRelate, t2 ->
                if (t2 != null)
                    println(t2)
                relateDatas.clear()
                val tempData = t1.data  ?: listOf()
                relateDatas.addAll(tempData)

            }
    }


}
