package com.lisocean.musicplayer.model.repository

import com.lisocean.musicplayer.model.remote.MvService

class MvSearchRepo(private val remote : MvService){
    //获取视频的详细信息
    fun getDetailById(id : Int) = remote.getMvDetailByMvid(id)
    //获取视频的相关视频信息
    fun getRelateDatasById(id : Int)  = remote.getRelateDataByMvid(id)

}