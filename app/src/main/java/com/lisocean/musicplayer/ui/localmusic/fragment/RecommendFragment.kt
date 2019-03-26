package com.lisocean.musicplayer.ui.localmusic.fragment

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lisocean.musicplayer.BR
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.databinding.FragmentRdBinding
import com.lisocean.musicplayer.ui.base.BaseActivity
import com.lisocean.musicplayer.ui.localmusic.adapter.RdRecyclerAdapter
import com.lisocean.musicplayer.ui.localmusic.viewmodel.LocalMusicViewModel
import com.lisocean.musicplayer.ui.localmusic.viewmodel.RdViewModel
import kotlinx.android.synthetic.main.fragment_singlemusic.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.android.viewmodel.ext.android.viewModel

class RecommendFragment : Fragment()  {
    private val mViewModel by viewModel<RdViewModel>()
    private val localMusicViewModel by lazy{
        ViewModelProviders.of(activity!!).get(LocalMusicViewModel::class.java)}
    private val mBinding by lazy {
        DataBindingUtil.inflate<FragmentRdBinding>(layoutInflater, R.layout.fragment_rd, null , false)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding.setVariable(BR.presenter,this)
        mBinding.executePendingBindings()
        mBinding.vm = mViewModel

        return mBinding.root.apply {
            singleMusicRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = RdRecyclerAdapter((activity as BaseActivity) ,localMusicViewModel, mViewModel ,mViewModel.show_list , mViewModel.list)
            }
        }
    }
}