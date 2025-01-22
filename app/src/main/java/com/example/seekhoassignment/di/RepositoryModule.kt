package com.example.seekhoassignment.di

import com.example.seekhoassignment.data.datasource.ApiService
import com.example.seekhoassignment.data.repository.AnimeRepository
import com.example.seekhoassignment.data.repository.AnimeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAnimeRepository(
        animeRepositoryImpl: AnimeRepositoryImpl
    ): AnimeRepository
}