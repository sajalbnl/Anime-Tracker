package com.example.seekhoassignment.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seekhoassignment.data.model.AnimeData
import com.example.seekhoassignment.data.model.AnimeDetailsData
import com.example.seekhoassignment.data.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {


    private val _animeListState = MutableStateFlow<AnimeData?>(null)
    val animeListState: StateFlow<AnimeData?> = _animeListState

    private val _animeDetailsState = MutableStateFlow<AnimeDetailsData?>(null)
    val animeDetailsState: StateFlow<AnimeDetailsData?> = _animeDetailsState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    // Function to fetch the list of anime
    fun fetchAnimeList() {
        viewModelScope.launch {
            try {
                repository.fetchAnime().collect { animeData ->
                    _animeListState.value = animeData
                }
            } catch (e: Exception) {
                _errorState.value = e.message
            }
        }
    }

    // Function to fetch anime details by ID
    fun fetchAnimeDetails(animeID: Int) {
        viewModelScope.launch {
            try {
                repository.fetchAnimeDetails(animeID).collect { animeDetails ->
                    _animeDetailsState.value = animeDetails
                }
            } catch (e: Exception) {
                _errorState.value = e.message
            }
        }
    }

    // Clear the error state after handling
    fun clearErrorState() {
        _errorState.value = null
    }
}