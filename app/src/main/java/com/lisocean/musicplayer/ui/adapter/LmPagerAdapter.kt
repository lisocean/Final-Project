package com.lisocean.musicplayer.ui.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.lisocean.musicplayer.ui.fragment.DefaultFragment
import com.lisocean.musicplayer.ui.fragment.SingleMusicFragment

class LmPagerAdapter (val context : Context?, fm: FragmentManager? = null) : FragmentPagerAdapter(fm) {
    private val fragmentMap by lazy {
        mapOf(0 to ("单曲" to SingleMusicFragment()),
              1 to ("歌手" to DefaultFragment()),
              2 to ("专辑" to DefaultFragment()),
              3 to ("文件夹" to DefaultFragment()))
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