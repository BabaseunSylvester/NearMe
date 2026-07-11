package com.example.nearme.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Query("SELECT * FROM places")
    fun getAllPlaces(): Flow<List<PlaceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<PlaceEntity>)

    @Query("SELECT * FROM places WHERE favorite = 1")
    fun getFavoritePlaces(): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM places WHERE id = :id")
    suspend fun getPlaceById(id: String): PlaceEntity?

    @Query("SELECT * FROM places WHERE id IN (:ids)")
    fun getPlacesByIds(ids: List<String>): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM places WHERE id IN (:ids)")
    suspend fun getPlacesByIdsImmediate(ids: List<String>): List<PlaceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlace(place: PlaceEntity)
}
