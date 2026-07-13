package com.example.nearme.di

import android.content.Context
import androidx.room.Room
import com.example.nearme.data.local.AppDatabase
import com.example.nearme.data.local.PlaceDao
import com.example.nearme.data.remote.PlacesApi
import com.example.nearme.data.repository.PlaceRepository
import com.example.nearme.data.repository.PlaceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "nearme_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providePlaceDao(db: AppDatabase): PlaceDao = db.placeDao()

    @Provides
    @Singleton
    fun providePlacesApi(): PlacesApi {
        return Retrofit.Builder()
            .baseUrl(PlacesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlacesApi::class.java)
    }

    @Provides
    @Singleton
    fun providePlaceRepository(
        api: PlacesApi,
        dao: PlaceDao
    ): PlaceRepository {
        return PlaceRepositoryImpl(api, dao)
    }
}
