package com.lisocean.musicplayer.ui.videoplayer.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.databinding.*
import com.lisocean.musicplayer.model.data.search.MvDetail
import com.lisocean.musicplayer.model.data.search.MvRelate
import com.lisocean.musicplayer.model.data.search.VideoAddress
import com.lisocean.musicplayer.model.repository.MvSearchRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Suppress("UNCHECKED_CAST")
class VideoPlayerViewModel(mvid : Int, val repo : MvSearchRepo) : ViewModel() {
    val currentId = ObservableField<String>()
    val mvData = ObservableField<MvDetail.DataBean>()
    val thumbUrl = ObservableField<String>()
    val isPlaying = ObservableBoolean()
    val isPause = ObservableBoolean()
    val relateDatas = ObservableArrayList<MvRelate.DataBean>()
    val videoId = ObservableField<String>()
    val videoDetail = ObservableField<VideoAddress>()
    init{
        currentId.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                sender?.let {
                    val str = it as ObservableField<String>
                    loadData(str.get() ?: "")
                    loadRelateData(str.get() ?: "")

                }
            }

        })
        videoId.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                sender?.let {
                    val str = it as ObservableField<String>
                    loadVideoData(str.get() ?: "")
                    loadRelateData(str.get() ?: "")

                }
            }

        })
        currentId.set(mvid.toString())
    }

    @SuppressLint("CheckResult")
    fun loadVideoData(id : String){
        repo.getAddressByid(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t1, t2 ->
                if(t2 != null)
                    println(t2)
                videoDetail.set(t1)
            }
    }
    @SuppressLint("CheckResult")
    fun loadData(mvid : String){
        repo.getDetailById(mvid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t1 : MvDetail, t2 ->
                if(t2 != null)
                    println(t2)
                mvData.set(t1.data)
                thumbUrl.set(mvData.get()?.cover)
            }
    }    @SuppressLint("CheckResult")
    fun loadRelateData(mvid : String){
        repo.getRelateDatasById(mvid)

            .subscribeOn(Schedulers.single())
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
