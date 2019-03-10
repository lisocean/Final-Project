package com.lisocean.musicplayer.ui.presenter

import android.view.View

interface Presenter:View.OnClickListener {
    override fun onClick(v: View?)
}