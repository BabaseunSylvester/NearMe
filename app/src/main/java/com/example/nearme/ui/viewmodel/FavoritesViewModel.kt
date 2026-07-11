package com.example.nearme.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nearme.data.local.PlaceEntity
import com.example.nearme.data.repository.PlaceRepository
import com.example.nearme.ui.model.FavoritesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: PlaceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesUiState())
    val state: StateFlow<FavoritesUiState> = _state.asStateFlow()

    init {
        getFavorites()
    }

    private fun getFavorites() {
        repository.getFavoritePlaces().onEach { favorites ->
            _state.update { it.copy(favoritePlaces = favorites) }
        }.launchIn(viewModelScope)
    }

    fun onFavoriteClick(place: PlaceEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(place)
        }
    }
}
