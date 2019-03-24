package com.lisocean.musicplayer.ui.localmusic.fragment

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lisocean.musicplayer.BR
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.databinding.FragmentMainMvsBinding
import com.lisocean.musicplayer.model.data.search.mainvideo.MainMvs
import com.lisocean.musicplayer.ui.base.adapter.SingleTypeAdapter
import com.lisocean.musicplayer.ui.localmusic.viewmodel.MainMvsViewModel
import com.lisocean.musicplayer.ui.presenter.ItemClickPresenter
import com.lisocean.musicplayer.ui.videoplayer.VideoPlayerActivity
import kotlinx.android.synthetic.main.fragment_main_mvs.view.*
import org.jetbrains.anko.find
import org.koin.android.viewmodel.ext.android.viewModel
import android.support.v4.util.Pair

class MainMvsFragment : Fragment() , ItemClickPresenter<MainMvs.DataBean> {
    private val mViewModel by viewModel<MainMvsViewModel>()

    private val mBinding by lazy {
        DataBindingUtil.inflate<FragmentMainMvsBinding>(layoutInflater, R.layout.fragment_main_mvs, null ,false)
    }
    private val mAdapter by lazy {
        SingleTypeAdapter<MainMvs.DataBean>(
            context ?: throw Exception("activity is null"),
            R.layout.item_main_mv,
            mViewModel.data).apply {
            itemPresenter = this@MainMvsFragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding.setVariable(BR.presenter,this)
        mBinding.executePendingBindings()
        mBinding.vm = mViewModel

        return mBinding.root.apply {
            mainMvsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
            }
        }
    }

    override fun onItemClick(v: View?, item: MainMvs.DataBean) {
        val intent = Intent(this.context, VideoPlayerActivity::class.java)
        intent.putExtra("mvid", item.id)
        intent.putExtra("isTransient", true)
        val pair = Pair((v as ViewGroup).find<View>(R.id.image), "IMG_TRANSITION")
        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, pair)
        startActivity(intent, activityOptions.toBundle())


    }
}