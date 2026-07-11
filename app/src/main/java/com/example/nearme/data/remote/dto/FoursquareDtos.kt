package com.example.nearme.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val results: List<PlaceDto>
)

data class PlaceDto(
    @SerializedName("fsq_place_id") val fsqId: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val location: LocationDto? = null
)

data class LocationDto(
    val address: String? = null,
    val region: String? = null,
    val country: String? = null,
    @SerializedName("formatted_address") val formattedAddress: String? = null
)

data class PhotoDto(
    val id: String,
    @SerializedName("created_at") val createdAt: String? = null,
    val prefix: String,
    val suffix: String,
    val width: Int,
    val height: Int,
    val tip: TipDto
)

data class TipDto(
    val id: String? = null,
    val text: String? = null,
    val url: String? = null,
)
