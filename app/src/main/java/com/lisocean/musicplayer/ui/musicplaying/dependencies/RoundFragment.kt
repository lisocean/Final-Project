package com.lisocean.musicplayer.ui.musicplaying.dependencies

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.lisocean.musicplayer.R
import kotlinx.android.synthetic.main.fragment_roundimage.view.*


class RoundFragment :Fragment() {

    companion object {
        fun newInstance(url : String = "") : RoundFragment {
            val fragment = RoundFragment()
            val bundle = Bundle()
            bundle.putString("url", url)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.fragment_roundimage, null).apply {

            Glide.with(this.context)
                .load(this@RoundFragment.arguments?.getString("url"))
                .placeholder(R.drawable.placeholder_disk_play_program)
                .override(200,200)
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(sdv)
        }

    }
}