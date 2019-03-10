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
        getSharedPreferences(Constants.CONFIG_INFO, Context.MODE_PRIVATE)
        .getString(key,"") as T }

inline fun <reified T :Any> Fragment.argument(key: String) = lazy {
    context?.getSharedPreferences(Constants.CONFIG_INFO, Context.MODE_PRIVATE)
        ?.getString(key  , "") as T
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