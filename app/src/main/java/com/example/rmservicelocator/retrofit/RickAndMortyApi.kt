package com.example.rmservicelocator.retrofit

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): ResultApi

    @GET("character/{id}")
    suspend fun getCharacterDetails(@Path("id") id: Int): Character
}