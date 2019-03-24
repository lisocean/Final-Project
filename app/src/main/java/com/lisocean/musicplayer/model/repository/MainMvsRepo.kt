package com.lisocean.musicplayer.model.repository

import com.lisocean.musicplayer.model.remote.MvService

class MainMvsRepo(private val remote : MvService) {
    fun getMainMvs() = remote.getMainMvs()
}