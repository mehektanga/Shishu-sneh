package com.example.shishu_sneh_healthcare.presentation.medication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shishu_sneh_healthcare.data.local.entity.MedicationEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationScreen(
    babyId: Long,
    onBackClick: () -> Unit,
    viewModel: MedicationViewModel = hiltViewModel()
) {
    val medications by viewModel.medications.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = babyId) {
        viewModel.loadMedications(babyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Medications", fontWeight = FontWeight.Bold) },
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
                Icon(Icons.Default.Add, contentDescription = "Add")
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
                text = "Active Reminders",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(medications) { medication ->
                    MedicationItem(medication)
                }
                
                if (medications.isEmpty()) {
                    item {
                        EmptyMedicationState()
                    }
                }
            }
        }

        if (showAddDialog) {
            AddMedicationDialog(
                onDismiss = { showAddDialog = false },
                onSave = { name, dose, timing, notes ->
                    viewModel.addMedication(
                        MedicationEntity(
                            babyId = babyId,
                            name = name,
                            dose = dose,
                            frequency = timing,
                            startDate = System.currentTimeMillis(),
                            endDate = null,
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
fun AddMedicationDialog(onDismiss: () -> Unit, onSave: (String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }
    var timing by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Medication", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Medicine Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dose,
                    onValueChange = { dose = it },
                    label = { Text("Dosage (e.g. 5ml)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = timing,
                    onValueChange = { timing = it },
                    label = { Text("Timing (e.g. Twice daily)") },
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
                onClick = { onSave(name, dose, timing, notes) },
                enabled = name.isNotEmpty() && dose.isNotEmpty()
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
fun MedicationItem(medication: MedicationEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Medication, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = medication.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "${medication.dose} • ${medication.frequency}", fontSize = 14.sp, color = Color.Gray)
                if (!medication.notes.isNullOrEmpty()) {
                    Text(text = medication.notes, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun EmptyMedicationState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "💊", fontSize = 60.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "No medications set", fontWeight = FontWeight.Bold, color = Color.Gray)
        Text(text = "Add reminders for baby's drops or vitamins", fontSize = 14.sp, color = Color.Gray)
    }
}
