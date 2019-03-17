package com.lisocean.musicplayer.ui.localmusic

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lisocean.musicplayer.BR
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.databinding.FragmentSinglemusicBinding
import com.lisocean.musicplayer.helper.*
import com.lisocean.musicplayer.ui.presenter.ItemClickPresenter
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.ui.base.adapter.SingleTypeAdapter
import com.lisocean.musicplayer.ui.localmusic.viewmodel.LocalMusicViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_singlemusic.view.*
import org.jetbrains.anko.support.v4.toast
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


@Suppress("DEPRECATION")
class SingleMusicFragment : Fragment(), ItemClickPresenter<SongInfo> {


    private val musicId by argumentInt(Constants.MUSIC_ID)

    private val mViewModel by lazy{ ViewModelProviders.of(activity!!).get(LocalMusicViewModel::class.java)}

    private val mBinding by lazy {
        DataBindingUtil.inflate<FragmentSinglemusicBinding>(layoutInflater, R.layout.fragment_singlemusic, null ,false)
    }
    private val mAdapter by lazy {
        SingleTypeAdapter<SongInfo>(
            context ?: throw Exception("activity is null"),
                    R.layout.item_singlemusic,
                    mViewModel.list).apply {
            itemPresenter = this@SingleMusicFragment
            this.onBindItem { v :View , item : SongInfo ->
                try {
                    val selectedState =  this@SingleMusicFragment.argument<String>(item.id.toString()).value
                    if (selectedState.isEmpty())
                        throw Exception("go next")
                    v.isSelected = selectedState.toBoolean()
                    //v.isSelected = selectedState
                }catch (e : Exception){
                    v.isSelected = false
                }

            }
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

        }
    }
    override fun onItemClick(v: View?, item: SongInfo) {
        mViewModel.currentSong.set(item)
        mViewModel.picUrl.set(item.pictureUrl)
        mViewModel.position.set(mViewModel.list.findIndex { it.id == item.id })
        v?.setArgumentInt(Constants.MUSIC_ID,item.id)
    }

    override fun onLikeClick(v: View?, item: SongInfo) {

        v?.apply{
            isSelected = when(isSelected){
                true -> false
                false -> true
            }
            setArgument(item.id.toString(), isSelected.toString())
        }
    }

    override fun onPopClick(v: View?, item: SongInfo) {

        toast("pop")
    }
}