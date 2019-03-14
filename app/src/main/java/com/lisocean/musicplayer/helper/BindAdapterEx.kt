package com.lisocean.musicplayer.helper

import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

@BindingAdapter(value = ["url"])
fun setUrl(v : ImageView?, url : String){
    v?.apply {
        Glide
            .with(this.context)
            .load(url)
            .centerCrop()
            .into(this)
    }
}