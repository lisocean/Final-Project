package com.lisocean.musicplayer.di

import com.lisocean.musicplayer.model.local.CpDatabase
import com.lisocean.musicplayer.model.remote.MusicService
import com.lisocean.musicplayer.model.remote.MvService
import com.lisocean.musicplayer.model.repository.LocalMusicRepo
import com.lisocean.musicplayer.ui.localmusic.viewmodel.LocalMusicViewModel
import com.lisocean.musicplayer.helper.Constants
import com.lisocean.musicplayer.model.local.AppDatabase
import com.lisocean.musicplayer.model.repository.MusicSearchRepo
import com.lisocean.musicplayer.ui.search.viewmodel.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule =  module{
    viewModel {LocalMusicViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}

val repoModule =  module{
    single { LocalMusicRepo(get()) }
    single { MusicSearchRepo(get()) }
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