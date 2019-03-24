package com.lisocean.musicplayer.model.data.search

class VideoDetail {

    /**
     * code : 200
     * data : {"advertisement":false,"authType":0,"avatarUrl":"http://p1.music.126.net/I68CkTFEMbueMJHA8jKzGw==/109951163652344557.jpg","commentCount":891,"coverUrl":"http://p1.music.126.net/RxBSKhZXMee5gn5Gg2p8TQ==/109951163573071019.jpg","creator":{"accountStatus":0,"authStatus":0,"avatarUrl":"http://p1.music.126.net/I68CkTFEMbueMJHA8jKzGw==/109951163652344557.jpg","followed":false,"nickname":"Dawn_夜明","userId":124483968,"userType":0},"description":"许嵩《燕归巢》乐器录制花絮（吉他 马头琴 琵琶 古筝）","durationms":485018,"hasRelatedGameAd":false,"height":540,"markTypes":[109],"playTime":519052,"praisedCount":5306,"publishTime":1520643498000,"resolutions":[{"resolution":240,"size":54157476},{"resolution":480,"size":77364876}],"shareCount":1095,"subscribeCount":2192,"threadId":"R_VI_62_89ADDE33C0AAE8EC14B99F6750DB954D","title":"许嵩《燕归巢》乐器录制花絮","vid":"89ADDE33C0AAE8EC14B99F6750DB954D","videoGroup":[{"alg":"groupTagRank","id":15180,"name":"许嵩"},{"alg":"groupTagRank","id":16132,"name":"作品信息"},{"alg":"groupTagRank","id":13222,"name":"华语"},{"alg":"groupTagRank","id":4101,"name":"娱乐"}],"width":960}
     * message : success
     */

    var code: Int = 0
    var data: DataBean? = null
    var message: String? = null

    class DataBean {
        /**
         * advertisement : false
         * authType : 0
         * avatarUrl : http://p1.music.126.net/I68CkTFEMbueMJHA8jKzGw==/109951163652344557.jpg
         * commentCount : 891
         * coverUrl : http://p1.music.126.net/RxBSKhZXMee5gn5Gg2p8TQ==/109951163573071019.jpg
         * creator : {"accountStatus":0,"authStatus":0,"avatarUrl":"http://p1.music.126.net/I68CkTFEMbueMJHA8jKzGw==/109951163652344557.jpg","followed":false,"nickname":"Dawn_夜明","userId":124483968,"userType":0}
         * description : 许嵩《燕归巢》乐器录制花絮（吉他 马头琴 琵琶 古筝）
         * durationms : 485018
         * hasRelatedGameAd : false
         * height : 540
         * markTypes : [109]
         * playTime : 519052
         * praisedCount : 5306
         * publishTime : 1520643498000
         * resolutions : [{"resolution":240,"size":54157476},{"resolution":480,"size":77364876}]
         * shareCount : 1095
         * subscribeCount : 2192
         * threadId : R_VI_62_89ADDE33C0AAE8EC14B99F6750DB954D
         * title : 许嵩《燕归巢》乐器录制花絮
         * vid : 89ADDE33C0AAE8EC14B99F6750DB954D
         * videoGroup : [{"alg":"groupTagRank","id":15180,"name":"许嵩"},{"alg":"groupTagRank","id":16132,"name":"作品信息"},{"alg":"groupTagRank","id":13222,"name":"华语"},{"alg":"groupTagRank","id":4101,"name":"娱乐"}]
         * width : 960
         */

        var isAdvertisement: Boolean = false
        var authType: Int = 0
        var avatarUrl: String? = null
        var commentCount: Int = 0
        var coverUrl: String? = null
        var creator: CreatorBean? = null
        var description: String? = null
        var durationms: Int = 0
        var isHasRelatedGameAd: Boolean = false
        var height: Int = 0
        var playTime: Int = 0
        var praisedCount: Int = 0
        var publishTime: Long = 0
        var shareCount: Int = 0
        var subscribeCount: Int = 0
        var threadId: String? = null
        var title: String? = null
        var vid: String? = null
        var width: Int = 0
        var markTypes: List<Int>? = null
        var resolutions: List<ResolutionsBean>? = null
        var videoGroup: List<VideoGroupBean>? = null

        class CreatorBean {
            /**
             * accountStatus : 0
             * authStatus : 0
             * avatarUrl : http://p1.music.126.net/I68CkTFEMbueMJHA8jKzGw==/109951163652344557.jpg
             * followed : false
             * nickname : Dawn_夜明
             * userId : 124483968
             * userType : 0
             */

            var accountStatus: Int = 0
            var authStatus: Int = 0
            var avatarUrl: String? = null
            var isFollowed: Boolean = false
            var nickname: String? = null
            var userId: Int = 0
            var userType: Int = 0
        }

        class ResolutionsBean {
            /**
             * resolution : 240
             * size : 54157476
             */

            var resolution: Int = 0
            var size: Int = 0
        }

        class VideoGroupBean {
            /**
             * alg : groupTagRank
             * id : 15180
             * name : 许嵩
             */

            var alg: String? = null
            var id: Int = 0
            var name: String? = null
        }
    }
}
