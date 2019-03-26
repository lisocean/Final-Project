# Music Player（Unfinished）
This is a music player using the MVVM(view--viewmodel--model), Network Api comes from [NeteaseCloudMusicApi](https://binaryify.github.io/NeteaseCloudMusicApi/#/?id=neteasecloudmusicapi).

## Technology stack And Open source framework

* MVVM
* Kotlin
* DataBinding
* Rxjava2
* Retrofit2
* Room
* Koin
* ViewModel
* Gson
* Dachshund-Tab-Layout
* EventBus
* Glide
* Flexbox
* GSYVideoPlayer

## Reference : Ui effect
* [PaoNet](https://github.com/ditclear/PaoNet) -> RecyclerView's Base Activity
* [KotlinMvp](https://github.com/git-xuhao/KotlinMvp) -> Search Activity Transition Animation


## Local Load Strategy
Content Providers, App DataBase(playTime >= 60s)to get local music. 
Synchronize of database is search online resources to adapter local Content Providers' Music and add it to local App database.

<img src="display/demo1.jpg" alt="screenshot" title="screenshot" width="216" height="384"> <img 
src="display/demo2.jpg" alt="screenshot" title="screenshot" width="216"  height="384">
<img src="display/demo3.jpg" alt="screenshot" title="screenshot" width="216" height="384"> <img 
src="display/demo4.jpg" alt="screenshot" title="screenshot" width="216"  height="384">
<img src="display/demo5.jpg" alt="screenshot" title="screenshot" width="216"  height="384">
<img src="display/demo6.jpg" alt="screenshot" title="screenshot" width="216"  height="384"> <img 
src="display/demo7.jpg" alt="screenshot" title="screenshot" width="216"  height="384">
<img src="display/demo8.jpg" alt="screenshot" title="screenshot" width="216"  height="384"> <img 
src="display/demo9.jpg" alt="screenshot" title="screenshot" width="216"  height="384">

