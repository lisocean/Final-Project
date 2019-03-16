package com.lisocean.musicplayer.ui.localmusic

import android.Manifest
import android.annotation.TargetApi
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import com.lisocean.musicplayer.BR
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.databinding.ActivityMainBinding
import com.lisocean.musicplayer.ui.localmusic.viewmodel.LocalMusicViewModel
import com.lisocean.musicplayer.ui.localmusic.adapter.LmPagerAdapter
import com.lisocean.musicplayer.helper.Constants
import com.lisocean.musicplayer.helper.argument
import com.lisocean.musicplayer.helper.argumentInt
import com.lisocean.musicplayer.service.AudioService
import com.lisocean.musicplayer.service.Iservice
import com.lisocean.musicplayer.ui.musicplaying.MusicPlayingActivity
import com.lisocean.musicplayer.ui.presenter.ItemClickPresenter
import com.lisocean.musicplayer.ui.presenter.Presenter
import com.lisocean.musicplayer.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.startActivity
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), Presenter{


    /**
     * get config for local
     */
    private val musicId by argumentInt(Constants.MUSIC_ID)

    private val mViewModel by viewModel<LocalMusicViewModel>{parametersOf(musicId)}

    private val mBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }
    private val conn by lazy { AudioConnection() }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding data
        mBinding.vm = mViewModel
        mBinding.presenter = this
        /**
         * permission
         */
        val checkSelfPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
        if(checkSelfPermission != PackageManager.PERMISSION_GRANTED)
        {
            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions,1)
        }

      //  EventBus.getDefault().register(this)
        setSupportActionBar(toolbar)
        val adapter = LmPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
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
    private lateinit var searchView: SearchView
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
                mViewModel.addDataToApp{
                    dialog.cancel()
                    toast("Synchronize successfully")}
                mViewModel.loadData()

            }
        }
        return true
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bottom_main -> {
                val intent = Intent(this, MusicPlayingActivity::class.java)
                intent.putParcelableArrayListExtra("list", mViewModel.list)

                startActivity(intent)

            }
            R.id.bottom_play_button -> {
                if(iService == null)
                    startService()
                /**
                 * change player button state
                 * then change service state
                 */
                v.isSelected = when(v.isSelected){
                    false ->true
                    true ->false
                }
                iService?.updatePlayState()
            }
            R.id.bottom_popup_more -> toast("popup")
        }
    }


    /***
     * start service for once
     */
    private fun startService(){
        /**
         * start service
         */
        val intent = Intent(this,  AudioService::class.java)
        intent.putParcelableArrayListExtra("list", mViewModel.localAudioList)

        intent.putExtra("position", 0)

        bindService(intent, conn, Context.BIND_AUTO_CREATE)
        startService(intent)
    }
    var iService: Iservice? = null
    inner class AudioConnection : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            iService = p1 as Iservice
        }
        override fun onServiceDisconnected(p0: ComponentName?) {

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        unbindService(conn)
   //   EventBus.getDefault().unregister(this)
    }



}
