package com.lisocean.musicplayer.helper.ex

import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.lisocean.musicplayer.R

@BindingAdapter(value = ["visibile"])
fun setVisibile(v : LinearLayout?, mvid : Int){
    if(mvid <= 0)
        v?.visibility = View.GONE
    else
        v?.visibility = View.VISIBLE
}

@BindingAdapter(value = ["url"])
fun setUrl(v : ImageView?, url : String?){
    if(!url.isNullOrEmpty())
        v?.apply {
            Glide
                .with(this.context)
                .load(url)
                .into(this)

        }
    else{
        v?.apply {
            Glide.with(this.context)
                .load(R.drawable.ic_blackground)
                .centerCrop()
                .into(this)
        }
    }
}