package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HistoricalArchiveItem
import com.example.data.Player
import com.example.data.Repository
import com.example.data.Venue
import com.example.ui.theme.*

@Composable
fun DataCentreScreen() {
    var subTab by remember { mutableStateOf("Terminal") } // "Terminal", "Archive", "Grounds", "Grassroots"
    var playerSearchQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Navigation header of Data Centre ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Brass.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                    .background(NavyDark)
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "THE NATIONAL CRICKET DATA ARCHIVE",
                        color = Brass,
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Cricket Data Centre",
                        color = Cream,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }
            }
        }

        // --- Sub-Tab Selection Ribbon ---
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Slate, RoundedCornerShape(4.dp))
                    .background(NavyDark)
                    .padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val subTabs = listOf("Terminal", "Archive", "Grounds", "Grassroots")
                items(subTabs) { tab ->
                    val isSel = subTab == tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isSel) Burgundy else Color.Transparent)
                            .clickable { subTab = tab }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = tab.uppercase(),
                            color = if (isSel) Cream else Cream.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // --- Render Selected Sub-Tab ---
        when (subTab) {
            "Terminal" -> {
                // Bloomberg terminal statistics lookup
                item {
                    TerminalSearchBlock(
                        query = playerSearchQuery,
                        onQueryChange = { playerSearchQuery = it }
                    )
                }

                val filteredPlayers = Repository.playerDatabase.filter {
                    it.name.contains(playerSearchQuery, ignoreCase = true) ||
                            it.teamsRepresented.contains(playerSearchQuery, ignoreCase = true)
                }

                if (filteredPlayers.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Slate)
                                .padding(16.dp)
                        ) {
                            Text("No query indexes on record. Search e.g. Anderson or Lara.", color = CreamMuted, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                } else {
                    items(filteredPlayers) { player ->
                        TerminalPlayerIndexRow(player = player)
                    }
                }
            }
            "Archive" -> {
                // Historical Archives / Wisden ledger records
                item {
                    Text(
                        text = "HISTORIC COUNTY & TEST ARCHIVE",
                        color = Brass,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                items(Repository.historicalArchive) { archiveItem ->
                    ArchiveLedgerCard(item = archiveItem)
                }
            }
            "Grounds" -> {
                // Venue Details & Blueprints
                item {
                    Text(
                        text = "INTERACTIVE STADIUM PROFILE REGISTER",
                        color = Brass,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                items(Repository.groundDatabase) { venue ->
                    VenueProfileCard(venue = venue)
                }
            }
            "Grassroots" -> {
                // Club cricket rankings and verified pages
                item {
                    Text(
                        text = "CLUB & GRASSROOTS VERIFIED DIRECTORY",
                        color = Brass,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                item {
                    GrassrootsOverviewDashboard()
                }
            }
        }
    }
}

@Composable
fun TerminalSearchBlock(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Slate, RoundedCornerShape(4.dp))
            .background(NavyDark)
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CRICINTEL INDEX SEARCH",
                    color = Brass,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(ForestGreen)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                    Text("ONLINE", color = Cream, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search by player name or county (e.g. Surrey, Jack Hobbs)...", color = CreamMuted.copy(alpha = 0.5f), fontSize = 12.sp) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Slate,
                    unfocusedContainerColor = Slate,
                    focusedTextColor = Cream,
                    unfocusedTextColor = Cream,
                    cursorColor = Brass,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Brass) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("terminal_stats_search"),
                shape = RoundedCornerShape(4.dp)
            )
        }
    }
}

