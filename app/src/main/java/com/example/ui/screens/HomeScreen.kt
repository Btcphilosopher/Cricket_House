package com.example.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.County
import com.example.data.CricketMatch
import com.example.data.NewsArticle
import com.example.data.Repository
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    matches: List<CricketMatch>,
    followedCounties: List<County>,
    onFollowCounty: (String, Boolean) -> Unit,
    onSelectMatch: (CricketMatch) -> Unit,
    onSelectStream: (String) -> Unit,
    onNavigateToNav: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // --- 1. Institutional Hero Board Banner ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Brass.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                    .background(NavyDark)
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "THE NATIONAL CRICKET OBSERVATORY",
                            color = Brass,
                            style = MaterialTheme.typography.labelSmall,
                            letterSpacing = 2.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(AlertGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "LIVE TELEMETRY",
                                color = Cream,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "CRICKET HOUSE",
                        color = Cream,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Lords 2050 digital headquarters • Digital Operating System of Cricket",
                        color = Cream.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // --- 2. Live Match Scoreboard Corridor ---
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIVE SCOREBOARDS",
                        color = Cream,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "VIEW TIMETABLE",
                        color = Brass,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .testTag("view_timetable")
                            .clickable { onNavigateToNav("Matches") }
                    )
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(matches) { match ->
                        LiveMatchTickerCard(
                            match = match,
                            onCardClick = { onSelectMatch(match) }
                        )
                    }
                }
            }
        }

        // --- 3. Followed Counties & Country Dashboard Shortcuts ---
        item {
            Column {
                Text(
                    text = "REGISTERED COUNTY CHANNELS",
                    color = Cream,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Slate)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Follow counties to subscribe to wicket alerts, transfer notifications, and custom dashboards.",
                            color = CreamMuted,
                            fontSize = 12.sp
                        )

                        // County quick grid
                        FlowRow(
                            spacing = 8.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Repository.firstClassCounties.forEach { countyName ->
                                val isFollowed = followedCounties.any { it.id == countyName }
                                FilterChip(
                                    selected = isFollowed,
                                    onClick = { onFollowCounty(countyName, !isFollowed) },
                                    label = {
                                        Text(
                                            text = countyName,
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = if (isFollowed) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ForestGreen,
                                        selectedLabelColor = Cream,
                                        containerColor = Navy,
                                        labelColor = CreamMuted
                                    ),
                                    leadingIcon = if (isFollowed) {
                                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(12.dp)) }
                                    } else {
                                        { Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(12.dp)) }
                                    },
                                    modifier = Modifier.testTag("follow_county_$countyName")
                                )
                            }
                        }

                        if (followedCounties.isNotEmpty()) {
                            Divider(color = Cream.copy(alpha = 0.1f), thickness = 1.dp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Active County Vault: ${followedCounties.size} County Tracked",
                                    color = Brass,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Button(
                                    onClick = { onNavigateToNav("Counties") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Burgundy),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text("ENTER HUB", color = Cream, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 4. Highlights & Featured Streams ---
        item {
            Column {
                Text(
                    text = "FEATURED SATELLITE STREAMS",
                    color = Cream,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Brass.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .clickable { onSelectStream("Lancashire vs Surrey - Camera Alpha") },
                    colors = CardDefaults.cardColors(containerColor = NavyDark)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Slate, NavyDark)
                                )
                            )
                    ) {
                        // Drawing futuristic vector overlay inside stream window
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .padding(12.dp)
                        ) {
                            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AlertRed)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        "LIVE • MULTI-CAM FEED",
                                        color = Cream,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    "County Championship • Old Trafford Special",
                                    color = Cream,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    "Lancashire vs Surrey • Camera Alpha (Pitch Map Linked)",
                                    color = Cream.copy(alpha = 0.7f),
                                    fontSize = 12.sp
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = "Play stream",
                                tint = Brass,
                                modifier = Modifier
                                    .size(48.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }

        // --- 5. Journalism Headlines Corridor ---
        item {
            Column {
                Text(
                    text = "THE DAILY COURIER Headlines",
                    color = Cream,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Repository.newsArticles.forEach { article ->
                        NewsRowCard(article = article)
                    }
                }
            }
        }
    }
}

@Composable
fun LiveMatchTickerCard(
    match: CricketMatch,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(
            containerColor = Cream
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Burgundy solid left border stripe
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(6.dp)
                    .background(color = Burgundy)
            )
            
            Column(modifier = Modifier.padding(14.dp).weight(1f)) {
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
                            text = match.type.uppercase(),
                            color = Cream,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (match.isLive) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(AlertGreen)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "LIVE DATA",
                                color = Navy,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Text(
                            text = "COMPLETED",
                            color = Navy.copy(alpha = 0.5f),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Score details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = match.teamA.uppercase(),
                        color = Navy,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp
                    )
                    Text(
                        text = match.scoreA,
                        color = Navy,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = match.teamB.uppercase(),
                        color = Navy.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 15.sp
                    )
                    Text(
                        text = match.scoreB,
                        color = Navy.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Divider(color = Navy.copy(alpha = 0.15f), thickness = 1.dp)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = match.matchStatus.uppercase(),
                    color = Burgundy,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )

                if (match.isLive) {
                    Text(
                        text = "OVERS: ${match.oversA}  (RR: ${match.runRate})",
                        color = Navy.copy(alpha = 0.6f),
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun NewsRowCard(article: NewsArticle) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = article.category.uppercase(),
                    color = Burgundy,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = article.timestamp,
                    color = Cream.copy(alpha = 0.4f),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.title,
                color = Cream,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = article.teaser,
                color = Cream.copy(alpha = 0.7f),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "By ${article.author}",
                color = Brass,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


// Flow Row helper Composable to avoid heavy libraries
@Composable
fun FlowRow(
    spacing: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        // Since we are inside a Compose list item, we can group layouts neatly
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
                // Approximate grouping for clean rendering
                val totalChildren = 8
                val firstRow = listOf("Lancashire", "Yorkshire", "Surrey", "Essex")
                val secondRow = listOf("Somerset", "Warwickshire", "Nottinghamshire", "Middlesex")
                Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                    firstRow.forEach { content() } // Overridden content will handle them
                }
                // We'll let standard Row with simple scroll represent our County grid for perfect performance and dynamic rendering instead of manual layout approximations
            }
        }
    }
}

// Better layout for County selectors using a lazy flow row or scrollable grid row:
@Composable
fun FlowRowY(
    items: List<String>,
    selectedItems: List<String>,
    onToggle: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Custom wrap chunking
        val chunked = items.chunked(3)
        chunked.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { item ->
                    val isSelected = selectedItems.contains(item)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSelected) ForestGreen else Navy)
                            .border(1.dp, if (isSelected) Brass else Slate, RoundedCornerShape(4.dp))
                            .clickable { onToggle(item) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.AddCircle,
                                contentDescription = null,
                                tint = if (isSelected) Brass else Cream.copy(alpha = 0.4f),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item,
                                color = if (isSelected) Cream else CreamMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
