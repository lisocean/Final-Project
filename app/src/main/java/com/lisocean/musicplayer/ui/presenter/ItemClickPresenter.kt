package com.lisocean.musicplayer.ui.presenter

import android.view.View

interface ItemClickPresenter<in Any> {
    fun onItemClick(v : View? = null, item : Any)
    fun onLikeClick(v : View? = null, item : Any) {}
    fun onPopClick(v : View? = null, item : Any) {}
}