package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.County
import com.example.data.Repository
import com.example.ui.theme.*

@Composable
fun CountyDashboardScreen(
    followedCounties: List<County>,
    onFollowCounty: (String, Boolean) -> Unit
) {
    var selectedCounty by remember { mutableStateOf("Lancashire") }
    val isCurrentlyFollowed = followedCounties.any { it.id == selectedCounty }

    // Alert toggles
    var wicketAlerts by remember { mutableStateOf(true) }
    var matchReminders by remember { mutableStateOf(true) }
    var transferNews by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- County Selector Row ---
        item {
            Column {
                Text(
                    text = "SELECT COUNTY DOSSIER",
                    color = Brass,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Slate, RoundedCornerShape(4.dp))
                        .background(NavyDark)
                        .padding(12.dp)
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                text = "Viewing Channel:",
                                color = Cream.copy(alpha = 0.5f),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = selectedCounty.uppercase(),
                                color = Cream,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = Cream.copy(alpha = 0.05f), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Scrollable grid of counties
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val majorCounties = listOf("Lancashire", "Yorkshire", "Surrey", "Somerset", "Warwickshire")
                            majorCounties.forEach { cName ->
                                val active = selectedCounty == cName
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(if (active) Burgundy else Slate)
                                        .clickable { selectedCounty = cName }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cName.take(5),
                                        color = if (active) Cream else CreamMuted,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- Core County Hero Details ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Brass, RoundedCornerShape(8.dp))
                    .background(NavyDark)
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = selectedCounty.uppercase() + " CCC",
                                color = Cream,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = if (selectedCounty == "Lancashire") "Nickname: Red Rose • Est. 1864" else "Est. 1890",
                                color = Cream.copy(alpha = 0.5f),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Button(
                            onClick = { onFollowCounty(selectedCounty, !isCurrentlyFollowed) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCurrentlyFollowed) ForestGreen else Slate
                            ),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.testTag("follow_dashboard_button")
                        ) {
                            Icon(
                                imageVector = if (isCurrentlyFollowed) Icons.Default.Star else Icons.Default.Star,
                                contentDescription = null,
                                tint = if (isCurrentlyFollowed) Brass else Cream.copy(alpha = 0.3f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isCurrentlyFollowed) "TRACKED" else "TRACK COUNTY",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = Brass.copy(alpha = 0.3f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (selectedCounty == "Lancashire") {
                            "Defending home turf with railway schedule precision. Squad led by Keaton Jennings. Primary testing center: Emirates Old Trafford."
                        } else {
                            "Competing at high performance levels across County Championship and T20 Blast tournaments."
                        },
                        color = Cream.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // --- Alert Subscription Panel ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Slate)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.NotificationsActive, null, tint = Brass, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "HIGH-SPEED TELEMETRY ALERTS",
                            color = Brass,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Alert check 1
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Realtime Wicket Alerts", color = Cream, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Audible vibration when wickets fall live", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp)
                        }
                        Switch(
                            checked = wicketAlerts,
                            onCheckedChange = { wicketAlerts = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Brass, checkedTrackColor = ForestGreen),
                            modifier = Modifier.testTag("wicket_alerts_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Alert check 2
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Match Start Reminders", color = Cream, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Timetable alert 30 minutes before toss", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp)
                        }
                        Switch(
                            checked = matchReminders,
                            onCheckedChange = { matchReminders = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Brass, checkedTrackColor = ForestGreen),
                            modifier = Modifier.testTag("match_start_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Alert check 3
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Transfer / Squad News", color = Cream, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Receive official board bulletins instantly", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp)
                        }
                        Switch(
                            checked = transferNews,
                            onCheckedChange = { transferNews = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Brass, checkedTrackColor = ForestGreen),
                            modifier = Modifier.testTag("transfer_switch")
                        )
                    }
                }
            }
        }

        // --- Fixture Timetable ---
        item {
            Column {
                Text(
                    text = "UPCOMING FIXTURES",
                    color = Cream,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                FixtureItem(
                    opponent = "Surrey CCC",
                    date = "June 18-21, 2026",
                    comp = "County Championship",
                    loc = "Emirates Old Trafford"
                )
                Spacer(modifier = Modifier.height(8.dp))
                FixtureItem(
                    opponent = "Yorkshire CCC",
                    date = "June 25, 2026",
                    comp = "T20 Blast",
                    loc = "Headingley Stadium"
                )
            }
        }

        // --- Squad Roster ---
        item {
            Column {
                Text(
                    text = "ACTIVE COUNTY REGISTER",
                    color = Cream,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                // Render squad players matching selected county
                val roster = Repository.playerDatabase.filter { it.teamsRepresented.contains(selectedCounty, ignoreCase = true) }
                if (roster.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Slate)
                            .padding(12.dp)
                    ) {
                        Text("Searching archive vaults...", color = CreamMuted, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    }
                } else {
                    roster.forEach { player ->
                        RosterPlayerItem(player = player)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FixtureItem(opponent: String, date: String, comp: String, loc: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Slate, RoundedCornerShape(4.dp))
            .background(NavyDark)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = opponent, color = Cream, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = "$date • $loc", color = Cream.copy(alpha = 0.5f), fontSize = 11.sp)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(Burgundy)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = comp, color = Cream, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RosterPlayerItem(player: com.example.data.Player) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = player.name, color = Cream, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = player.role, color = Cream.copy(alpha = 0.5f), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Avg: ${player.battingAvg}", color = Brass, fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                Text(text = "Matches: ${player.matches}", color = Cream.copy(alpha = 0.4f), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
        }
    }
}
