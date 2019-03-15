package com.lisocean.musicplayer.model.remote


import com.lisocean.musicplayer.model.data.search.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicService {
    /**
     * 必选参数 : keywords : 关键词
     * 可选参数 : limit : 返回数量 , 默认为 30 offset : 偏移数量，用于分页 , 如 : 如 :( 页数 -1)*30, 其中 30 为 limit 的值 , 默认为 0
     * type: 搜索类型；默认为 1 即单曲 , 取值意义 : 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单, 1002: 用户, 1004: MV, 1006: 歌词, 1009: 电台,1014: 视频
     * 接口地址 : /search
     */
    @GET("search")
    fun getMusicByKeyWords(@Query("keywords") keywords: String, @Query("offset") offset: Int = 0): Single<MusicList>

    /**
     * 调用此接口 , 传入音乐 id 可获得对应音乐的歌词
     * 接口地址 : /lyric
     */
    @GET("lyric")
    fun getLyricById(@Query("id") id: Int): Single<Lyric>

    /**
     * 说明 : 调用此接口 , 传入音乐 id(支持多个 id, 用 , 隔开), 可获得歌曲详情(注意:歌曲封面现在需要通过专辑内容接口获取)
     * 必选参数 : ids: 音乐 id, 如 ids=347230
     * 接口地址 : /song/detail
     */
    @GET("song/detail")
    fun getSongByIds(@Query("ids") ids : String) : Single<SongsDetail>
    /**
     * 说明: 调用此接口,传入歌曲 id, 可获取音乐是否可用,返回 { success: true, message: 'ok' } 或者 { success: false, message: '亲爱的,暂无版权' }
     * 必选参数 : id : 歌曲 id
     *可选参数 : br: 码率,默认设置了 999000 即最大码率,如果要 320k 则可设置为 320000,其他类推
     * 接口地址 : /check/music
     *
     */
    @GET("check/music")
    fun checkMusicById(@Query("id") id: Int): Single<CheckMusic>
    /**
     * 说明 : 调用此接口,可获取热门搜索列表
     * 接口地址 : /search/hot
     */
    @GET("search/hot")
    fun getHotSearch() : Single<Hot>
    /**
     * 说明 : 调用此接口 , 传入音乐 id 和 limit 参数 , 可获得该音乐的所有评论
     * 必选参数 : id: 音乐 id
     * 可选参数 : limit: 取出评论数量 , 默认为 20
     * offset: 偏移数量 , 用于分页 , 如 :( 评论页数 -1)*20, 其中 20 为 limit 的值
     * 接口地址 : /comment/music
     */
    @GET("comment/music")
    fun getCommentById(@Query("id")id : Int, @Query("offset") offset: Int = 0) : Single<MusicComment>


}

