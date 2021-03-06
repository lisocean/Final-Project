package com.lisocean.musicplayer.helper.constval

import android.os.Environment

object Constants {
    val HOST_API = "http://148.70.143.105:3000/"
    /**
     * 将 https://music.163.com/song/media/outer/url?id=id.mp3 以 src 赋予 Audio 即可播放
     * 必选参数 : id : 音乐 id
     *可选参数 : br: 码率,默认设置了 999000 即最大码率,如果要 320k 则可设置为 320000,其他类推
     *
     *replace origin url to resolve 403
     */
    val HOST_REPLACE_API = "https://music.163.com/song/media/outer/url?id="

    const val  DB_NAME : String = "musicplayer.db"

    const val CONFIG_INFO : String = "configstate"

    const val  MUSIC_ID : String = "musicidtokeep"

    const val MUSIC_PLAYER_MODE : String = "musicplayermode"
    /**
     * cache
     */
    val path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/app/com.lisocean.musicplayer/cached/listCache.x"
    val currentSongPath = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/app/com.lisocean.musicplayer/cached/currentSongCache.x"


}