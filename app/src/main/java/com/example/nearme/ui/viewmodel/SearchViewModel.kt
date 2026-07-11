package com.example.nearme.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nearme.data.local.PlaceEntity
import com.example.nearme.data.repository.PlaceRepository
import com.example.nearme.data.repository.Resource
import com.example.nearme.ui.model.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: PlaceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    private var searchJob: Job? = null
    private var searchFlowJob: Job? = null

    fun onQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query, error = null) }
        searchJob?.cancel()
        if (query.isBlank()) {
            _state.update { it.copy(places = emptyList(), isLoading = false) }
            return
        }
        searchJob = viewModelScope.launch {
            delay(500L)
            searchPlaces(query)
        }
    }

    fun searchPlaces(query: String, latLng: String? = null) {
        searchFlowJob?.cancel()
        searchFlowJob = repository.getPlaces(query, latLng).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update { it.copy(
                        places = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    ) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(
                        error = result.message,
                        isLoading = false
                    ) }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onFavoriteClick(place: PlaceEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(place)
        }
    }
}
