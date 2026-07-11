package com.example.nearme.data.repository

import com.example.nearme.data.local.PlaceDao
import com.example.nearme.data.local.PlaceEntity
import com.example.nearme.data.remote.PlacesApi
import com.example.nearme.data.remote.dto.PhotoDto
import com.example.nearme.data.remote.dto.PlaceDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface PlaceRepository {
    fun getPlaces(query: String?, latLng: String?): Flow<Resource<List<PlaceEntity>>>
    suspend fun getPlaceDetails(fsqId: String): Resource<PlaceDto>
    suspend fun getPlacePhotos(fsqId: String): Resource<List<PhotoDto>>
    fun getFavoritePlaces(): Flow<List<PlaceEntity>>
    suspend fun toggleFavorite(place: PlaceEntity)
}

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}

class PlaceRepositoryImpl @Inject constructor(
    private val api: PlacesApi,
    private val dao: PlaceDao
) : PlaceRepository {

    override fun getPlaces(query: String?, latLng: String?): Flow<Resource<List<PlaceEntity>>> = flow {
        emit(Resource.Loading())

        try {
            val remoteResponse = api.searchPlaces(query = query, latLng = latLng)
            val remotePlaces = remoteResponse.results.map { it.toPlaceEntity() }

            if (remotePlaces.isEmpty()) {
                emit(Resource.Error("No places found for \"${query ?: ""}\""))
            } else {
                val remoteIds = remotePlaces.map { it.id }
                val localPlaces = dao.getPlacesByIdsImmediate(remoteIds)
                val localMap = localPlaces.associateBy { it.id }

                val updatedPlaces = remotePlaces.map { remotePlace ->
                    localMap[remotePlace.id]?.let { local ->
                        remotePlace.copy(favorite = local.favorite)
                    } ?: remotePlace
                }
                dao.insertPlaces(updatedPlaces)

                dao.getPlacesByIds(remoteIds).collect { places ->
                    emit(Resource.Success(places))
                }
            }
        } catch (e: IOException) {
            emit(Resource.Error("Network connection error. Please check your internet."))
        } catch (e: HttpException) {
            val message = when (e.code()) {
                401 -> "Invalid API Key."
                429 -> "Rate limit exceeded. Try again later."
                else -> "Server error: ${e.code()}"
            }
            emit(Resource.Error(message))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred: ${e.localizedMessage}"))
        }
    }

    override suspend fun getPlaceDetails(fsqId: String): Resource<PlaceDto> {
        return try {
            val response = api.getPlaceDetails(fsqId)
            Resource.Success(response)
        } catch (e: IOException) {
            Resource.Error("Network error. Could not load details.")
        } catch (e: HttpException) {
            Resource.Error("Server error (${e.code()}).")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun getPlacePhotos(fsqId: String): Resource<List<PhotoDto>> {
        return try {
            val response = api.getPlacePhotos(fsqId)
            Resource.Success(response)
        } catch (e: IOException) {
            Resource.Error("Network error. Could not load photos.")
        } catch (e: HttpException) {
            Resource.Error("Server error (${e.code()}).")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override fun getFavoritePlaces(): Flow<List<PlaceEntity>> {
        return dao.getFavoritePlaces()
    }

    override suspend fun toggleFavorite(place: PlaceEntity) {
        dao.updatePlace(place.copy(favorite = !place.favorite))
    }

    private fun PlaceDto.toPlaceEntity(): PlaceEntity {
        return PlaceEntity(
            id = fsqId,
            name = name,
            address = location?.formattedAddress ?: location?.address ?: "No address"
        )
    }
}
