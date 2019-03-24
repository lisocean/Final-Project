package com.lisocean.musicplayer.ui.localmusic.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.lisocean.musicplayer.ui.DefaultFragment
import com.lisocean.musicplayer.ui.localmusic.fragment.LocalMusicFragment
import com.lisocean.musicplayer.ui.localmusic.fragment.MainMvsFragment

class LmPagerAdapter (val context : Context?, fm: FragmentManager? = null) : FragmentPagerAdapter(fm) {
    private val fragmentMap by lazy {
        mapOf(0 to ("Local" to LocalMusicFragment()),
              1 to ("NewMusic" to DefaultFragment()),
              2 to ("Album" to DefaultFragment()),
              3 to ("MV" to MainMvsFragment()))
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