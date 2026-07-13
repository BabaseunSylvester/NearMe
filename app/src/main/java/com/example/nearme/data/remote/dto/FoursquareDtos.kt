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
    val location: LocationDto? = null,
    val categories: List<CategoryDto>? = null,
    val photos: List<PhotoDto>? = null,
    val description: String? = null,
    val email: String? = null,
    val attributes: AttributesDto? = null,
    @SerializedName("hours_popular") val hours: List<HoursDto>? = null,
    val link: String? = null,
    val popularity: Double? = null,
    val price: Int? = null,
    val rating: Double? = null,
    @SerializedName("social_media") val socialMedia: SocialMediaDto? = null,
    val tel: String? = null,
    val tastes: List<String>? = null,
    val website: String? = null
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

data class CategoryDto(
    @SerializedName("fsq_category_id") val id: String? = null,
    val name: String? = null,
    @SerializedName("short_name") val shortName: String? = null
)

data class AttributesDto(
    val restroom: Boolean? = null,
    @SerializedName("outdoor_seating") val outdoorSeating: Boolean? = null,
    val atm: Boolean? = null,
    @SerializedName("has_parking") val hasParking: Boolean? = null,
    val wifi: String? = null,
    val delivery: Boolean? = null,
    val reservations: Boolean? = null,
    @SerializedName("takes_credit_card") val takesCreditCard: Boolean? = null
)

data class HoursDto(
    val closes: String? = null,
    val day: Int? = null,
    val open: String? = null,
)

data class SocialMediaDto(
    @SerializedName("facebook_id") val facebookId: String? = null,
    val instagram: String? = null,
    val twitter: String? = null,
)
