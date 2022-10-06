package com.example.rmservicelocator.service

import android.content.Context
import com.example.rmservicelocator.retrofit.RickAndMortyApi
import com.example.rmservicelocator.room.CharacterDao
import com.example.rmservicelocator.room.appDatabase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ServiceLocator {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .build()
        )
        .build()

    val apiRickAndMorty = retrofit.create<RickAndMortyApi>()

    fun provideCharacters(): RickAndMortyApi = apiRickAndMorty

    //DB
    private lateinit var contextAPP: Context

    private val characterDao by lazy{
        contextAPP.appDatabase.characterDao()
    }

    fun init(context: Context){
        this.contextAPP = context
    }

    fun provideDataSource(): CharacterDao = characterDao

}