package com.lisocean.musicplayer.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.databinding.ObservableArrayList
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.helper.ex.writeCurrentSong
import com.lisocean.musicplayer.helper.ex.writeList
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.ui.base.BaseActivity
import com.lisocean.musicplayer.ui.base.adapter.SingleTypeAdapter
import com.lisocean.musicplayer.ui.localmusic.viewmodel.LocalMusicViewModel
import com.lisocean.musicplayer.ui.localmusic.viewmodel.RdViewModel
import com.lisocean.musicplayer.ui.presenter.ItemClickPresenter
import com.lisocean.musicplayer.ui.videoplayer.VideoPlayerActivity
import kotlinx.android.synthetic.main.item_recommend_all.view.*

@SuppressLint("ViewConstructor")
class ItemRdAllView @JvmOverloads constructor(
    private val activity: BaseActivity,
    private val viewModel : LocalMusicViewModel,
    private val mViewModel : RdViewModel,
    val show_list : ObservableArrayList<SongInfo>,
    val list: ArrayList<SongInfo>,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), ItemClickPresenter<SongInfo> {


    private val adapter by lazy {
        SingleTypeAdapter<SongInfo>(
            context,
            R.layout.item_singlemusic,
            show_list).apply {
            itemPresenter = this@ItemRdAllView
        }
    }
    init {
        View.inflate(context, R.layout.item_recommend_all, this)
        recommend_recyclerView.apply{
            //disable RecyclerView scrolling
            layoutManager = object : LinearLayoutManager(context){
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter = this@ItemRdAllView.adapter
        }
        loading.setOnClickListener {
            loadMore()
        }
        recommend_text.setOnClickListener {
            initData()
        }

    }

    private fun initData() {
        show_list.clear()
        for(i in 0..4)
            show_list.add(list[i])
    }

    private fun loadMore(){
        val offset = list.size - show_list.size
        if(offset > 5)
        {
            for ( i in show_list.size..(show_list.size + 5))
                show_list.add(list[i])

        }else{
            for(i in show_list.size..(list.size - 1))
                show_list.add(list[i])
        }

    }

    override fun onItemClick(v: View?, item: SongInfo) {
        viewModel.playingSongs.clear()
        viewModel.playingSongs.addAll(list)
        writeList(list)
        writeCurrentSong(item)
        viewModel.currentSong.set(item)
        activity.presenter.playItem()
    }
    override fun onLikeClick(v: View?, item: SongInfo) {
        activity.presenter.pause()
        val intent = Intent(context, VideoPlayerActivity::class.java)
        intent.putExtra("mvid", item.mvid)
        startActivity(context ,intent, null)

    }

    override fun onPopClick(v: View?, item: SongInfo) {

    }
}