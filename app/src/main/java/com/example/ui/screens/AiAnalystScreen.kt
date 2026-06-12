package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Send
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
import com.example.data.AiService
import com.example.data.QueryHistory
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiAnalystScreen(
    historyList: List<QueryHistory>,
    onSaveQuery: (String) -> Unit,
    onClearHistory: () -> Unit
) {
    var promptInput by remember { mutableStateOf("") }
    var runningResponse by remember { mutableStateOf<String?>(null) }
    var isComputing by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val quickQueries = listOf(
        "Compare Joe Root and Ken Barrington.",
        "Show Lancashire's record at Old Trafford since 2000.",
        "Who has the best County Championship average this season?",
        "Analyse today's bowling performance."
    )

    fun executeQuery(text: String) {
        if (text.isBlank()) return
        isComputing = true
        runningResponse = "Connecting with National Cricket Observatory neural core...\nConducting deep archive statistics analysis... Please hold."
        onSaveQuery(text)
        coroutineScope.launch {
            try {
                val ans = AiService.getAnalysis(text)
                runningResponse = ans
            } catch (e: Exception) {
                runningResponse = "Error contacting AI reasoning core: ${e.localizedMessage}"
            } finally {
                isComputing = false
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Analyst Header ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Brass.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                    .background(NavyDark)
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "AI Core", tint = Brass)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CRICINTEL REASONING CORE",
                            color = Brass,
                            style = MaterialTheme.typography.labelSmall,
                            letterSpacing = 2.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "AI Cricket Analyst",
                        color = Cream,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Powered by Gemini 3.5 Flash • Accessing historical scores, county rosters, playbooks, and centuries archives.",
                        color = Cream.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }
        }

        // --- Quick Recommended Query Dossiers ---
        item {
            Column {
                Text(
                    text = "SELECT ADVANCED ANALYTICAL TEMPLATE",
                    color = Cream.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                FlowRowQuick(spacing = 8.dp) {
                    quickQueries.forEach { q ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dp))
                                .background(Slate)
                                .clickable {
                                    promptInput = q
                                    executeQuery(q)
                                }
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(Brass)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = q,
                                    color = Cream,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }

        // --- Live Input Console ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Slate, RoundedCornerShape(4.dp))
                    .background(NavyDark)
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "QUERY TELEMETRY INPUT",
                        color = Brass,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = promptInput,
                            onValueChange = { promptInput = it },
                            placeholder = { Text("Ask about any county records, averages, or eras...", color = Cream.copy(alpha = 0.4f), fontSize = 12.sp) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Slate,
                                unfocusedContainerColor = Slate,
                                focusedTextColor = Cream,
                                unfocusedTextColor = Cream,
                                cursorColor = Brass,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .weight(4f)
                                .testTag("ai_prompt_input"),
                            shape = RoundedCornerShape(4.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                executeQuery(promptInput)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Burgundy)
                                .testTag("ai_send_button")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Query", tint = Cream)
                        }
                    }
                }
            }
        }

        // --- Active Reasoning Output Ticker ---
        if (runningResponse != null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Brass, RoundedCornerShape(6.dp))
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
                                text = "ANALYST DOSSIER TRANSCRIPT",
                                color = AlertGreen,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )

                            if (isComputing) {
                                CircularProgressIndicator(color = Brass, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                            } else {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(ForestGreen)
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text("VERIFIED DATA", color = Cream, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Custom structured parser for markdown style headers!
                        StructuredResultField(text = runningResponse ?: "")
                    }
                }
            }
        }

        // --- Query History Vault (Room Persistent!) ---
        if (historyList.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.History, null, tint = Cream.copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "HISTORY QUERY LOG",
                            color = Cream.copy(alpha = 0.5f),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clickable { onClearHistory() }
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.DeleteSweep, null, tint = AlertRed, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("CLEAR HISTORY", color = AlertRed, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(historyList) { h ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Slate)
                        .border(1.dp, Slate, RoundedCornerShape(4.dp))
                        .clickable {
                            promptInput = h.queryText
                            executeQuery(h.queryText)
                        }
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = h.queryText,
                            color = CreamMuted,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1,
                            modifier = Modifier.weight(4f)
                        )
                        Icon(Icons.Default.History, null, tint = Cream.copy(alpha = 0.2f), modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}

// Flow Row helper Composable for quick options
@Composable
fun FlowRowQuick(
    spacing: androidx.compose.ui.unit.Dp,
    content: @Composable () -> Unit
) {
    Column {
        content()
    }
}

// Beautiful formatted output text parser supporting subheads, bullet lists, markdown-like layout
@Composable
fun StructuredResultField(text: String) {
    val lines = text.split("\n")

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        lines.forEach { line ->
            val clean = line.trim()
            when {
                clean.startsWith("# ") -> {
                    Text(
                        text = clean.substring(2).uppercase(),
                        color = Brass,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                    )
                }
                clean.startsWith("## ") -> {
                    Text(
                        text = clean.substring(3),
                        color = Cream,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                    )
                }
                clean.startsWith("### ") -> {
                    Text(
                        text = clean.substring(4),
                        color = Brass,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
                clean.startsWith("* ") || clean.startsWith("- ") -> {
                    val bulletTxt = if (clean.startsWith("* ")) clean.substring(2) else clean.substring(2)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("• ", color = Brass, fontSize = 13.sp)
                        Text(text = bulletTxt, color = Cream, fontSize = 12.sp, lineHeight = 16.sp)
                    }
                }
                clean.startsWith("1. ") || clean.startsWith("2. ") || clean.startsWith("3. ") || clean.startsWith("4. ") -> {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = clean.take(3), color = Brass, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        Text(text = clean.substring(3), color = Cream, fontSize = 12.sp, lineHeight = 16.sp)
                    }
                }
                clean.isNotBlank() -> {
                    Text(
                        text = clean,
                        color = CreamMuted,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
                else -> {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
