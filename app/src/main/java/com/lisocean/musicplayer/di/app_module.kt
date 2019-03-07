package com.lisocean.musicplayer.di

import com.lisocean.musicplayer.model.remote.MusicService
import com.lisocean.musicplayer.model.remote.MvService
import com.lisocean.musicplayer.util.Constants
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule =  module{}

val repoModule =  module{}

val localModule =  module{
//    single { AppDatabase.getInstance(androidApplication()) }
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