@file:Suppress("DEPRECATION")

package com.lisocean.musicplayer.ui.musicplaying

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.databinding.ActivityMusicPlayingBinding
import com.lisocean.musicplayer.helper.BlurBitmapTransformtion
import com.lisocean.musicplayer.helper.BlurTransformation
import com.lisocean.musicplayer.helper.StatusBarUtil
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.ui.musicplaying.dependencies.RoundFragment
import com.lisocean.musicplayer.ui.musicplaying.viewmodel.MusicPlayingViewModel
import com.lisocean.musicplayer.ui.presenter.Presenter
import com.lisocean.musicplayer.widget.AlbumViewPager
import kotlinx.android.synthetic.main.activity_music_playing.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.ArrayList


class MusicPlayingActivity: AppCompatActivity(), Presenter {

    val list: ArrayList<SongInfo> by lazy {
        intent.getParcelableArrayListExtra<SongInfo>("list")
    }
    val position by lazy {
        intent.getIntExtra("position", 0)
    }
    private val mViewModel
            by viewModel<MusicPlayingViewModel> { parametersOf(list, position) }

    private val mBinding by lazy {
        DataBindingUtil.setContentView<ActivityMusicPlayingBinding>(this, R.layout.activity_music_playing)
    }
    /**
     * needle animation
     */
    private val mNeedleAnim by lazy {
        ObjectAnimator.ofFloat(needle, "rotation", -30f, 0f).apply {
            duration = 300
            interpolator = LinearInterpolator()
        }
    }

    /**
     * rotate
     */
    private var rotateAnimation : ObjectAnimator? = null

    private val animatorSet by lazy { AnimatorSet() }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_playing)
        playButton.setOnClickListener {
            changeBackground()
        }
        mBinding.vm = mViewModel
        mBinding.presenter = this
        initViewPager()
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
    }

    private fun initViewPager() {

        view_pager.offscreenPageLimit = 2

        view_pager.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(p0: Int): Fragment {
                return RoundFragment.newInstance(mViewModel.list[p0].pictureUrl)
            }
            override fun getCount(): Int {
                return mViewModel.list.size
            }
        }
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
            }

        })
        view_pager.currentItem = mViewModel.position.get()
        changeBackground()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.playButton -> {
                when(mViewModel.isPlaying.get()){
                    true -> {
                        v.isSelected = false
                        mNeedleAnim.reverse()
                        rotateAnimation?.pause()
                        mViewModel.isPlaying.set(false)
                    }
                    false -> {
                        v.isSelected = true
                        if(rotateAnimation == null)
                        {
                            rotateAnimation = view_pager
                                .rotate(mViewModel.position.get())
                                .apply { start() }
                        }else
                            rotateAnimation?.resume()

                        mNeedleAnim.start()
                        mViewModel.isPlaying.set(true)
                    }
                }

            }
            R.id.playMode -> {toast("mode")}
            R.id.playNext -> {
                changeSong(mViewModel.position.get() + 1)
            }
            R.id.playPre -> {
                changeSong(mViewModel.position.get() - 1)
            }
            R.id.popUpMore -> {toast("popUpMore")}
        }
    }

    /**
     * rotate
     * 以自身中心旋转
     */
    private fun AlbumViewPager.rotate(position: Int) : ObjectAnimator{

        val fragment =
            this.adapter?.instantiateItem(view_pager, position) as RoundFragment?
        return ObjectAnimator.ofFloat(
            fragment?.view,
            "rotation",
            0f, 360f).apply {
            duration = 15000
            startDelay = 500
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
        }
    }
    /**
     * change song and then update all view and service
     */
    private fun changeSong(position : Int){
        //TODO => When position == -1 or size and WeakReference

        rotateAnimation?.end()
        rotateAnimation = view_pager.rotate(position).apply { start() }
        mViewModel.position.set(position)
        mViewModel.playingSong.set(mViewModel.list[mViewModel.position.get()])
        view_pager.currentItem = mViewModel.position.get()
        changeBackground()
        mViewModel.isPlaying.set(true)
        mNeedleAnim.start()
        find<View>(R.id.playButton).isSelected = true
    }

    /**
     * chenge back ground
     */
    private fun changeBackground(){
        Glide.with(this.applicationContext)
            .load(mViewModel.playingSong.get()?.pictureUrl)
            .thumbnail(0.1f)
            .transform(BlurBitmapTransformtion(this.applicationContext))
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    rootLayout.foreground = resource
                    rootLayout.beginAnimation()
                }
            })
    }

}