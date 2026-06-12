package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CricketMatch
import com.example.ui.screens.*
import com.example.ui.theme.CricketHouseTheme
import com.example.ui.theme.Navy
import com.example.ui.theme.NavyDark
import com.example.ui.theme.Brass
import com.example.ui.theme.Burgundy
import com.example.ui.theme.AlertRed
import com.example.ui.theme.AlertGreen
import com.example.ui.theme.Slate
import com.example.ui.theme.Cream
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CricketHouseTheme {
                val matches by viewModel.matches.collectAsState()
                val followedCounties by viewModel.followedCounties.collectAsState()
                val queryHistory by viewModel.queryHistory.collectAsState()

                // Bottom Navigation active tab control
                var activeTab by remember { mutableStateOf("Observatory") } // Observatory, Matches, Counties, Broadcast, Analyst, Ledger
                var selectedMatchDetail by remember { mutableStateOf<CricketMatch?>(null) }

                // Live Notification Ticker alert overlay bar
                var currentNotificationAlert by remember { mutableStateOf<String?>(null) }
                val coroutineScope = rememberCoroutineScope()

                // Listen to matches updates to fire wicket banners
                LaunchedEffect(matches) {
                    // Search for wicket alerts in commentary
                    val liveMatch = matches.firstOrNull()
                    if (liveMatch != null) {
                        val lastComment = liveMatch.ballByBallCommentary.firstOrNull()
                        if (lastComment != null && (lastComment.contains("OUT") || lastComment.contains("WICKET"))) {
                            currentNotificationAlert = "🚨 WICKET ALERT @ Old Trafford: " + lastComment.substringAfter(" - ")
                            coroutineScope.launch {
                                delay(7000)
                                currentNotificationAlert = null
                            }
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        InstitutionalHeader()
                    },
                    bottomBar = {
                        CricketHouseNavigationBar(
                            activeTab = activeTab,
                            onTabSelected = { tab ->
                                activeTab = tab
                                if (tab == "Matches" && selectedMatchDetail == null) {
                                    // Default back to first match
                                    selectedMatchDetail = matches.firstOrNull()
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Navy)
                            .padding(innerPadding)
                    ) {
                        // Multi-Screen Display Router
                        when (activeTab) {
                            "Observatory" -> {
                                HomeScreen(
                                    matches = matches,
                                    followedCounties = followedCounties,
                                    onFollowCounty = { countyName, isFollow ->
                                        viewModel.toggleCountyTracking(countyName, isFollow)
                                    },
                                    onSelectMatch = { m ->
                                        selectedMatchDetail = m
                                        activeTab = "Matches"
                                    },
                                    onSelectStream = { feedName ->
                                        activeTab = "Broadcast"
                                    },
                                    onNavigateToNav = { navTab ->
                                        activeTab = navTab
                                    }
                                )
                            }
                            "Matches" -> {
                                val currentActiveMatch = selectedMatchDetail ?: matches.firstOrNull()
                                if (currentActiveMatch != null) {
                                    LiveScoreScreen(
                                        match = currentActiveMatch,
                                        onBackToList = {
                                            activeTab = "Observatory"
                                        }
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("No active scores indexes available.", color = Cream, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }
                            "Counties" -> {
                                CountyDashboardScreen(
                                    followedCounties = followedCounties,
                                    onFollowCounty = { name, isFollow ->
                                        viewModel.toggleCountyTracking(name, isFollow)
                                    }
                                )
                            }
                            "Broadcast" -> {
                                LiveStreamScreen()
                            }
                            "Analyst" -> {
                                AiAnalystScreen(
                                    historyList = queryHistory,
                                    onSaveQuery = { q ->
                                        viewModel.recordQuery(q)
                                    },
                                    onClearHistory = {
                                        viewModel.deleteQueryHistoryLog()
                                    }
                                )
                            }
                            "Ledger" -> {
                                DataCentreScreen()
                            }
                        }

                        // Sliding Wicket Alert Ticker Banner
                        AnimatedVisibility(
                            visible = currentNotificationAlert != null,
                            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(AlertRed)
                                    .border(1.5.dp, Brass, RoundedCornerShape(6.dp))
                                    .clickable { currentNotificationAlert = null }
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = currentNotificationAlert ?: "",
                                        color = Cream,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.weight(4f)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close ticket",
                                        tint = Cream,
                                        modifier = Modifier
                                            .size(16.dp)
                                            .weight(1.0f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InstitutionalHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Navy)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "NATIONAL OBSERVATORY",
                    color = Cream.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "CRICKET",
                        color = Cream,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "HOUSE",
                        color = Brass,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                // Showing formatted UTC time
                val timeString = remember {
                    val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
                    val hours = String.format("%02d", cal.get(java.util.Calendar.HOUR_OF_DAY))
                    val minutes = String.format("%02d", cal.get(java.util.Calendar.MINUTE))
                    "$hours:$minutes UTC"
                }

                Text(
                    text = timeString,
                    color = Cream.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "LIVE COMMAND",
                    color = Brass,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
            }
        }
        Divider(
            color = Brass.copy(alpha = 0.2f),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun CricketHouseNavigationBar(
    activeTab: String,
    onTabSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(Navy)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Divider(color = Brass.copy(alpha = 0.3f), thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val menuItems = listOf(
                Pair("Observatory", "H"),
                Pair("Matches", "S"),
                Pair("Analyst", "A"),
                Pair("Counties", "C"),
                Pair("Broadcast", "B"),
                Pair("Ledger", "L")
            )

            menuItems.forEach { item ->
                val isSelected = activeTab == item.first
                val label = when (item.first) {
                    "Observatory" -> "Home"
                    "Matches" -> "Scores"
                    "Analyst" -> "Analysis"
                    "Counties" -> "Counties"
                    "Broadcast" -> "Media"
                    "Ledger" -> "Ledger"
                    else -> item.first
                }
                val borderCol = if (isSelected) Brass else Cream.copy(alpha = 0.4f)
                val textCol = if (isSelected) Cream else Cream.copy(alpha = 0.4f)

                Column(
                    modifier = Modifier
                        .clickable { onTabSelected(item.first) }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .testTag("nav_btn_${item.first.lowercase()}"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .border(1.dp, borderCol, RoundedCornerShape(2.dp))
                            .background(if (isSelected) Burgundy.copy(alpha = 0.15f) else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.second,
                            color = borderCol,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = if (item.second == "S") FontFamily.Serif else FontFamily.Monospace,
                            fontStyle = if (item.second == "S") androidx.compose.ui.text.font.FontStyle.Italic else androidx.compose.ui.text.font.FontStyle.Normal
                        )
                    }
                    Text(
                        text = label.uppercase(),
                        color = textCol,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.2).sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
