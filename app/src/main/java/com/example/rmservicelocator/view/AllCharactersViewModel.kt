package com.example.rmservicelocator.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmservicelocator.model.Lce
import com.example.rmservicelocator.retrofit.Character
import com.example.rmservicelocator.retrofit.RickAndMortyApi
import com.example.rmservicelocator.room.CharacterDao
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class AllCharactersViewModel(
    private val rickAndMortyApi: RickAndMortyApi,
    private val characterDao: CharacterDao
) : ViewModel() {

    private val refreshFlow = MutableSharedFlow<Unit>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val queryFlow = MutableStateFlow("")

    val rickAndMortyFlow: Flow<Lce<List<Character>>> = queryFlow.combine(
        refreshFlow
            .map {
                delay(5000)
                runCatching {
                    rickAndMortyApi.getCharacters(4).characterList
                }.onSuccess {
                    Lce.Success(it)
                }
                    .onFailure { Lce.Fail(it) }
            }) { query, result ->
        result.map {
            it.filter { it.name.contains(query, ignoreCase = true) }
        }.fold(onSuccess = {
            characterDao.insertAll(it)
            Lce.Success(it)
        }, onFailure = { Lce.Fail(it) })
    }
        .onStart {
            val storedList = characterDao.getAll()
            val state = if (storedList.isNotEmpty()) {
                Lce.Success(storedList)
            } else {
                Lce.Loading
            }
            emit(state)
        }
        .shareIn(
            viewModelScope,
            SharingStarted.Eagerly,
            1
        )

    fun onQueryChanged(query: String) {
        queryFlow.value = query
    }

    fun onRefreshed() {
        refreshFlow.tryEmit(Unit)
    }
}