package com.example.nearme.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nearme.data.repository.PlaceRepository
import com.example.nearme.data.repository.Resource
import com.example.nearme.ui.model.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: PlaceRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailUiState())
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    init {
        savedStateHandle.get<String>("fsq_id")?.let { fsqId ->
            getPlaceDetails(fsqId)
        }
    }

    fun retry() {
        savedStateHandle.get<String>("fsq_id")?.let { getPlaceDetails(it) }
    }

    private fun getPlaceDetails(fsqId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val detailsDeferred = async { repository.getPlaceDetails(fsqId) }
            val photosDeferred = async { repository.getPlacePhotos(fsqId) }

            val detailsResult = detailsDeferred.await()
            val photosResult = photosDeferred.await()

            if (detailsResult is Resource.Success) {
                _state.update { it.copy(
                    place = detailsResult.data,
                    photos = photosResult.data ?: emptyList(),
                    isLoading = false,
                    error = null
                ) }
            } else if (detailsResult is Resource.Error) {
                _state.update { it.copy(
                    error = detailsResult.message,
                    isLoading = false
                ) }
            }
        }
    }
}
