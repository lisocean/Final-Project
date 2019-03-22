package com.lisocean.musicplayer.ui.videoplayer

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.databinding.ActivityVideoplayerBinding
import com.lisocean.musicplayer.helper.StatusBarUtil
import com.lisocean.musicplayer.model.data.search.MvDetail
import com.lisocean.musicplayer.ui.videoplayer.viewmodel.VideoPlayerViewModel
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import kotlinx.android.synthetic.main.activity_videoplayer.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@Suppress("UNCHECKED_CAST")
class VideoPlayerActivity : AppCompatActivity(){

    private val mvid : Int by lazy {
        intent.getIntExtra("mvid", -1)
    }
    private val mViewModel by viewModel<VideoPlayerViewModel>{parametersOf(mvid)}

    private val mBinding by lazy {
        DataBindingUtil.setContentView<ActivityVideoplayerBinding>(this, R.layout.activity_videoplayer)
    }

    private var orientationUtils: OrientationUtils? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        //recyclerView
//        videoPlayer_recyclerView.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter =
//        }
        mBinding.vm = mViewModel
        initVideoViewConfig()
        initCallBack()
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.darkModeForM(window, true)
        StatusBarUtil.setPaddingSmart(this, videoPlayer)

    }
    //when Get the date
    private fun initCallBack() {
        mViewModel.mvData.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                sender?.let {
                    val data = it as  ObservableField<MvDetail.DataBean>
                    val url = data.get()?.brs?.`_$1080`
                        ?: data.get()?.brs?.`_$720`
                        ?: data.get()?.brs?.`_$480`
                        ?: data.get()?.brs?.`_$240`
                        ?: ""
                    videoPlayer.setUp(url, false ,data.get()?.name)
                    videoPlayer.startPlayLogic()
                }
            }

        })
    }

    override fun onBackPressed() {
        orientationUtils?.backToProtVideo()
        if(GSYVideoManager.backFromWindowFull(this))
            return
        super.onBackPressed()
    }

    /**
     * 初始化 VideoView 的配置
     */
    private fun initVideoViewConfig() {
        //设置旋转
        orientationUtils = OrientationUtils(this, videoPlayer)
        orientationUtils?.isEnable = false
        videoPlayer.apply {
            //是否旋转
            isRotateViewAuto = false
            //是否可以滑动调整
            setIsTouchWiget(true)
            setVideoAllCallBack(object : GSYSampleCallBack(){
                override fun onPrepared(url: String?, vararg objects: Any?) {
                    super.onPrepared(url, *objects)
                    //开始播放了才能旋转和全屏
                    orientationUtils?.isEnable = true
                    mViewModel.isPlaying.set(true)
                }

                override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                    super.onQuitFullscreen(url, *objects)
                    //列表返回的样式判断
                    orientationUtils?.backToProtVideo()
                }

            })
            //设置返回按键功能
            backButton.setOnClickListener { onBackPressed() }
            //设置全屏按键功能
            fullscreenButton.setOnClickListener {
                //直接横屏
                orientationUtils?.resolveByClick()
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                startWindowFullscreen(this@VideoPlayerActivity, true, true)
            }
            //锁屏事件
            setLockClickListener { view, lock ->
                //配合下方的onConfigurationChanged
                orientationUtils?.isEnable = !lock
            }
        }


    }
    override fun onResume() {
        videoPlayer.currentPlayer.onVideoResume(false)
        super.onResume()
        mViewModel.isPause.set(false)


    }
    override fun onPause() {
        videoPlayer.currentPlayer.onVideoPause()
        super.onPause()
        mViewModel.isPause.set(false)


    }
    override fun onDestroy() {
        super.onDestroy()
        if(mViewModel.isPlaying.get())
            videoPlayer.currentPlayer.release()
        orientationUtils?.releaseListener()
    }
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏
        if(mViewModel.isPlaying.get() && !mViewModel.isPause.get())
            videoPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true)
    }
}