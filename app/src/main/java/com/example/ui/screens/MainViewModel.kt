package com.example.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = CricketHouseDatabase.getDatabase(application)
    private val countyDao = db.countyDao()
    private val queryHistoryDao = db.queryHistoryDao()

    // Expose followed counties reactively
    val followedCounties: StateFlow<List<County>> = countyDao.getFollowedCounties()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Expose analytical queries history logs reactively
    val queryHistory: StateFlow<List<QueryHistory>> = queryHistoryDao.getQueryHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Flow binding for match telemetry updates
    private val _matchesState = MutableStateFlow<List<CricketMatch>>(emptyList())
    val matches: StateFlow<List<CricketMatch>> = _matchesState.asStateFlow()

    init {
        // Hydrate initial matches array
        _matchesState.value = Repository.matches.value

        // Activate real-time score tracker background coroutine
        viewModelScope.launch {
            Repository.simulateScoringFeed().collect { primaryMatch ->
                _matchesState.value = Repository.matches.value
            }
        }
    }

    fun toggleCountyTracking(countyName: String, isTracking: Boolean) {
        viewModelScope.launch {
            if (isTracking) {
                countyDao.followCounty(County(id = countyName, name = countyName, isSubscribedToAlerts = true))
            } else {
                countyDao.unfollowCounty(County(id = countyName, name = countyName, isSubscribedToAlerts = false))
            }
        }
    }

    fun recordQuery(queryText: String) {
        if (queryText.isBlank()) return
        viewModelScope.launch {
            queryHistoryDao.insertQuery(QueryHistory(queryText = queryText))
        }
    }

    fun deleteQueryHistoryLog() {
        viewModelScope.launch {
            queryHistoryDao.clearHistory()
        }
    }
}
