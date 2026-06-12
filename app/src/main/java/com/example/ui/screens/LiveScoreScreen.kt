package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CricketMatch
import com.example.ui.theme.*

@Composable
fun LiveScoreScreen(
    match: CricketMatch,
    onBackToList: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Scorecard") } // "Scorecard", "Analysis", "Commentary"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Back to list navigation ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "← MATCH CENTRE INDEX",
                    color = Brass,
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .testTag("back_to_index")
                        .clickable { onBackToList() }
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Default.Star, "favorite", tint = Brass, modifier = Modifier.size(18.dp))
                    Icon(Icons.Default.Share, "share", tint = Cream, modifier = Modifier.size(18.dp))
                }
            }
        }

        // --- Live Board Score Callout ---
        item {
            ScoreboardHeaderBlock(match = match)
        }

        // --- Tab Selection Matrix ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Slate, RoundedCornerShape(4.dp))
                    .background(NavyDark)
                    .padding(4.dp)
            ) {
                val tabs = listOf("Scorecard", "Analysis", "Commentary")
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (isSelected) Burgundy else Color.Transparent)
                            .clickable { selectedTab = tab }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab.uppercase(),
                            color = if (isSelected) Cream else Cream.copy(alpha = 0.5f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // --- Tab Contents ---
        when (selectedTab) {
            "Scorecard" -> {
                item {
                    BattingCardBlock(match = match)
                }
                item {
                    BowlingCardBlock(match = match)
                }
                item {
                    PartnershipAndMatchSituationBlock(match = match)
                }
            }
            "Analysis" -> {
                item {
                    AnalyticsWagonWheelAndPitchMap()
                }
                item {
                    WinProbabilitySection(match = match)
                }
            }
            "Commentary" -> {
                item {
                    Text(
                        text = "LIVE BALL-BY-BALL COMMENTARY",
                        color = Brass,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                if (match.ballByBallCommentary.isEmpty()) {
                    item {
                        Text(
                            text = "Waiting for match play to resume...",
                            color = CreamMuted,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    items(match.ballByBallCommentary) { commentaryLine ->
                        CommentaryRow(line = commentaryLine)
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreboardHeaderBlock(match: CricketMatch) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Cream),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Burgundy left border stripe
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(6.dp)
                    .background(color = Burgundy)
            )

            Column(modifier = Modifier.padding(16.dp).weight(1f)) {
                // Venue / Match type info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(Navy)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${match.type.uppercase()} • ${match.venue.uppercase()}",
                            color = Cream,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (match.isLive) ForestGreen else Navy.copy(alpha = 0.5f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (match.isLive) "LIVE TELEMETRY" else "COMPLETED",
                            color = Cream,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scores Display
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = match.teamA.uppercase(),
                            color = Navy,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "OVERS: ${match.oversA}  (RR: ${match.runRate})",
                            color = Navy.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = match.scoreA,
                        color = Navy,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = match.teamB.uppercase(),
                            color = Navy.copy(alpha = 0.8f),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                        if (match.oversB.isNotEmpty()) {
                            Text(
                                text = "OVERS: ${match.oversB}",
                                color = Navy.copy(alpha = 0.5f),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = match.scoreB,
                        color = Navy.copy(alpha = 0.8f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Divider(color = Navy.copy(alpha = 0.15f), thickness = 1.dp)

                Spacer(modifier = Modifier.height(10.dp))

                // Status message
                Text(
                    text = match.matchStatus.uppercase(),
                    color = Burgundy,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun BattingCardBlock(match: CricketMatch) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "ACTIVE BATMEN",
                color = Brass,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Header line
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("BATTER", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(3f))
                Text("R", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("B", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("4s", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("6s", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Divider(color = Cream.copy(alpha = 0.1f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(6.dp))

            // Batsman 1
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${match.batsman1}*", color = Cream, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(3f))
                Text("${match.batsman1Runs}", color = Cream, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("${match.batsman1Balls}", color = CreamMuted, fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("${match.batsman1Runs / 12}", color = CreamMuted, fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("${match.batsman1Runs / 24}", color = CreamMuted, fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Batsman 2
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(match.batsman2, color = Cream.copy(alpha = 0.8f), fontSize = 13.sp, modifier = Modifier.weight(3f))
                Text("${match.batsman2Runs}", color = Cream.copy(alpha = 0.8f), fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("${match.batsman2Balls}", color = CreamMuted, fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("${match.batsman2Runs / 15}", color = CreamMuted, fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("${match.batsman2Runs / 32}", color = CreamMuted, fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            }
        }
    }
}

@Composable
fun BowlingCardBlock(match: CricketMatch) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "ACTIVE BOWLER",
                color = Brass,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Header line
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("BOWLER", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(3f))
                Text("O", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("M", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("R", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("W", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Divider(color = Cream.copy(alpha = 0.1f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(6.dp))

            // Bowler
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(match.bowler, color = Cream, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(3f))
                Text(String.format("%.1f", match.bowlerOvers), color = Cream, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("${(match.bowlerOvers / 3).toInt()}", color = CreamMuted, fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("${match.bowlerRuns}", color = CreamMuted, fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("${match.bowlerWickets}", color = AlertGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            }
        }
    }
}

@Composable
fun PartnershipAndMatchSituationBlock(match: CricketMatch) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "MATCH SITUATION METRICS",
                color = Brass,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("CURRENT PARTNERSHIP", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    Text(match.partnership, color = Cream, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("PROJECTED TOTAL", color = Cream.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    val runs = match.scoreA.split("/").firstOrNull()?.replace(" & ", " ")?.split(" ")?.lastOrNull()?.toIntOrNull() ?: 240
                    val projected = (runs * 1.2).toInt()
                    Text("~$projected Runs", color = Brass, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun WinProbabilitySection(match: CricketMatch) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Brass.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = NavyDark)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Brass)
                )
                Text(
                    text = "PROBABILITY MATRIX",
                    color = Brass,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
            }

            // High-fidelity custom clipped probability bar exactly matching HTML style
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, Brass.copy(alpha = 0.15f), RoundedCornerShape(4.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Team A bar
                if (match.winProbabilityA > 0) {
                    Box(
                        modifier = Modifier
                            .weight(match.winProbabilityA.toFloat())
                            .fillMaxHeight()
                            .background(Burgundy)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${match.teamA.take(3).uppercase()} ${match.winProbabilityA}%",
                            color = Cream,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Draw/Tie bar
                val drawProb = 100 - match.winProbabilityA - match.winProbabilityB
                if (drawProb > 0) {
                    Box(
                        modifier = Modifier
                            .weight(drawProb.toFloat())
                            .fillMaxHeight()
                            .background(ForestGreen)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "DRAW ${drawProb}%",
                            color = Cream,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Team B bar
                if (match.winProbabilityB > 0) {
                    Box(
                        modifier = Modifier
                            .weight(match.winProbabilityB.toFloat())
                            .fillMaxHeight()
                            .background(Navy)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${match.teamB.take(3).uppercase()} ${match.winProbabilityB}%",
                            color = Cream.copy(alpha = 0.8f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.Info, null, tint = Brass, modifier = Modifier.size(12.dp))
                Text(
                    "Calibrated using live weather and pitch landings telemetry.",
                    color = Cream.copy(alpha = 0.5f),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun AnalyticsWagonWheelAndPitchMap() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Wagon Wheel Composable (drawn live on canvas!)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Slate)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "LIVE BATS DEFLECTION (WAGON WHEEL)",
                    color = Brass,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(NavyDark)
                        .border(1.dp, Slate, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(180.dp)) {
                        val center = Offset(size.width / 2, size.height / 2)
                        val radius = size.width / 2

                        // Draw Boundary circle
                        drawCircle(
                            color = Brass.copy(alpha = 0.5f),
                            radius = radius,
                            center = center,
                            style = Stroke(width = 2f)
                        )

                        // Draw inner ring (30 yard circle)
                        drawCircle(
                            color = Slate,
                            radius = radius * 0.5f,
                            center = center,
                            style = Stroke(width = 1.5f)
                        )

                        // Draw wicket pitch in center
                        drawRect(
                            color = CreamMuted,
                            topLeft = Offset(center.x - 6f, center.y - 18f),
                            size = androidx.compose.ui.geometry.Size(12f, 36f)
                        )

                        // Draw shot trajectory lines
                        // 1. Cover Drive (Green)
                        drawLine(
                            color = Color(0xFF32CD32),
                            start = center,
                            end = Offset(center.x - radius * 0.7f, center.y - radius * 0.5f),
                            strokeWidth = 3f
                        )
                        // 2. Pulled through Mid-wicket (Red)
                        drawLine(
                            color = Color(0xFFFF4D4D),
                            start = center,
                            end = Offset(center.x + radius * 0.8f, center.y + radius * 0.2f),
                            strokeWidth = 3f
                        )
                        // 3. Late Cut (Cyan)
                        drawLine(
                            color = Color(0xFF00FFFF),
                            start = center,
                            end = Offset(center.x - radius * 0.4f, center.y + radius * 0.7f),
                            strokeWidth = 3f
                        )
                        // 4. Straight Drive (Yellow)
                        drawLine(
                            color = Color.Yellow,
                            start = center,
                            end = Offset(center.x, center.y - radius * 0.9f),
                            strokeWidth = 3.5f
                        )
                    }

                    // Legends index overlay
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        LegendRow(color = Color(0xFF32CD32), text = "Off-side Drives: 64 Runs")
                        LegendRow(color = Color(0xFFFF4D4D), text = "On-side Pulls: 48 Runs")
                        LegendRow(color = Color.Yellow, text = "V-Arc Straight: 72 Runs")
                    }
                }
            }
        }

        // Pitch Map bounces (Good length, yorker, short)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Slate)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "PITCH LANDINGS ANALYSIS (PITCH MAP)",
                    color = Brass,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(NavyDark)
                        .border(1.dp, Slate, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        val canvasW = size.width
                        val canvasH = size.height

                        // Draw 2D lateral Cricket pitch (brown sand field)
                        drawRect(
                            color = Color(0xFFC5A880).copy(alpha = 0.4f),
                            topLeft = Offset(canvasW * 0.15f, canvasH * 0.2f),
                            size = androidx.compose.ui.geometry.Size(canvasW * 0.7f, canvasH * 0.6f)
                        )

                        // Draw creeping creases
                        drawLine(
                            color = Cream,
                            start = Offset(canvasW * 0.22f, canvasH * 0.2f),
                            end = Offset(canvasW * 0.22f, canvasH * 0.8f),
                            strokeWidth = 2f
                        )
                        drawLine(
                            color = Cream,
                            start = Offset(canvasW * 0.78f, canvasH * 0.2f),
                            end = Offset(canvasW * 0.78f, canvasH * 0.8f),
                            strokeWidth = 2f
                        )

                        // Draw color coded delivery zones
                        // Short delivery (Red zone) on left
                        drawRect(
                            color = Color(0xFFFF4D4D).copy(alpha = 0.2f),
                            topLeft = Offset(canvasW * 0.25f, canvasH * 0.2f),
                            size = androidx.compose.ui.geometry.Size(canvasW * 0.14f, canvasH * 0.6f)
                        )
                        // Good length (Green zone) in middle
                        drawRect(
                            color = Color(0xFF32CD32).copy(alpha = 0.2f),
                            topLeft = Offset(canvasW * 0.39f, canvasH * 0.2f),
                            size = androidx.compose.ui.geometry.Size(canvasW * 0.18f, canvasH * 0.6f)
                        )
                        // Full delivery (Blue zone)
                        drawRect(
                            color = Color(0xFF00FFFF).copy(alpha = 0.2f),
                            topLeft = Offset(canvasW * 0.57f, canvasH * 0.2f),
                            size = androidx.compose.ui.geometry.Size(canvasW * 0.12f, canvasH * 0.6f)
                        )

                        // Dots representing individual ball impacts in current spell
                        // Short bounce
                        drawCircle(color = Color(0xFFFF4D4D), radius = 6f, center = Offset(canvasW * 0.32f, canvasH * 0.45f))
                        // Good length hits
                        drawCircle(color = Color(0xFF32CD32), radius = 6f, center = Offset(canvasW * 0.44f, canvasH * 0.35f))
                        drawCircle(color = Color(0xFF32CD32), radius = 6f, center = Offset(canvasW * 0.48f, canvasH * 0.55f))
                        drawCircle(color = Color(0xFF32CD32), radius = 6f, center = Offset(canvasW * 0.51f, canvasH * 0.48f))
                        // Yorker/Full
                        drawCircle(color = Color(0xFF00FFFF), radius = 6f, center = Offset(canvasW * 0.62f, canvasH * 0.5f))
                    }

                    // Index taggers
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        LegendRow(color = Color(0xFFFF4D4D), text = "Short (1)")
                        LegendRow(color = Color(0xFF32CD32), text = "Good L. (3)")
                        LegendRow(color = Color(0xFF00FFFF), text = "Full/Yorker (1)")
                    }
                }
            }
        }
    }
}

@Composable
fun LegendRow(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
        Text(text = text, color = Cream.copy(alpha = 0.8f), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
    }
}

@Composable
fun CommentaryRow(line: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Slate, RoundedCornerShape(4.dp))
            .background(NavyDark)
            .padding(12.dp)
    ) {
        Column {
            val parts = line.split(" - ")
            val over = parts.firstOrNull() ?: ""
            val comment = if (parts.size > 1) parts.subList(1, parts.size).joinToString(" - ") else ""

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = over,
                    color = Brass,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(end = 8.dp)
                )
                if (comment.contains("OUT") || comment.contains("WICKET")) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(AlertRed)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text("WICKET", color = Cream, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                } else if (comment.contains("FOUR") || comment.contains("SIX")) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(ForestGreen)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text("BOUNDARY", color = Cream, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment,
                color = Cream,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}
