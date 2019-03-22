package com.lisocean.musicplayer.model.remote

import com.lisocean.musicplayer.model.data.search.MvComment
import com.lisocean.musicplayer.model.data.search.MvDetail
import com.lisocean.musicplayer.model.data.search.MvRelate
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MvService {
    /**
     * 说明 : 调用此接口 , 传入 mvid ( 在搜索音乐的时候传 type=1004 获得 ) , 可获取对应 MV 数据 , 数据包含 mv 名字 , 歌手 , 发布时间 , mv 视频地址等数据 , 其中 mv 视频 网易做了防盗链处理 , 可能不能直接播放 , 需要播放的话需要调用 ' mv 地址' 接口
     * 必选参数 : mvid: mv 的 id
     * 接口地址 : /mv/detail
     */
    @GET("mv/detail")
    fun getMvDetailByMvid(@Query("mvid") mvId : Int) : Single<MvDetail>


    /**
     * 说明 : 调用此接口 , 传入视频 id,可获取视频播放地址
     */
    @GET("video/url")
    fun getRelateDataByMvid(@Query("id") mvId : Int) : Single<MvRelate>
    /**
     * 说明 : 调用此接口 , 传入音乐 id 和 limit 参数 , 可获得该 mv 的所有评论
     * 必选参数 : id: mv id
     * 可选参数 : limit: 取出评论数量 , 默认为 20
     * offset: 偏移数量 , 用于分页 , 如 :( 评论页数 -1)*20, 其中 20 为 limit 的值
     * 接口地址 : /comment/mv
     *
     */
    @GET("comment/mv")
    fun getMvCommentByMvid(@Query("id") mvId : Int) : Single<MvComment>

}