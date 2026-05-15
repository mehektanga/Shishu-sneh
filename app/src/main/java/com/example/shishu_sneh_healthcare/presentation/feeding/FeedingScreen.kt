package com.example.shishu_sneh_healthcare.presentation.feeding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var showAddDialog by remember { mutableStateOf(false) }

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
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Log")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
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
                        EmptyFeedingState()
                    }
                }
            }
        }

        if (showAddDialog) {
            AddFeedingDialog(
                onDismiss = { showAddDialog = false },
                onSave = { type, amount, notes ->
                    viewModel.addFeedingLog(
                        FeedingLogEntity(
                            babyId = babyId,
                            startTime = System.currentTimeMillis(),
                            duration = 0,
                            type = type,
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            foodItem = null,
                            notes = notes
                        )
                    )
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AddFeedingDialog(onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var type by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Feeding", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Feeding Type (e.g. Milk, Solids)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Quantity (ml / gm)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(type, amount, notes) },
                enabled = type.isNotEmpty()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun FeedingLogItem(log: FeedingLogEntity) {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val startTime = timeFormat.format(Date(log.startTime))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = log.type, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "Time: $startTime", fontSize = 14.sp, color = Color.Gray)
                if (log.amount > 0) {
                    Text(text = "Amount: ${log.amount}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
            if (!log.notes.isNullOrEmpty()) {
                Text(text = log.notes, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun EmptyFeedingState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "🍼", fontSize = 60.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "No logs yet", fontWeight = FontWeight.Bold, color = Color.Gray)
        Text(text = "Track baby's feeding schedule here", fontSize = 14.sp, color = Color.Gray)
    }
}
