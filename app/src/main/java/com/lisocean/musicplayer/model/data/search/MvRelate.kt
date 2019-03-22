package com.lisocean.musicplayer.model.data.search

class MvRelate {

    /**
     * code : 200
     * message : success
     * data : [{"type":0,"title":"如果没有你","durationms":286000,"creator":[{"userId":8926,"userName":"莫文蔚"}],"playTime":884150,"coverUrl":"http://p1.music.126.net/MAJQ1BwS3bSripKjUflPfg==/1375489051002253.jpg","vid":"5307751","aliaName":null,"transName":null,"alg":"icf3","markTypes":null},{"type":1,"title":"莫文蔚、张智霖演唱《广岛之恋》，赏心悦目的现场","durationms":356309,"creator":[{"userId":449979212,"userName":"全球潮音乐"}],"playTime":286809,"coverUrl":"http://p1.music.126.net/KzYfVUE6fahWRpEtbHNdvQ==/109951163572684051.jpg","vid":"AEFE97956A6A767FCCDC9622952F7F67","aliaName":null,"transName":null,"alg":"icf3","markTypes":[109]},{"type":1,"title":"马頔/宋冬野《广岛之恋》深情对唱","durationms":203198,"creator":[{"userId":69805292,"userName":"独音记"}],"playTime":148638,"coverUrl":"http://p1.music.126.net/CO0JqUrF9sVBuFb8qQLnLQ==/109951163747491163.jpg","vid":"253DD019930358699BC0D7C78DAAABAE","aliaName":null,"transName":null,"alg":"icf3","markTypes":[111]},{"type":1,"title":"李易峰吴昕合唱《广岛之恋》，怎么样？","durationms":125040,"creator":[{"userId":290781345,"userName":"全球音乐影视排行榜"}],"playTime":11428,"coverUrl":"http://p1.music.126.net/SPMHBBq4c_vf0fU2EhSpfA==/109951163857139434.jpg","vid":"E8910F71F206D6973A86A14A32BFC8C0","aliaName":null,"transName":null,"alg":"icf3","markTypes":[]},{"type":1,"title":"还记得这首老歌《广岛之恋》吗，曾经非常流行KTV必点歌曲","durationms":324320,"creator":[{"userId":1436382,"userName":"MTV音乐之旅"}],"playTime":16491,"coverUrl":"http://p1.music.126.net/c1bKjZ8rNzcgHBWt5IJJwA==/109951163574168700.jpg","vid":"393C4DF6D66D9A04B8734683E339ED7C","aliaName":null,"transName":null,"alg":"icf3","markTypes":[]}]
     */

    var code: Int = 0
    var message: String? = null
    var data: List<DataBean>? = null

    class DataBean {
        /**
         * type : 0
         * title : 如果没有你
         * durationms : 286000
         * creator : [{"userId":8926,"userName":"莫文蔚"}]
         * playTime : 884150
         * coverUrl : http://p1.music.126.net/MAJQ1BwS3bSripKjUflPfg==/1375489051002253.jpg
         * vid : 5307751
         * aliaName : null
         * transName : null
         * alg : icf3
         * markTypes : null
         */

        var type: Int = 0
        var title: String? = null
        var durationms: Int = 0
        var playTime: Int = 0
        var coverUrl: String? = null
        var vid: String? = null
        var aliaName: Any? = null
        var transName: Any? = null
        var alg: String? = null
        var markTypes: Any? = null
        var creator: List<CreatorBean>? = null

        class CreatorBean {
            /**
             * userId : 8926
             * userName : 莫文蔚
             */

            var userId: Int = 0
            var userName: String? = null
        }
    }
}
