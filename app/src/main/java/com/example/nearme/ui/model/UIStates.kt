package com.example.nearme.ui.model

import com.example.nearme.data.local.PlaceEntity
import com.example.nearme.data.remote.dto.PhotoDto
import com.example.nearme.data.remote.dto.PlaceDto

data class SearchUiState(
    val places: List<PlaceEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

data class FavoritesUiState(
    val favoritePlaces: List<PlaceEntity> = emptyList()
)

data class DetailUiState(
    val place: PlaceDto? = null,
    val photos: List<PhotoDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
