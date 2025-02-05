package com.example.seekhoassignment.data.repository

import com.example.seekhoassignment.data.datasource.ApiService
import com.example.seekhoassignment.data.model.AnimeData
import com.example.seekhoassignment.data.model.AnimeDetailsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


interface AnimeRepository {
    suspend fun fetchAnime(): Flow<AnimeData>
    suspend fun fetchAnimeDetails(animeID: Int) :Flow<AnimeDetailsData>
}

class AnimeRepositoryImpl @Inject constructor(private val apiService: ApiService) : AnimeRepository {
    override suspend fun fetchAnime(): Flow<AnimeData> = flow  {
        val result= apiService.getAnime()
        emit(result)
    }

    override suspend fun fetchAnimeDetails(animeID : Int): Flow<AnimeDetailsData> = flow {
        val result = apiService.getAnimeDetails(animeID)
        emit(result)
    }
}