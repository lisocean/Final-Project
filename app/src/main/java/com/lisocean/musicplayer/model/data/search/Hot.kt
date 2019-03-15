package com.lisocean.musicplayer.model.data.search

class Hot {

    /**
     * code : 200
     * result : {"hots":[{"first":"张艺兴远东韵律","second":1,"third":null,"iconType":1},{"first":"吴青峰新歌","second":1,"third":null,"iconType":0},{"first":"绿色","second":1,"third":null,"iconType":0},{"first":"董又霖就喜欢你","second":1,"third":null,"iconType":0},{"first":"以团之名","second":1,"third":null,"iconType":0},{"first":"起风了","second":1,"third":null,"iconType":0},{"first":"我曾","second":1,"third":null,"iconType":0},{"first":"告白之夜","second":1,"third":null,"iconType":0},{"first":"阿丽塔","second":1,"third":null,"iconType":0},{"first":"A妹新专辑","second":1,"third":null,"iconType":0}]}
     */

    var code: Int = 0
    var result: ResultBean? = null

    class ResultBean {
        var hots: List<HotsBean>? = null

        class HotsBean {
            /**
             * first : 张艺兴远东韵律
             * second : 1
             * third : null
             * iconType : 1
             */

            var first: String? = null
            var second: Int = 0
            var third: Any? = null
            var iconType: Int = 0
        }
    }
}
