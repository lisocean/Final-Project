package com.lisocean.musicplayer.model.data.search

class VideoAddress {

    /**
     * urls : [{"id":"89ADDE33C0AAE8EC14B99F6750DB954D","url":"http://vodkgeyttp9.vod.126.net/vodkgeyttp8/68sKFbGS_1328070069_hd.mp4?wsSecret=7fb0c4f8f4e29920057284b28fa1f1d7&wsTime=1553449101&ext=NnR5gMvHcZNcbCz592mDGUGuDOFN18isir07K1EOfL12tUM6SXBCkQ529iWnffW45To9wQga1eVqM%2FmHQAq7Ca4rgqoSo8QreQjEUkp8YeMAFz92JXFHHC4xGKI28fb1xGOmljOOwYwaa1fhE%2BIclF%2FXqLJTkU5VuzSmp%2F5n5TuBBYPSphRcHvxc9W2J5XjhAs%2FF8aUf7sHzOpCvO8qhNf9pbx%2FMYVwNqQlUsa9K7GZzcwcIyQwoO9svVjRZqJDt","size":77364876,"validityTime":1200,"needPay":false,"payInfo":null,"r":480}]
     * code : 200
     */

    var code: Int = 0
    var urls: List<UrlsBean>? = null

    class UrlsBean {
        /**
         * id : 89ADDE33C0AAE8EC14B99F6750DB954D
         * url : http://vodkgeyttp9.vod.126.net/vodkgeyttp8/68sKFbGS_1328070069_hd.mp4?wsSecret=7fb0c4f8f4e29920057284b28fa1f1d7&wsTime=1553449101&ext=NnR5gMvHcZNcbCz592mDGUGuDOFN18isir07K1EOfL12tUM6SXBCkQ529iWnffW45To9wQga1eVqM%2FmHQAq7Ca4rgqoSo8QreQjEUkp8YeMAFz92JXFHHC4xGKI28fb1xGOmljOOwYwaa1fhE%2BIclF%2FXqLJTkU5VuzSmp%2F5n5TuBBYPSphRcHvxc9W2J5XjhAs%2FF8aUf7sHzOpCvO8qhNf9pbx%2FMYVwNqQlUsa9K7GZzcwcIyQwoO9svVjRZqJDt
         * size : 77364876
         * validityTime : 1200
         * needPay : false
         * payInfo : null
         * r : 480
         */

        var id: String? = null
        var url: String? = null
        var size: Int = 0
        var validityTime: Int = 0
        var isNeedPay: Boolean = false
        var payInfo: Any? = null
        var r: Int = 0
    }
}
