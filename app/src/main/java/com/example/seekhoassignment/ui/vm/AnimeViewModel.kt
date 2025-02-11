package com.example.seekhoassignment.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seekhoassignment.data.model.AnimeData
import com.example.seekhoassignment.data.model.AnimeDetailsData
import com.example.seekhoassignment.data.model.Data
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


    private val _animeListState = MutableStateFlow<List<Data>?>(emptyList())
    val animeListState: StateFlow<List<Data>?> = _animeListState

    private val _animeDetailsState = MutableStateFlow<AnimeDetailsData?>(null)
    val animeDetailsState: StateFlow<AnimeDetailsData?> = _animeDetailsState

    private val _filteredAnimeList = MutableStateFlow<List<Data>?>(emptyList())
    val filteredAnimeList: StateFlow<List<Data>?> = _filteredAnimeList

//    private val _searchQuery = MutableStateFlow("")
//    val searchQuery: StateFlow<String> = _searchQuery

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    init {
        fetchAnimeList()
    }

    // Function to fetch the list of anime
    fun fetchAnimeList() {
        viewModelScope.launch {

                repository.fetchAnime().collect { animeData ->
                    _animeListState.value = animeData.data
                    _filteredAnimeList.value = animeData.data
                }

        }
    }

    fun searchAnime(query: String) {
    //    _searchQuery.value = query
        _filteredAnimeList.value = (if (query.isEmpty()) {
            _animeListState.value
        } else {
            _animeListState.value?.filter { anime ->
                anime.title.contains(query, ignoreCase = true)
            }
        })!!
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
}