@Composable
fun TerminalPlayerIndexRow(player: Player) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = player.name.uppercase(),
                        color = Cream,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = player.teamsRepresented,
                        color = Cream.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (player.isHistorical) Burgundy else ForestGreen)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (player.isHistorical) "ERA REGISTER: " + player.era else "MODERN ACTIVE",
                        color = Cream,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Cream.copy(alpha = 0.05f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Stats grid layout (like Bloomberg Terminal)
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("MAT", color = Cream.copy(alpha = 0.4f), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    Text("${player.matches}", color = Cream, fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
                Column(modifier = Modifier.weight(1.2f)) {
                    Text("RUNS", color = Cream.copy(alpha = 0.4f), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    Text("${player.runs}", color = Cream, fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
                Column(modifier = Modifier.weight(1.2f)) {
                    Text("B-AVERAGE", color = Cream.copy(alpha = 0.4f), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    Text("${player.battingAvg}", color = Brass, fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("H-SCORE", color = Cream.copy(alpha = 0.4f), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    Text(player.highestScore, color = Cream, fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("WICKETS", color = Cream.copy(alpha = 0.4f), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    Text("${player.wickets}", color = AlertGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
                Column(modifier = Modifier.weight(1.2f)) {
                    Text("W-AVERAGE", color = Cream.copy(alpha = 0.4f), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    Text("${player.bowlingAvg}", color = Cream, fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}

@Composable
fun ArchiveLedgerCard(item: HistoricalArchiveItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    color = Cream,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Serif
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(Burgundy)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.year,
                        color = Cream,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.description,
                color = Cream.copy(alpha = 0.8f),
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Cream.copy(alpha = 0.05f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "RECORD METRICS: ",
                    color = Brass,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = item.records,
                    color = AlertGreen,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
fun VenueProfileCard(venue: Venue) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = venue.name,
                        color = Cream,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Location: " + venue.city,
                        color = Cream.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(NavyDark)
                        .border(1.dp, Brass, RoundedCornerShape(3.dp))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "LIVE METEOROLOGY", color = Brass, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                        Text(text = "${venue.weather} • ${venue.temp}", color = AlertGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = venue.history,
                color = Cream.copy(alpha = 0.8f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Cream.copy(alpha = 0.05f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("SEATING CAPACITY", color = Cream.copy(alpha = 0.4f), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    Text(venue.seatingCapacity, color = Cream, fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("GROUND INDEX RECORD", color = Cream.copy(alpha = 0.4f), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    Text(
                        text = venue.records.split("\n").firstOrNull()?.replace("Highest Score: ", "") ?: "",
                        color = Brass,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
fun GrassrootsOverviewDashboard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "COMMUNITY GRASSROOTS HUB",
                    color = Brass,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(ForestGreen)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("452 CLUBS ENROLLED", color = Cream, fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Connecting the local village greens directly into the national historical record vaults. Register your local team, stream fixtures, track rosters, and publish leagues.",
                color = CreamMuted,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = Cream.copy(alpha = 0.05f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "VERIFIED CLUB LEAGUES STANDINGS",
                color = Cream,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Dynamic ranking tables mock
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Navy)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("RANK", color = Cream.copy(alpha = 0.4f), fontSize = 9.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f))
                Text("LOCAL CLUB CC", color = Cream.copy(alpha = 0.4f), fontSize = 9.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(3f))
                Text("P", color = Cream.copy(alpha = 0.4f), fontSize = 9.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("PTS", color = Cream.copy(alpha = 0.4f), fontSize = 9.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1.2f), textAlign = TextAlign.End)
            }

            GrassrootsRankingRow(rank = "1", name = "Clifton CC (Lancashire)", played = "12", pts = "220")
            GrassrootsRankingRow(rank = "2", name = "Ramsbottom CC", played = "12", pts = "204")
            GrassrootsRankingRow(rank = "3", name = "Wimbledon CC", played = "11", pts = "194")
            GrassrootsRankingRow(rank = "4", name = "Guildford CC", played = "12", pts = "182")
        }
    }
}

@Composable
fun GrassrootsRankingRow(rank: String, name: String, played: String, pts: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = rank, color = Brass, fontWeight = FontWeight.Bold, fontSize = 11.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f))
        Text(text = name, color = Cream, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(3f))
        Text(text = played, color = CreamMuted, fontSize = 11.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
        Text(text = pts, color = AlertGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1.2f), textAlign = TextAlign.End)
    }
}
