package com.example.rmservicelocator.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmservicelocator.model.Lce
import com.example.rmservicelocator.retrofit.RickAndMortyApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class DetailViewModel(
    private val rickAndMortyApi: RickAndMortyApi,
    private val idCharacter: Int
) : ViewModel() {

    val detailFlow = flow {
        delay(2000)
        runCatching {
            rickAndMortyApi.getCharacterDetails(idCharacter)
        }.onSuccess { emit(Lce.Success(it)) }
            .onFailure { emit(Lce.Fail(it)) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, Lce.Loading)
}