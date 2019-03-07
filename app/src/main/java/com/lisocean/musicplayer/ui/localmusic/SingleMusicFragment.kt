package com.lisocean.musicplayer.ui.localmusic

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.ui.localmusic.adapter.SingleMusicAdapter
import kotlinx.android.synthetic.main.fragment_singlemusic.view.*


@Suppress("DEPRECATION")
class SingleMusicFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.fragment_singlemusic, null).apply {
            /**
             * 设置布局管理器
             * 设置adapter
             *
             */
            singleMusicRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = SingleMusicAdapter(null)

            }

        }
    }
}