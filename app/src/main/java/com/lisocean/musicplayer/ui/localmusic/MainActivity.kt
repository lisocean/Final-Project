package com.lisocean.musicplayer.ui.localmusic

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.databinding.ActivityMainBinding
import com.lisocean.musicplayer.ui.localmusic.viewmodel.LocalMusicViewModel
import com.lisocean.musicplayer.ui.localmusic.adapter.LmPagerAdapter
import com.lisocean.musicplayer.util.Constants
import com.lisocean.musicplayer.util.argument
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {


    /**
     * get config for local
     */
    private val musicId by argument<Int>(Constants.MUSIC_ID)

    private val mViewModel by viewModel<LocalMusicViewModel> { parametersOf(musicId) }

    private val mBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding data
        mBinding.vm = mViewModel
        getViewModel<LocalMusicViewModel>()
        mViewModel.loadData()

        setSupportActionBar(toolbar)
        val adapter = LmPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
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
                searchView = item.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }

                })
            }
            R.id.menu_other -> toast("其他")
            R.id.menu_cycling -> toast("扫描本地音乐")
        }
        return true
    }


}
