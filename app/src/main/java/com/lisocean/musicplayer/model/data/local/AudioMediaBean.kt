package com.lisocean.musicplayer.model.data.local

import android.os.Parcel
import android.os.Parcelable

/**
 *
MediaStore.Audio.Media._ID,           歌曲id         Int               188788
MediaStore.Audio.Media.DATA,          歌曲路径      String              /storage/emulated/0/netease/cloudmusic/Music/锦零 - 有何不可（Cover 许嵩）.mp3
MediaStore.Audio.Media.SIZE,          歌曲大小     Long                 9796891
MediaStore.Audio.Media.DISPLAY_NAME,  完整歌名字    String              锦零 - 有何不可（Cover 许嵩）.mp3
MediaStore.Audio.Media.ARTIST,        歌手显示使用     String           锦零
MediaStore.Audio.Media.DURATION,      播放时间长       Long             242651
MediaStore.Audio.Media.TITLE,         歌名显示使用     String           有何不可（Cover 许嵩）
MediaStore.Audio.Media.ALBUM,         专辑名字         String           zero
MediaStore.Audio.Media.ALBUM_ID,      专辑排序使用     Long              50
MediaStore.Audio.Media.MIME_TYPE,     歌曲播放品质  String              audio/mpeg  or  audio/quicktime
MediaStore.Audio.Media.ARTIST_ID      歌手的id 排序使用   Long            43
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class AudioMediaBean(var id : Int, var data : String, var size : Long,
                          var display_name : String, var artist : String, var duration : Long,
                          var title : String, var album : String, var album_id : Long,
                          var mime_type : String, var artist_id: Long) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readLong()

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(data)
        parcel.writeLong(size)
        parcel.writeString(display_name)
        parcel.writeString(artist)
        parcel.writeLong(duration)
        parcel.writeString(title)
        parcel.writeString(album)
        parcel.writeLong(album_id)
        parcel.writeString(mime_type)
        parcel.writeLong(artist_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AudioMediaBean> {
        override fun createFromParcel(parcel: Parcel): AudioMediaBean {
            return AudioMediaBean(parcel)
        }

        override fun newArray(size: Int): Array<AudioMediaBean?> {
            return arrayOfNulls(size)
        }
    }

}