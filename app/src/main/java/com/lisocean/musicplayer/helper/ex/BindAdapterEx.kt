package com.lisocean.musicplayer.helper.ex

import android.databinding.BindingAdapter
import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.helper.blur.BlurBitmapTransformtion
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

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

@BindingAdapter(value = ["videothumb"])
fun setThumbImageView(v : StandardGSYVideoPlayer?, thumbUrl : String?){
    //增加封面
    if(thumbUrl.isNullOrEmpty())
        return
    val imageView = ImageView(v?.context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(this.context)
            .load(thumbUrl)
            .centerCrop()
            .into(this)
    }
    v?.thumbImageView = imageView
}
@BindingAdapter(value = ["videobackground"])
fun setVideoBackground(v : ImageView?, url : String?){
    if(url.isNullOrEmpty())
        return
    v?.apply {
      Glide.with(context)
          .load(url)
          .transform(BlurBitmapTransformtion(context))
          .into(this)
    }
}