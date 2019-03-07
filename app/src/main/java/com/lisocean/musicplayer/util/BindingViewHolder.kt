package com.lisocean.musicplayer.util

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

class BindingViewHolder<out T : ViewDataBinding>(binding: T) : RecyclerView.ViewHolder(binding.root)