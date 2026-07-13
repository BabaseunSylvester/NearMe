package com.example.nearme.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val rating: Double? = null,
    val category: String? = null,
    val favorite: Boolean = false
)
