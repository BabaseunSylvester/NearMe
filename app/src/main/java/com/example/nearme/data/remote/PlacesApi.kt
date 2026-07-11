package com.example.nearme.data.remote

import com.example.nearme.BuildConfig
import com.example.nearme.data.remote.dto.PhotoDto
import com.example.nearme.data.remote.dto.PlaceDto
import com.example.nearme.data.remote.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PlacesApi {

    @GET("search")
    suspend fun searchPlaces(
        @Header("authorization") authorization: String = "Bearer $API_KEY",
        @Header("X-Places-Api-Version") version: String = "2025-06-17",
        @Header("accept") accept: String = "application/json",
        @Query("query") query: String? = null,
        @Query("ll") latLng: String? = null,
        @Query("radius") radius: Int? = null,
        @Query("limit") limit: Int? = null
    ): SearchResponse

    @GET("{fsq_place_id}")
    suspend fun getPlaceDetails(
        @Path("fsq_place_id") fsqId: String,
        @Header("authorization") authorization: String = "Bearer $API_KEY",
        @Header("X-Places-Api-Version") version: String = "2025-06-17",
        @Header("accept") accept: String = "application/json",
    ): PlaceDto

    @GET("{fsq_place_id}/photos")
    suspend fun getPlacePhotos(
        @Path("fsq_place_id") fsqId: String,
        @Header("authorization") authorization: String = "Bearer $API_KEY",
        @Header("X-Places-Api-Version") version: String = "2025-06-17",
        @Header("accept") accept: String = "application/json",
    ): List<PhotoDto>


    companion object {
        const val BASE_URL = BuildConfig.BASE_URL
        const val API_KEY = BuildConfig.API_SERVICE_KEY
    }
}
