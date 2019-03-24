package com.lisocean.musicplayer.model.repository

import com.lisocean.musicplayer.model.remote.MvService

class MvSearchRepo(private val remote : MvService){
    //获取视频的详细信息
    fun getDetailById(id : String) = remote.getMvDetailByMvid(id)
    //获取视频的相关视频信息
    fun getRelateDatasById(id : String)  = remote.getRelateDataByMvid(id)
    //获取视频播放地址
    fun getAddressByid(id: String)  = remote.getMvUrlById(id)

}