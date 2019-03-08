package com.lisocean.musicplayer.ui.localmusic.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.lisocean.musicplayer.ui.DefaultFragment
import com.lisocean.musicplayer.ui.localmusic.SingleMusicFragment

class LmPagerAdapter (val context : Context?, fm: FragmentManager? = null) : FragmentPagerAdapter(fm) {
    private val fragmentMap by lazy {
        mapOf(0 to ("Single" to SingleMusicFragment()),
              1 to ("Singer" to DefaultFragment()),
              2 to ("Album" to DefaultFragment()),
              3 to ("Folder" to DefaultFragment()))
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentMap[position]?.first
    }
    override fun getItem(p0: Int): Fragment? {
        return fragmentMap[p0]?.second

    }


    override fun getCount(): Int {
        return fragmentMap.size
    }
}