package com.lisocean.musicplayer.ui.base.adapter.decorator

import android.databinding.ViewDataBinding
import com.lisocean.musicplayer.ui.base.adapter.BindingViewHolder

interface ItemDecorator{
    fun decorator(holder: BindingViewHolder<ViewDataBinding>?, position: Int, viewType: Int)
}