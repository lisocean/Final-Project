package com.lisocean.musicplayer.helper

import android.databinding.BindingAdapter
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lisocean.musicplayer.R
import org.jetbrains.anko.image

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