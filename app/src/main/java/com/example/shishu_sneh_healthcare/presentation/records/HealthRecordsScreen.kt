package com.example.shishu_sneh_healthcare.presentation.records

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
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
import com.example.shishu_sneh_healthcare.data.local.entity.HealthRecordEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthRecordsScreen(
    babyId: Long,
    onBackClick: () -> Unit,
    viewModel: HealthRecordsViewModel = hiltViewModel()
) {
    val records by viewModel.records.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = babyId) {
        viewModel.loadRecords(babyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Records", fontWeight = FontWeight.Bold) },
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
                Icon(Icons.Default.Add, contentDescription = "Add Record")
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
                text = "Medical Vault",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(records) { record ->
                    HealthRecordItem(record = record)
                }
                
                if (records.isEmpty()) {
                    item {
                        EmptyRecordsState()
                    }
                }
            }
        }

        if (showAddDialog) {
            AddHealthRecordDialog(
                onDismiss = { showAddDialog = false },
                onSave = { type, doctor, clinic, notes ->
                    viewModel.addRecord(
                        HealthRecordEntity(
                            babyId = babyId,
                            date = System.currentTimeMillis(),
                            type = type,
                            doctorName = doctor,
                            clinic = clinic,
                            notes = notes,
                            attachmentUri = null
                        )
                    )
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AddHealthRecordDialog(onDismiss: () -> Unit, onSave: (String, String, String, String) -> Unit) {
    var type by remember { mutableStateOf("") }
    var doctor by remember { mutableStateOf("") }
    var clinic by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Health Record", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Record Type (e.g. Doctor Visit)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = doctor,
                    onValueChange = { doctor = it },
                    label = { Text("Doctor Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = clinic,
                    onValueChange = { clinic = it },
                    label = { Text("Clinic / Hospital") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes / Prescription") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(type, doctor, clinic, notes) },
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
fun HealthRecordItem(record: HealthRecordEntity) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dateString = dateFormat.format(Date(record.date))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.size(50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = record.type, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = dateString, fontSize = 14.sp, color = Color.Gray)
                if (!record.doctorName.isNullOrEmpty()) {
                    Text(text = "Dr. ${record.doctorName}", fontSize = 14.sp, color = Color.Gray)
                }
                if (!record.clinic.isNullOrEmpty()) {
                    Text(text = record.clinic!!, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                if (!record.notes.isNullOrEmpty()) {
                    Text(text = record.notes!!, fontSize = 13.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun EmptyRecordsState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "📂", fontSize = 60.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "No records found", fontWeight = FontWeight.Bold, color = Color.Gray)
        Text(text = "Upload prescriptions, lab reports and more", fontSize = 14.sp, color = Color.Gray)
    }
}
