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
            FloatingActionButton(onClick = { /* Add Medication */ }) {
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
    }
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
