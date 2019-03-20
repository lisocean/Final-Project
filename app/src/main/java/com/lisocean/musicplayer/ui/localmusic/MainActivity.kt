package com.lisocean.musicplayer.ui.localmusic

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.*
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.databinding.ActivityMainBinding
import com.lisocean.musicplayer.ui.localmusic.viewmodel.LocalMusicViewModel
import com.lisocean.musicplayer.ui.localmusic.adapter.LmPagerAdapter
import com.lisocean.musicplayer.helper.constval.Constants
import com.lisocean.musicplayer.helper.ex.argumentInt
import com.lisocean.musicplayer.helper.ex.setArgumentInt
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.service.PlayingService
import com.lisocean.musicplayer.ui.base.BaseActivity
import com.lisocean.musicplayer.ui.musicplaying.MusicPlayingActivity
import com.lisocean.musicplayer.ui.presenter.Presenter
import com.lisocean.musicplayer.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.*
import org.jetbrains.anko.collections.forEachWithIndex
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


@Suppress("DEPRECATION")
class MainActivity : BaseActivity(), Presenter{


    /**
     * get config for local
     */
    private val musicId by argumentInt(Constants.MUSIC_ID)

    private val mViewModel by viewModel<LocalMusicViewModel>{parametersOf(musicId)}

    private val mBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding data
        mBinding.vm = mViewModel
        mBinding.presenter = this
        initObserve()
        /**
         * permission
         */
        val checkSelfPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
        if(checkSelfPermission != PackageManager.PERMISSION_GRANTED)
        {
            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions,1)
        }

        setSupportActionBar(toolbar)
        val adapter = LmPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        if(presenter == null){
            startService(mViewModel.list, mViewModel.position.get())
        }

        EventBus.getDefault().register(this)
    }
    override fun onStart() {
        super.onStart()
        presenter?.notifyUpdateUi()
    }


    @Suppress("UNCHECKED_CAST")
    private fun initObserve() {
        mViewModel.currentSong.addOnPropertyChangedCallback(
            object : Observable.OnPropertyChangedCallback(){
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    sender?.let {
                        val songInfo = it as ObservableField<SongInfo>
                        mViewModel.picUrl.set(songInfo.get()?.pictureUrl)
                        presenter?.playingSong(songInfo.get() ?: SongInfo())
                        mViewModel.isPlaying.set(true)
                        var positionTemp = -1
                        mViewModel.list.forEachWithIndex { index, song ->
                            if (song.id == songInfo.get()?.id)
                                positionTemp = index
                        }
                        this@MainActivity.setArgumentInt(Constants.MUSIC_ID,songInfo.get()?.id ?: 0)
                        mViewModel.position.set(positionTemp)
                    }
                }

            })
        mViewModel.isPlaying.addOnPropertyChangedCallback(
            object : Observable.OnPropertyChangedCallback(){
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {

                    sender?.let {
                        val isPlaying = it as ObservableBoolean
                        if(isPlaying.get()){
                            presenter?.apply {
                                playing()
                                find<View>(R.id.bottom_play_button).isSelected = true
                            }

                        }else{
                            find<View>(R.id.bottom_play_button).isSelected = false
                            presenter?.pause()
                        }

                    }
                }

            }
        )
    }

    private var isFirst = true
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun UpdateState(serviceState: PlayingService.ServiceState){
        if(isFirst)
        {
            isFirst = false
            return
        }
        presenter?.setOnFinishListener {
            mViewModel.currentSong.set(it)
        }

        if(serviceState.isPlaying) {
            mViewModel.currentSong.set(serviceState.playSongInfo)
            find<View>(R.id.bottom_play_button).isSelected = true
        }else{
            mViewModel.currentSong.set(serviceState.playSongInfo)
            mViewModel.isPlaying.set(false)
            presenter?.pause()
        }

        mViewModel.picUrl.set(serviceState.playSongInfo.pictureUrl)
    }

    /**
     * after get the permission
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            mViewModel.loadData()
        }
    }
    /**
     * be of toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_search -> {
                val options =  ActivityOptionsCompat.makeSceneTransitionAnimation(this, iv_search, getString(R.string.search_transition_name))
                startActivity(Intent(this, SearchActivity::class.java), options.toBundle())
            }
            R.id.menu_other -> {
                toast("other")
            }
            R.id.menu_cycling -> {
                val dialog = indeterminateProgressDialog("Synchronize the music")
                dialog.show()
                mViewModel.addDataToApp {
                    dialog.cancel()
                    toast("Synchronize successfully")
                }
                mViewModel.loadData()
            }
        }
        return true
    }

    override fun onClick(v: View?) {
        if(presenter?.getPlayingSongs()?.size == 0){
            presenter?.updateSongs(mViewModel.list)
            presenter?.playingSong(mViewModel.currentSong.get() ?: SongInfo())
            presenter?.pause()
        }
        when(v?.id){
            R.id.bottom_main -> {
                val intent = Intent(this, MusicPlayingActivity::class.java)
                val array = arrayListOf<SongInfo>().apply{addAll(presenter?.getPlayingSongs() ?: listOf())}
                intent.putParcelableArrayListExtra("list", array)
                intent.putExtra("position", presenter?.getPosition())
                intent.putExtra("isPlaying",mViewModel.isPlaying.get())
                startActivity(intent)
            }
            R.id.bottom_play_button -> {

                /**
                 * change player button state
                 * then change service state
                 */
                when(v.isSelected){
                    false ->
                        mViewModel.isPlaying.set(true)
                    true ->
                        mViewModel.isPlaying.set(false)
                }

            }
            R.id.bottom_popup_more -> {
                mViewModel.position.set(mViewModel.position.get() + 1)
                mViewModel.currentSong.set(mViewModel.list[mViewModel.position.get()])
            }
        }
    }


}
