package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LiveStreamScreen(
    initialFeedName: String = "Lancashire vs Surrey - Day 3"
) {
    var activeCamera by remember { mutableStateOf("Tactical Alpha") }
    val cameras = listOf("Tactical Alpha", "Slip Cam Beta", "Bowler Crawler", "Drone Sky")

    var selectedHighlightTime by remember { mutableStateOf<String?>(null) }
    var isPlayingReplay by remember { mutableStateOf(false) }

    // Pulsing broadcast light animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Broadcast Header ---
        item {
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(50))
                                .background(AlertRed.copy(alpha = pulseAlpha))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CRICKET TV • BROADCAST CONSOLE",
                            color = Cream,
                            style = MaterialTheme.typography.labelSmall,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Visibility, null, tint = Brass, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "24,842 AUDIENCES LIVE",
                            color = Brass,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // --- Styled Video Canvas (Broadcaster View) ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .border(2.dp, Brass, RoundedCornerShape(8.dp))
                    .background(Color.Black)
            ) {
                // Drawing dynamic trajectory telemetry curves representing actual broadcast tracking!
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Draw green field outline
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF0F2B14), Color(0xFF1E4225))
                        ),
                        topLeft = Offset(0f, 0f),
                        size = size
                    )

                    // Draw cricket crease lines (perspective view)
                    drawLine(
                        color = Color.White.copy(alpha = 0.3f),
                        start = Offset(w * 0.1f, h * 0.8f),
                        end = Offset(w * 0.9f, h * 0.8f),
                        strokeWidth = 3f
                    )
                    drawLine(
                        color = Color.White.copy(alpha = 0.3f),
                        start = Offset(w * 0.3f, h * 0.3f),
                        end = Offset(w * 0.7f, h * 0.3f),
                        strokeWidth = 2f
                    )

                    // Trajectory flight curve (yellow neon path!)
                    val pathProgress = 1.0f
                    val controlX = w * 0.5f
                    val controlY = h * 0.2f
                    val startPt = Offset(w * 0.5f, h * 0.3f)
                    val endPt = Offset(w * 0.45f, h * 0.8f)

                    // Draw flight path
                    drawLine(
                        color = Brass,
                        start = startPt,
                        end = Offset(w * 0.5f, h * 0.5f),
                        strokeWidth = 3f
                    )
                    drawLine(
                        color = Color.Yellow,
                        start = Offset(w * 0.5f, h * 0.5f),
                        end = endPt,
                        strokeWidth = 4f
                    )

                    // Draw bounce impact glowing dot
                    drawCircle(
                        color = AlertGreen,
                        radius = 8f,
                        center = Offset(w * 0.5f, h * 0.5f)
                    )
                }

                // Overlay information
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(12.dp)
                ) {
                    // Top stats watermark
                    Row(
                        modifier = Modifier.fillMaxWidth().align(Alignment.TopStart),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(Navy.copy(alpha = 0.8f))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "STREAM FEED: $activeCamera".uppercase(),
                                color = Cream,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (isPlayingReplay) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(AlertRed)
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "INSTANT REPLAY",
                                    color = Cream,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Play button or overlay indicators
                    IconButton(
                        onClick = { isPlayingReplay = !isPlayingReplay },
                        modifier = Modifier
                            .size(54.dp)
                            .align(Alignment.Center)
                            .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(50))
                    ) {
                        Icon(
                            imageVector = if (isPlayingReplay) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                            contentDescription = "play replay",
                            tint = Brass,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    // Bottom info (overlaying bowler stats)
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .clip(RoundedCornerShape(4.dp))
                            .background(NavyDark.copy(alpha = 0.75f))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "LANCASHIRE vs SURREY • DAY 3 CC",
                            color = Cream,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "Bowler Speed: 88.4 mph • Ball Turn: 2.1°",
                            color = Brass,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // --- Multi-Camera Control Console ---
        item {
            Column {
                Text(
                    text = "SELECT SATELLITE CAMERA ANGLE",
                    color = Brass,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    cameras.forEach { cam ->
                        val isSel = activeCamera == cam
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isSel) Burgundy else Slate)
                                .border(1.dp, if (isSel) Brass else Color.Transparent, RoundedCornerShape(4.dp))
                                .clickable {
                                    activeCamera = cam
                                    isPlayingReplay = false
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = if (cam.contains("Sky")) Icons.Default.Videocam else Icons.Default.Duo,
                                    contentDescription = null,
                                    tint = if (isSel) Brass else Cream.copy(alpha = 0.5f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = cam.split(" ").last(),
                                    color = if (isSel) Cream else CreamMuted,
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

        // --- Stream Replays & Highlights Timelines ---
        item {
            Column {
                Text(
                    text = "TIMELINE REPLAYS (TACTICAL MOMENTS)",
                    color = Cream,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    HighlightTimelineRow(
                        title = "Outswing Wicket: J. Anderson to R. Burns",
                        time = "34.1 Overs",
                        desc = "Classical late outswinger catching outside edge to slip.",
                        isSelected = selectedHighlightTime == "34.1",
                        onClick = {
                            selectedHighlightTime = "34.1"
                            isPlayingReplay = true
                        }
                    )

                    HighlightTimelineRow(
                        title = "Four Runs: Keaton Jennings Cover Drive",
                        time = "42.5 Overs",
                        desc = "Jennings strides forward, hitting beautifully past cover.",
                        isSelected = selectedHighlightTime == "42.5",
                        onClick = {
                            selectedHighlightTime = "42.5"
                            isPlayingReplay = true
                        }
                    )

                    HighlightTimelineRow(
                        title = "Wicket Alert: S. Abbott claiming J. Bohannon",
                        time = "44.1 Overs",
                        desc = "Searing short delivery gloved behind to wicketkeeper.",
                        isSelected = selectedHighlightTime == "44.1",
                        onClick = {
                            selectedHighlightTime = "44.1"
                            isPlayingReplay = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HighlightTimelineRow(
    title: String,
    time: String,
    desc: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) Slate else NavyDark)
            .border(1.dp, if (isSelected) Brass else Slate, RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(4f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (title.contains("Wicket")) AlertRed else ForestGreen)
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (title.contains("Wicket")) "WKT" else "FOUR",
                            color = Cream,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        color = Cream,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    color = Cream.copy(alpha = 0.5f),
                    fontSize = 11.sp
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = time,
                    color = Brass,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = if (isSelected) AlertGreen else Cream.copy(alpha = 0.2f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
