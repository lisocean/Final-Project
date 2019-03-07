package com.lisocean.musicplayer

import com.lisocean.musicplayer.di.remoteModule
import com.lisocean.musicplayer.model.data.search.MusicList
import com.lisocean.musicplayer.model.remote.MusicService
import com.lisocean.musicplayer.model.remote.MvService
import org.junit.Test

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class NetUnitTest {
    /**
     * all function test passed
     */


    private val search by lazy {
        Retrofit.Builder()
            .baseUrl("https://music.aityp.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MusicService::class.java)
    }
    @Test
    fun testSearch() {

        search.getMusicByKeyWords("有点甜")
            .subscribe { t1  : MusicList?,t2 : Throwable?->
                 println(t1)
                println(t2)
            }
    }
    @Test
    fun testLyricByid(){
        search.getLyricById(33894312)
            .subscribe { t1, t2 ->
                println(t1)
                println(t2)
            }
    }
    @Test
    fun testcheckMusicById(){
        search.checkMusicById(33894312)
            .subscribe { t1, t2 ->
                println(t1)
                println(t2)
            }
    }
    @Test
    fun testgetCommentById(){
        search.getCommentById(33894312)
            .subscribe { t1, t2 ->
                println(t1)
                println(t2)
            }
    }

//    @Test
//    fun testgetMvDetailByMvid(){
//
//        search.getMvDetailByMvid(5436712)
//            .subscribe { t1, t2 ->
//                println(t1)
//                println(t2)
//            }
//    }
//    @Test
//    fun testgetMvCommentByMvid(){
//        search.getMvCommentByMvid(5436712)
//            .subscribe { t1, t2 ->
//                println(t1)
//                println(t2)
//            }
//    }

}
