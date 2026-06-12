package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Match Model
data class CricketMatch(
    val id: String,
    val teamA: String,
    val teamB: String,
    val scoreA: String, // e.g., "342/6" or "182"
    val scoreB: String,
    val oversA: String, // e.g., "90.0"
    val oversB: String,
    val teamABatting: Boolean, // is team A batting currently?
    val matchStatus: String, // e.g., "Lancashire lead by 160 runs"
    val venue: String, // e.g., "Old Trafford"
    val type: String, // "County Championship", "Test Match", "T20 Blast", "One-Day Cup"
    val runRate: String,
    val partnership: String, // e.g., "68 (112 balls)"
    val winProbabilityA: Int, // percentage
    val winProbabilityB: Int,
    val isLive: Boolean,
    val batsman1: String,
    val batsman1Runs: Int,
    val batsman1Balls: Int,
    val batsman2: String,
    val batsman2Runs: Int,
    val batsman2Balls: Int,
    val bowler: String,
    val bowlerOvers: Double,
    val bowlerRuns: Int,
    val bowlerWickets: Int,
    val ballByBallCommentary: List<String> = emptyList()
)

// County Model
@Entity(tableName = "followed_counties")
data class County(
    @PrimaryKey val id: String,
    val name: String,
    val isSubscribedToAlerts: Boolean = false,
    val favoriteColor: String = "#C5A880"
)

// Player Profile
data class Player(
    val id: String,
    val name: String,
    val role: String, // e.g. "Right-hand bat", "Right-arm fast-medium"
    val matches: Int,
    val runs: Int,
    val battingAvg: Double,
    val highestScore: String,
    val strikeRate: Double,
    val wickets: Int,
    val bowlingAvg: Double,
    val bestBowling: String,
    val teamsRepresented: String,
    val isHistorical: Boolean,
    val era: String // e.g. "1970-1985" or "Modern"
)

// Newsroom Article
data class NewsArticle(
    val id: String,
    val title: String,
    val category: String, // International, County, Women's, Grassroots
    val teaser: String,
    val content: String,
    val timestamp: String,
    val author: String
)

// Historical Archive Item
data class HistoricalArchiveItem(
    val id: String,
    val title: String,
    val year: String,
    val description: String,
    val records: String
)

// Venues Model
data class Venue(
    val id: String,
    val name: String,
    val city: String,
    val history: String,
    val seatingCapacity: String,
    val records: String,
    val weather: String,
    val temp: String
)
