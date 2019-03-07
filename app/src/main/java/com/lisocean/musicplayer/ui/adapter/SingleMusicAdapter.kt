package com.lisocean.musicplayer.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.lisocean.musicplayer.model.data.AudioMediaBean
import com.lisocean.musicplayer.widget.SingleMusicItemView

class SingleMusicAdapter(private val list : List<AudioMediaBean>?) : RecyclerView.Adapter<SingleMusicAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(SingleMusicItemView(p0.context))
    }

    override fun getItemCount(): Int {
        return list?.size ?: 80
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
}