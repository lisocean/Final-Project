package com.lisocean.musicplayer.ui.localmusic

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lisocean.musicplayer.BR
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.databinding.FragmentSinglemusicBinding
import com.lisocean.musicplayer.helper.Constants
import com.lisocean.musicplayer.helper.argument
import com.lisocean.musicplayer.helper.presenter.ItemClickPresenter
import com.lisocean.musicplayer.ui.base.adapter.SingleTypeAdapter
import com.lisocean.musicplayer.ui.localmusic.viewmodel.LocalMusicViewModel
import com.lisocean.musicplayer.ui.localmusic.viewmodel.MusicItemViewModel
import kotlinx.android.synthetic.main.fragment_singlemusic.*
import kotlinx.android.synthetic.main.fragment_singlemusic.view.*
import org.jetbrains.anko.support.v4.defaultSharedPreferences
import org.jetbrains.anko.support.v4.toast
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


@Suppress("DEPRECATION")
class SingleMusicFragment : Fragment(), ItemClickPresenter<MusicItemViewModel> {


    private val musicId by argument<Int>(Constants.MUSIC_ID)

    private val mViewModel by viewModel<LocalMusicViewModel>()

    private val mBinding by lazy {
        DataBindingUtil.inflate<FragmentSinglemusicBinding>(layoutInflater, R.layout.fragment_singlemusic, null ,false)
    }
    private val mAdapter by lazy {
        SingleTypeAdapter<MusicItemViewModel>(
            context ?: throw Exception("activity is null"),
                    R.layout.item_singlemusic,
                    mViewModel.list).apply {
            itemPresenter = this@SingleMusicFragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding.setVariable(BR.presenter,this)
        mBinding.executePendingBindings()
        mBinding.vm = mViewModel

        return mBinding.root.apply {
            singleMusicRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
            }
            mViewModel.loadData()
        }
    }
    override fun onItemClick(v: View?, item: MusicItemViewModel) {
        toast("item")
    }

    override fun onLikeClick(v: View?, item: MusicItemViewModel) {
        toast("like")
    }

    override fun onPopClick(v: View?, item: MusicItemViewModel) {
        toast("pop")
    }
}