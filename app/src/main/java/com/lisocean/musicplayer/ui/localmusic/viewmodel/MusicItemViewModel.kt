package com.lisocean.musicplayer.ui.localmusic.viewmodel

import com.lisocean.musicplayer.model.data.local.AudioMediaBean

/**
 *
歌曲id         Int               188788
歌曲路径      String              /storage/emulated/0/netease/cloudmusic/Music/锦零 - 有何不可（Cover 许嵩）.mp3
歌曲大小     Long                 9796891
完整歌名字    String              锦零 - 有何不可（Cover 许嵩）.mp3
歌手显示使用     String           锦零
播放时间长       Long             242651
歌名显示使用     String           有何不可（Cover 许嵩）
专辑名字         String           zero
专辑排序使用     Long              50
歌曲播放品质  String              audio/mpeg  or  audio/quicktime
歌手的id 排序使用   Long            43
 */
/**
 * var id : Int, var data : String, var size : Long,
var display_name : String, var artist : String, var duration : Long,
var title : String, var album : String, var album_id : Long,
var mime_type : String, var artist_id: Long
 */
class MusicItemViewModel(i : Int ,val local : AudioMediaBean) {
    val index = (i + 1).toString()
    val id = -1
    val data = local.data
    val size = local.size
    val display_name = local.display_name
    val duration =local.duration
    val mime_type = local.mime_type
    val album = local.album
    val title = local.title
    val artist = local.artist
}