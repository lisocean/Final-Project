@file:Suppress("UNCHECKED_CAST")

package com.lisocean.musicplayer.helper.ex

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.lisocean.musicplayer.helper.constval.Constants
import com.lisocean.musicplayer.model.data.local.SongInfo
import java.io.*
import java.util.Arrays.asList



/**
 *  获取参数配置信息
 *  get parameter configuration information
 */
inline fun <reified T : Any> Context.argument(key : String) =
    lazy {
        try {
            getSharedPreferences(Constants.CONFIG_INFO, Context.MODE_PRIVATE)
                .getString(key,"") as T
        }catch (e : Throwable){
            return@lazy 0
        }
    }

inline fun <reified T :Any> Fragment.argument(key: String) = lazy {
    context?.getSharedPreferences(Constants.CONFIG_INFO, Context.MODE_PRIVATE)
        ?.getString(key  , "") as T
}

fun Context.argumentInt(key : String) =
    lazy {
        try {
            getSharedPreferences(Constants.CONFIG_INFO, Context.MODE_PRIVATE)
                .getInt(key,0)
        }catch (e : Throwable){
            return@lazy 0
        }
    }

fun  Fragment.argumentInt(key: String) = lazy {
    try {
    context?.getSharedPreferences(Constants.CONFIG_INFO, Context.MODE_PRIVATE)
        ?.getInt(key  , 0)
    }catch (e : Throwable){
        return@lazy 0
    }
}
/**
 * set parameter configuration information
 */
fun View.setArgument(key: String, data : String){
    context.getSharedPreferences(Constants.CONFIG_INFO, Context.MODE_PRIVATE)
        .edit()
        .putString(key, data)
        .apply()
}
fun View.setArgumentInt(key: String, data : Int){
    context.getSharedPreferences(Constants.CONFIG_INFO, Context.MODE_PRIVATE)
        .edit()
        .putInt(key, data)
        .apply()
}
fun AppCompatActivity.setArgumentInt(key: String, data : Int){
    getSharedPreferences(Constants.CONFIG_INFO, Context.MODE_PRIVATE)
        .edit()
        .putInt(key, data)
        .apply()
}
inline fun <reified T> Iterable<T>.findIndex(predicate: (T) -> Boolean): Int{
    forEachIndexed{ index ,t ->
        if (predicate(t))
            return index
    }
    return -1
}
fun writeList(list : List<SongInfo>){
    val file = File(Constants.path)
    val out = ObjectOutputStream(FileOutputStream(file))
    try {
        out.writeObject(list.toTypedArray())
    }catch (e : Throwable){
        println(e)
    }finally {
        out.close()
    }
}
fun readList() : List<SongInfo>{
    val arrayList = arrayListOf<SongInfo>()
    run {
        val file = File(Constants.path)
        val out = ObjectInputStream(FileInputStream(file))

        try {
            val list = out.readObject() as Array<SongInfo>
            arrayList.addAll(list)
        }catch (e : Throwable){
            println(e)
        }finally {
            out.close()
        }
    }

    return arrayList.toList()
}
fun writeCurrentSong(currentSong : SongInfo){
    val file = File(Constants.currentSongPath)
    val out = ObjectOutputStream(FileOutputStream(file))
    try {
        out.writeObject(currentSong)
    }catch (e : Throwable){
        println(e)
    }finally {
        out.close()
    }
}
fun readCurrentSong() : SongInfo{
    var currentSong = SongInfo()
    run {
        val file = File(Constants.currentSongPath)
        val out = ObjectInputStream(FileInputStream(file))
        try {
            val song = out.readObject() as SongInfo
            currentSong = song
        }catch (e : Throwable){
            println(e)
        }finally {
            out.close()
        }
    }
    return currentSong
}
fun run(func : () -> Unit) {try {
    func.invoke()
}catch (e : Throwable){}}

