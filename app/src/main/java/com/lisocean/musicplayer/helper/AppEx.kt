package com.lisocean.musicplayer.helper

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import org.jetbrains.anko.support.v4.defaultSharedPreferences

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
inline fun <reified T> Iterable<T>.findIndex(predicate: (T) -> Boolean): Int{
    forEachIndexed{ index ,t ->
        if (predicate(t))
            return index
    }
    return -1
}