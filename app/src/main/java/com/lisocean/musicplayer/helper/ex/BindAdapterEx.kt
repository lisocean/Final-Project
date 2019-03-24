@file:Suppress("DEPRECATION")

package com.lisocean.musicplayer.helper.ex

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.media.Image
import android.transition.Transition
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.helper.blur.BlurBitmapTransformtion
import com.lisocean.musicplayer.widget.BackgroundRelativeLayout
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import kotlinx.android.synthetic.main.activity_music_playing.view.*

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
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .into(this)

        }
    else{
        v?.apply {
            Glide.with(this.context)
                .load(R.drawable.ic_blackground)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .into(this)
        }
    }
}

@BindingAdapter(value = ["videothumb"])
fun setThumbImageView(v : StandardGSYVideoPlayer?, thumbUrl : String?){
    //增加封面
    if(thumbUrl.isNullOrEmpty())
        return
    val imageView = ImageView(v?.context).apply {

        Glide.with(this.context)
            .load(thumbUrl)
            .centerCrop()
            .into(this)
    }
    v?.thumbImageView = imageView
}
@BindingAdapter(value = ["videobackground"])
fun setVideoBackground(v : BackgroundRelativeLayout?, url : String?){
    if(url.isNullOrEmpty())
        return
    v?.apply {
        Glide.with(context)
            .load(url)
            .thumbnail(0.75f)
            .transform(BlurBitmapTransformtion(context))
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: com.bumptech.glide.request.transition.Transition<in Drawable>?
                ) {
                    foreground = resource
                    beginAnimation()

                }
            })
    }
}