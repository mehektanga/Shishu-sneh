package com.example.shishu_sneh_healthcare.presentation.feeding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shishu_sneh_healthcare.data.local.entity.FeedingLogEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedingScreen(
    babyId: Long,
    onBackClick: () -> Unit,
    viewModel: FeedingViewModel = hiltViewModel()
) {
    val feedingLogs by viewModel.feedingLogs.collectAsState()

    LaunchedEffect(key1 = babyId) {
        viewModel.loadFeedingLogs(babyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Feeding Log", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Open add log dialog */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add Log")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Recent Activities",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(feedingLogs) { log ->
                    FeedingLogItem(log = log)
                }
                
                if (feedingLogs.isEmpty()) {
                    item {
                        Text(text = "No feeding logs yet. Start by adding one!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun FeedingLogItem(log: FeedingLogEntity) {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val startTime = timeFormat.format(Date(log.startTime))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = log.type, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "Started at: $startTime", fontSize = 14.sp)
                if (log.amount > 0) {
                    Text(text = "Amount: ${log.amount} ml", fontSize = 14.sp)
                }
            }
            if (log.duration > 0) {
                Text(
                    text = "${log.duration} min",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
