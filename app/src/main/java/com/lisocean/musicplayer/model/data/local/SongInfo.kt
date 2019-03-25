package com.lisocean.musicplayer.model.data.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.lisocean.musicplayer.model.data.search.MusicList
import com.lisocean.musicplayer.model.data.search.SongsDetail
import com.lisocean.musicplayer.model.data.search.recommend.RdPlaylistDetail
import java.io.Serializable

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Entity(tableName = "songs")
class SongInfo() : Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "songid")
    var id : Int = -1
    var mvid : Int = -1
    var name : String = ""
    var duration : Int = -1
    var albumId : Int = -1
    var albumname : String = ""
    var artists : String = ""
    var artistsId : String = ""
    var data : String = ""
    var pictureUrl : String = ""


    fun inject(data : Any) : SongInfo{
        when(data){
            is MusicList.ResultBean.SongsBean -> injectFromResult(data)
            is SongsDetail.SongsBean -> injectFromDetail(data)
            is AudioMediaBean -> injectFromAudio(data)
            is RdPlaylistDetail.PlaylistBean.TracksBean -> injectFromRdDetail(data)
            else -> throw Throwable("inject from is error")
        }
        return this
    }

    private fun injectFromRdDetail(data: RdPlaylistDetail.PlaylistBean.TracksBean) {
        id = data.id
        mvid = data.mv
        name = data.name ?: ""
        duration = data.dt
        albumId  = data.al?.id ?: -1
        albumname = data.al?.name ?: ""
        data.ar?.let {
            artists = it.joinToString(separator = "/") { art -> "${art.name}"}
            artistsId = it.joinToString(separator = "/") { art -> "${art.id}"}
        }
        pictureUrl = data.al?.picUrl ?: ""
    }

    private fun injectFromAudio(data: AudioMediaBean){
        id = 0
        mvid = 0
        name = data.title
        duration = data.duration.toInt()
        albumname  = data.album
        albumId = data.album_id.toInt()
        artists = data.artist
        artistsId = data.artist_id.toString()
        this.data = data.data
    }
    private fun injectFromResult(data : MusicList.ResultBean.SongsBean){
        id = data.id
        mvid = data.mvid
        name = data.name ?: ""
        duration = data.duration
        albumId  = data.album?.id ?: -1
        albumname = data.album?.name ?: ""
        data.artists?.let {
            artists = it.joinToString(separator = "/") { art -> "${art.name}"}
            artistsId = it.joinToString(separator = "/") { art -> "${art.id}"}
        }
    }

    private fun injectFromDetail(data : SongsDetail.SongsBean){
        id = data.id
        mvid = data.mv
        name = data.name ?: ""
        duration = data.dt
        albumId  = data.al?.id ?: -1
        albumname = data.al?.name ?: ""
        data.ar?.let {
            artists = it.joinToString(separator = "/") { art -> "${art.name}"}
            artistsId = it.joinToString(separator = "/") { art -> "${art.id}"}
        }
        pictureUrl = data.al?.picUrl ?: ""
    }



    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        mvid = parcel.readInt()
        name = parcel.readString()
        duration = parcel.readInt()
        albumId = parcel.readInt()
        albumname = parcel.readString()
        artists = parcel.readString()
        artistsId = parcel.readString()
        data = parcel.readString()
        pictureUrl = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(mvid)
        parcel.writeString(name)
        parcel.writeInt(duration)
        parcel.writeInt(albumId)
        parcel.writeString(albumname)
        parcel.writeString(artists)
        parcel.writeString(artistsId)
        parcel.writeString(data)
        parcel.writeString(pictureUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SongInfo> {
        override fun createFromParcel(parcel: Parcel): SongInfo {
            return SongInfo(parcel)
        }

        override fun newArray(size: Int): Array<SongInfo?> {
            return arrayOfNulls(size)
        }
    }

}