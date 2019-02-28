package com.lisocean.musicplayer.util

import android.database.Cursor
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media as S
import com.lisocean.musicplayer.model.AudioMediaBean


@JvmName("ParseCursor")
/**
 * 根据特定位置上的it获取bean
 */
fun getAudioMediaBean(cursor: Cursor?): AudioMediaBean {
    val audioMediaBean =
        AudioMediaBean("","", 0, "", "",
            0,"","",0,"",0)
    return audioMediaBean.apply {
        cursor?.let {
            id = it.getString(it.getColumnIndex(S._ID))
            data = it.getString(it.getColumnIndex(S.DATA))
            size = it.getLong(it.getColumnIndex(S.SIZE))
            display_name = it.getString(it.getColumnIndex(S.DISPLAY_NAME))
            display_name = display_name.substring(0, display_name.lastIndexOf("."))
            artist = it.getString(it.getColumnIndex(S.ARTIST))
            duration = it.getLong(it.getColumnIndex(S.DURATION))
            title = it.getString(it.getColumnIndex(S.TITLE))
            album = it.getString(it.getColumnIndex(S.ALBUM))
            album_id = it.getLong(it.getColumnIndex(S.ALBUM_ID))
            mime_type = it.getString(it.getColumnIndex(S.MIME_TYPE))
            artist_id = it.getLong(it.getColumnIndex(S.ARTIST_ID))
        }
    }
}

fun parseCursor(cursor : Cursor?) : List<AudioMediaBean>{
    val array = mutableListOf<AudioMediaBean>()
    cursor?.let {
        it.moveToPosition(-1)
        while (it.moveToNext()){
            array.add(getAudioMediaBean(it))
        }
    }
    return array
}