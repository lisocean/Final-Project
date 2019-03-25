package com.lisocean.musicplayer.di

import com.lisocean.musicplayer.model.local.CpDatabase
import com.lisocean.musicplayer.model.remote.MusicService
import com.lisocean.musicplayer.model.remote.MvService
import com.lisocean.musicplayer.ui.localmusic.viewmodel.LocalMusicViewModel
import com.lisocean.musicplayer.helper.constval.Constants
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.model.local.AppDatabase
import com.lisocean.musicplayer.model.repository.*
import com.lisocean.musicplayer.ui.localmusic.viewmodel.MainMvsViewModel
import com.lisocean.musicplayer.ui.localmusic.viewmodel.RdViewModel
import com.lisocean.musicplayer.ui.musicplaying.viewmodel.MusicPlayingViewModel
import com.lisocean.musicplayer.ui.search.viewmodel.SearchViewModel
import com.lisocean.musicplayer.ui.videoplayer.viewmodel.VideoPlayerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule =  module{
    viewModel {(id: Int) ->LocalMusicViewModel(id,get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { (list: ArrayList<SongInfo>, position: Int) -> MusicPlayingViewModel(list, position, get())}
    viewModel { (mvid : Int) -> VideoPlayerViewModel(mvid, get())}
    viewModel { MainMvsViewModel(get()) }
    viewModel { RdViewModel(get()) }
}

val repoModule =  module{
    single { LocalMusicRepo(get(), get(), get()) }
    single { MusicSearchRepo(get()) }
    single { MusicPlayingRepo(get()) }
    single { MvSearchRepo(get("mvService")) }
    single { MainMvsRepo(get()) }
    single { RdSearchRepo(get()) }
}

val localModule =  module{

    single { CpDatabase.getInstance(androidApplication()) }
    single { AppDatabase.getInstance(androidApplication()) }

    single { get<AppDatabase>().songInfoDao() }
}

val remoteModule = module {

    single <Retrofit> {
        Retrofit.Builder()
            .baseUrl(Constants.HOST_API)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //get music player of search single instance
    single<MusicService> { get<Retrofit>().create(MusicService::class.java) }

    //get mv of search single instance
    single<MvService> (name = "mvService") { get<Retrofit>().create(MvService::class.java) }
}


val appModule = listOf(viewModelModule, remoteModule, localModule, repoModule)