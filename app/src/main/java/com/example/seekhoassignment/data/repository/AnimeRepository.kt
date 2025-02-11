package com.example.seekhoassignment.data.repository

import com.example.seekhoassignment.data.datasource.ApiService
import com.example.seekhoassignment.data.model.AnimeData
import com.example.seekhoassignment.data.model.AnimeDetailsData
import com.example.seekhoassignment.utils.network.ApiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


interface AnimeRepository {
    suspend fun fetchAnime(): Flow<ApiState<AnimeData>>
    suspend fun fetchAnimeDetails(animeID: Int) :Flow<ApiState<AnimeDetailsData>>
}

class AnimeRepositoryImpl @Inject constructor(private val apiService: ApiService) : AnimeRepository {
    override suspend fun fetchAnime(): Flow<ApiState<AnimeData>> = flow  {
        val result= apiService.getAnime()
        emit(ApiState.Success(result))
    }

    override suspend fun fetchAnimeDetails(animeID : Int): Flow<ApiState<AnimeDetailsData>> = flow {
        val result = apiService.getAnimeDetails(animeID)
        emit(ApiState.Success(result))
    }
}