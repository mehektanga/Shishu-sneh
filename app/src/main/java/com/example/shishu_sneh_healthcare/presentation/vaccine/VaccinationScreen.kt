package com.example.shishu_sneh_healthcare.presentation.vaccine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shishu_sneh_healthcare.data.local.entity.VaccineEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccinationScreen(
    babyId: Long,
    onBackClick: () -> Unit,
    viewModel: VaccinationViewModel = hiltViewModel()
) {
    val vaccines by viewModel.vaccines.collectAsState()

    LaunchedEffect(key1 = babyId) {
        viewModel.loadVaccines(babyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Immunization Calendar", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Track your baby's protection",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(vaccines) { vaccine ->
                    VaccineCard(vaccine = vaccine)
                }
                
                if (vaccines.isEmpty()) {
                    item {
                        EmptyVaccineState()
                    }
                }
            }
        }
    }
}

@Composable
fun VaccineCard(vaccine: VaccineEntity) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dueDate = dateFormat.format(Date(vaccine.scheduledDate))

    val statusColor = when (vaccine.status) {
        "Done" -> Color(0xFF4CAF50)
        "Overdue" -> MaterialTheme.colorScheme.error
        "Due Today" -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = statusColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (vaccine.status == "Done") Icons.Default.CheckCircle else Icons.Default.CheckCircle, // Placeholder for specific vaccine icon
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = vaccine.name, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                Text(text = vaccine.disease, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Due: $dueDate", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
            StatusChip(status = vaccine.status, color = statusColor)
        }
    }
}

@Composable
fun StatusChip(status: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Text(
            text = status,
            color = color,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EmptyVaccineState() {
    Box(modifier = Modifier.fillMaxSize().padding(top = 100.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "💉", fontSize = 60.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "No vaccines scheduled yet", fontWeight = FontWeight.Bold, color = Color.Gray)
        }
    }
}
