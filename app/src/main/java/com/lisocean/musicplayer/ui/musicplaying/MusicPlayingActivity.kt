@file:Suppress("DEPRECATION")

package com.lisocean.musicplayer.ui.musicplaying

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.databinding.ActivityMusicPlayingBinding
import com.lisocean.musicplayer.helper.blur.BlurBitmapTransformtion
import com.lisocean.musicplayer.helper.StatusBarUtil
import com.lisocean.musicplayer.helper.utils.StringUtil
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.service.PlayingService
import com.lisocean.musicplayer.ui.base.BaseActivity
import com.lisocean.musicplayer.ui.musicplaying.dependencies.RoundFragment
import com.lisocean.musicplayer.ui.musicplaying.viewmodel.MusicPlayingViewModel
import com.lisocean.musicplayer.ui.presenter.Presenter
import com.lisocean.musicplayer.widget.AlbumViewPager
import com.lisocean.musicplayer.widget.PlayListPopUpWindow
import kotlinx.android.synthetic.main.activity_music_playing.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.ArrayList


class MusicPlayingActivity: BaseActivity(), Presenter, SeekBar.OnSeekBarChangeListener {


    val list: ArrayList<SongInfo> by lazy {
        intent.getParcelableArrayListExtra<SongInfo>("show_list")
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
    private var isNeedleStarted  = false

    /**
     * rotate
     */
    private var rotateAnimation : ObjectAnimator? = null

//    private val animatorSet by lazy { AnimatorSet() }
private val MSG_PROGRESS = 0
    private var handler =  @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message?) {
            when(msg?.what){
         //       MSG_PROGRESS -> updateProgress(presenter?.getProgress() ?: 0)
            }
        }
    }

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
        changeBackground()
        musicSeekBar.setOnSeekBarChangeListener(this)

    }
    private var progressed : Int = 0
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if(!fromUser)
            return
        progressed = progress
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        updateProgress(progressed)
    }

    private fun updateProgress(progress: Int = 0){
        musicSeekBar.progress = progress
        tvCurrentTime.text = StringUtil.parseDuration(progress)
        handler.sendEmptyMessageDelayed(MSG_PROGRESS, 1000)
    }
    private fun initState() {

        val isPlaying = intent.getBooleanExtra("isPlaying", false)
        mViewModel.isPlaying.set(isPlaying)
        if(view_pager.isActivated)
        when(mViewModel.isPlaying.get()){
            true ->{
                find<View>(R.id.playButton).isSelected = true
            }
            false ->{
                find<View>(R.id.playButton).isSelected = false

            }
        }
        musicSeekBar.max = mViewModel.playingSong.get()?.duration ?: 40000
        tvTotalTime.text = StringUtil.parseDuration(musicSeekBar.max)
        tvCurrentTime.text = StringUtil.parseDuration(progressed)
        handler.sendEmptyMessageDelayed(MSG_PROGRESS, 1000)

    }


    private fun initViewPager() {

        view_pager.offscreenPageLimit = 2

        view_pager.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(p0: Int): Fragment {
                return when(p0){
                    0 ->  RoundFragment.newInstance(mViewModel.list[mViewModel.list.size - 1].pictureUrl)
                    mViewModel.list.size + 1 -> RoundFragment.newInstance(mViewModel.list[0].pictureUrl)
                    else ->  RoundFragment.newInstance(mViewModel.list[p0 - 1].pictureUrl)
                }
            }
            override fun getCount(): Int {
                return mViewModel.list.size + 2
            }
        }
        view_pager.setCurrentItem(mViewModel.position.get() + 1, false)
        //2 3 1 origin    2 3 4 1 other pager  2 3 4 1 4 //边界
        //out invoke =>  3 4 1
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            var isChanged = false
            var isBounds = false
            override fun onPageSelected(p0: Int) {
                // bound => second onPageSelected return
                if(isBounds){
                    isBounds = false
                    return
                }
                println(4)
                if(p0 == 0 || p0 == mViewModel.list.size + 1)
                    isBounds = true
                //change info
                isChanged = true
                when(p0){
                    0 -> mViewModel.position.set(mViewModel.list.size - 1)
                    mViewModel.list.size + 1 ->  mViewModel.position.set(0)
                    else ->  mViewModel.position.set(p0 - 1)
                }
                if(isBounds){
                    find<View>(R.id.playButton).isSelected = true
                    mViewModel.isPlaying.set(true)
                    mViewModel.playingSong.set(mViewModel.list[mViewModel.position.get()])
                    if(p0 == 0){
                    }
                }else{
                    changeCurrentSong(mViewModel.position.get())
                }

            }

            override fun onPageScrollStateChanged(p0: Int) {
                when(p0){
                    ViewPager.SCROLL_STATE_IDLE -> {
                        println(1)
                        when(view_pager.currentItem){
                            0 -> {
                                view_pager.setCurrentItem(mViewModel.list.size, false)
                                if(!isNeedleStarted){
                                    mNeedleAnim.start()
                                    isNeedleStarted = true
                                }
                                rotateAnimation?.end()
                                rotateAnimation = view_pager.rotate(view_pager.currentItem).apply { start() }
                            }
                            mViewModel.list.size + 1 -> {
                                view_pager.setCurrentItem(1, false)
                                if (!isNeedleStarted) {
                                    mNeedleAnim.start()
                                    isNeedleStarted = true
                                }
                                rotateAnimation?.end()
                                rotateAnimation = view_pager.rotate(view_pager.currentItem).apply { start() }
                            }
                        }
                        isChanged = false

                    }
                    ViewPager.SCROLL_STATE_DRAGGING -> {
                        println(2)
                        if(mViewModel.isPlaying.get()){
                            if(isNeedleStarted){
                                mNeedleAnim.reverse()
                                isNeedleStarted = false
                            }
                            rotateAnimation?.pause()
                        }
                    }
                    ViewPager.SCROLL_STATE_SETTLING ->{
                        println(3)
                        if(isChanged){
                        }else{
                            if(mViewModel.isPlaying.get())
                            {
                                if(!isNeedleStarted){
                                    mNeedleAnim.start()
                                    isNeedleStarted = true
                                }
                                rotateAnimation?.resume()
                            }else{
                                // pause state
                            }
                        }
                    }
                }
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }


        })

    }

    private fun update(){
        when(mViewModel.isPlaying.get()){
            true -> {
                find<View>(R.id.playButton).isSelected = false
                if(isNeedleStarted){
                    mNeedleAnim.reverse()
                    isNeedleStarted = false
                }
                rotateAnimation?.pause()
                mViewModel.isPlaying.set(false)
            }
            false -> {
                find<View>(R.id.playButton).isSelected = true
                if(rotateAnimation == null)
                {
                    rotateAnimation = view_pager
                        .rotate(mViewModel.position.get() + 1)
                        .apply { start() }
                }else
                    rotateAnimation?.resume()

                if(!isNeedleStarted){
                    mNeedleAnim.start()
                    isNeedleStarted = true
                }
                mViewModel.isPlaying.set(true)
            }
        }
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.playButton -> {
                update()

            }
            R.id.playMode -> {toast("mode")}
            R.id.playNext -> {
                outInvokeSong(mViewModel.position.get() + 1)
            }
            R.id.playPre -> {
                outInvokeSong(mViewModel.position.get() - 1)
            }
            R.id.popUpMore -> {
                val popUp = PlayListPopUpWindow(window = window,context = this, listTemp = mViewModel.list.toList())
                popUp.showAsDropDown(find(R.id.space_playing_music),0, space_playing_music.height)

            }
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

    private fun outInvokeSong(position: Int){
        if(isNeedleStarted){
            mNeedleAnim.reverse()
            isNeedleStarted = false
        }
        view_pager.currentItem = position + 1
    }

    private fun changeCurrentSong(position: Int){
        rotateAnimation?.end()
        rotateAnimation = view_pager.rotate(position + 1).apply { start() }
        mViewModel.playingSong.set(mViewModel.list[mViewModel.position.get()])
        changeBackground()
        mViewModel.isPlaying.set(true)
        if(!isNeedleStarted){
            mNeedleAnim.start()
            isNeedleStarted = true
        }
        find<View>(R.id.playButton).isSelected = true
    }

    /**
     * change back ground
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

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}