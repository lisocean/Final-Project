package com.lisocean.musicplayer.ui.localmusic.adapter

import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.ui.base.BaseActivity
import com.lisocean.musicplayer.ui.localmusic.viewmodel.LocalMusicViewModel
import com.lisocean.musicplayer.ui.localmusic.viewmodel.RdViewModel
import com.lisocean.musicplayer.widget.ItemRdAllView

class RdRecyclerAdapter(val activity : BaseActivity,
                        val viewModel : LocalMusicViewModel,
                        val mViewModel : RdViewModel,
                        val show_list : List<ObservableArrayList<SongInfo>>,
                        val list : List<ArrayList<SongInfo>>
) : RecyclerView.Adapter<RdRecyclerAdapter.Holder>() {

    private var i = -1
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        i += 1
        return Holder(ItemRdAllView(activity ,viewModel, mViewModel ,show_list[i] , list[i], p0.context))
    }

    override fun getItemCount(): Int {
        return show_list.size
    }

    override fun onBindViewHolder(p0: Holder, p1: Int) {
    }

    inner class Holder(item : View) : RecyclerView.ViewHolder(item)
}