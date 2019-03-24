package com.lisocean.musicplayer.model.data.search

import com.google.gson.annotations.SerializedName

class MvDetail {

    /**
     * loadingPic :
     * bufferPic :
     * loadingPicFS :
     * bufferPicFS :
     * subed : false
     * data : {"id":5436712,"name":"广岛之恋","artistId":8926,"artistName":"莫文蔚","briefDesc":"","desc":null,"cover":"http://p1.music.126.net/ijUg7s_2S8GMbTNsYiepJA==/18676304511774727.jpg","coverId":18676304511774730,"playCount":470470,"subCount":6697,"shareCount":735,"likeCount":3182,"commentCount":312,"duration":379000,"nType":0,"publishTime":"1997-10-01","brs":{"240":"http://vodkgeyttp8.vod.126.net/cloudmusic/MjQ3NDQ3MjUw/89a6a279dc2acfcd068b45ce72b1f560/bf2750483ed02d4c6263dffefa5959d7.mp4?wsSecret=9a18d8fafe043303e0e78c3d7ef3e96b&wsTime=1551948287","480":"http://vodkgeyttp8.vod.126.net/cloudmusic/MjQ3NDQ3MjUw/89a6a279dc2acfcd068b45ce72b1f560/533e4183a709699d566180ed0cd9abe9.mp4?wsSecret=c352c384f431eec3f51428e69f54c395&wsTime=1551948287"},"artists":[{"id":8926,"name":"莫文蔚"},{"id":6481,"name":"张洪量"}],"isReward":false,"commentThreadId":"R_MV_5_5436712"}
     * code : 200
     */

    var loadingPic: String? = null
    var bufferPic: String? = null
    var loadingPicFS: String? = null
    var bufferPicFS: String? = null
    var isSubed: Boolean = false
    var data: DataBean? = null
    var code: Int = 0

    class DataBean {
        /**
         * id : 5436712
         * name : 广岛之恋
         * artistId : 8926
         * artistName : 莫文蔚
         * briefDesc :
         * desc : null
         * cover : http://p1.music.126.net/ijUg7s_2S8GMbTNsYiepJA==/18676304511774727.jpg
         * coverId : 18676304511774730
         * playCount : 470470
         * subCount : 6697
         * shareCount : 735
         * likeCount : 3182
         * commentCount : 312
         * duration : 379000
         * nType : 0
         * publishTime : 1997-10-01
         * brs : {"240":"http://vodkgeyttp8.vod.126.net/cloudmusic/MjQ3NDQ3MjUw/89a6a279dc2acfcd068b45ce72b1f560/bf2750483ed02d4c6263dffefa5959d7.mp4?wsSecret=9a18d8fafe043303e0e78c3d7ef3e96b&wsTime=1551948287","480":"http://vodkgeyttp8.vod.126.net/cloudmusic/MjQ3NDQ3MjUw/89a6a279dc2acfcd068b45ce72b1f560/533e4183a709699d566180ed0cd9abe9.mp4?wsSecret=c352c384f431eec3f51428e69f54c395&wsTime=1551948287"}
         * artists : [{"id":8926,"name":"莫文蔚"},{"id":6481,"name":"张洪量"}]
        * isReward : false
        * commentThreadId : R_MV_5_5436712
        */

        var id: Int = 0
        var name: String? = null
        var artistId: Int = 0
        var artistName: String? = null
        var briefDesc: String? = null
        var desc: Any? = null
        var cover: String? = null
        var coverId: Long = 0
        var playCount: Int = 0
        var subCount: Int = 0
        var shareCount: Int = 0
        var likeCount: Int = 0
        var commentCount: Int = 0
        var duration: Int = 0
        var nType: Int = 0
        var publishTime: String? = null
        var brs: BrsBean? = null
        var isIsReward: Boolean = false
        var commentThreadId: String? = null
        var artists: List<ArtistsBean>? = null

        class BrsBean {
            /**
            * 240 : http://vodkgeyttp8.vod.126.net/cloudmusic/MjQ3NDQ3MjUw/89a6a279dc2acfcd068b45ce72b1f560/bf2750483ed02d4c6263dffefa5959d7.mp4?wsSecret=9a18d8fafe043303e0e78c3d7ef3e96b&wsTime=1551948287
            * 480 : http://vodkgeyttp8.vod.126.net/cloudmusic/MjQ3NDQ3MjUw/89a6a279dc2acfcd068b45ce72b1f560/533e4183a709699d566180ed0cd9abe9.mp4?wsSecret=c352c384f431eec3f51428e69f54c395&wsTime=1551948287
            */

            @SerializedName("240")
            var `_$240`: String? = null
            @SerializedName("480")
            var `_$480`: String? = null
            @SerializedName("720")
            var `_$720`: String? = null
            @SerializedName("1080")
            var `_$1080`: String? = null
        }

        class ArtistsBean {
            /**
             * id : 8926
             * name : 莫文蔚
             */

            var id: Int = 0
            var name: String? = null
        }
    }
}