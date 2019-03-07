package com.lisocean.musicplayer.util

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.View

/**
 *  获取参数配置信息
 *  get parameter configuration information
 */
inline fun <reified T : Any> FragmentActivity.argument(key : String) =
    lazy {
        getSharedPreferences("confgigstate", Context.MODE_PRIVATE)
        .getString(key,"") as T }


/**
 * set parameter configuration information
 */
fun View.setArgument(key: String, data : String){
    context.getSharedPreferences("confgigstate", Context.MODE_PRIVATE)
        .edit()
        .putString(key, data)
        .apply()
}