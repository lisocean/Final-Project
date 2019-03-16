@file:Suppress("DEPRECATION")

package com.lisocean.musicplayer.ui.musicplaying

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.helper.BlurTransformation
import com.lisocean.musicplayer.helper.StatusBarUtil
import com.lisocean.musicplayer.model.data.local.SongInfo
import kotlinx.android.synthetic.main.activity_music_playing.*

class MusicPlayingActivity: AppCompatActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_playing)
        immmmm.setOnClickListener {
            changeBackground()
        }
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
    }

private var p = 1
    fun changeBackground(){
        val list = intent.getParcelableArrayListExtra<SongInfo>("list")

        Glide.with(this.applicationContext)
            .load(list[p].pictureUrl)
            .thumbnail(0.1f)
            .transform(BlurTransformation(this.applicationContext, 120f))
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    rootLayout.foreground = resource
                    rootLayout.beginAnimation()
                    p += 1
                }

            })
    }

}