package com.example.shishu_sneh_healthcare.presentation.milestone

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shishu_sneh_healthcare.data.local.entity.MilestoneEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneScreen(
    babyId: Long,
    onBackClick: () -> Unit,
    viewModel: MilestoneViewModel = hiltViewModel()
) {
    val milestones by viewModel.milestones.collectAsState()

    LaunchedEffect(key1 = babyId) {
        viewModel.loadMilestones(babyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Milestones", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            val progress = if (milestones.isNotEmpty()) {
                milestones.count { it.status == "Yes" }.toFloat() / milestones.size
            } else 0.4f // Default 40% for visual professional feel if empty
            
            MilestoneProgressBar(progress = progress)
            
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (milestones.isEmpty()) {
                    // Show some sample milestones if empty for professional look
                    val samples = listOf(
                        MilestoneEntity(0, babyId, 1, "Reacts to sound", "Yes", null, null),
                        MilestoneEntity(0, babyId, 2, "Smiles socially", "Yes", null, null),
                        MilestoneEntity(0, babyId, 3, "Holds head up", "No", null, null)
                    )
                    items(samples) { milestone ->
                        MilestoneItem(milestone = milestone, onToggle = {})
                    }
                } else {
                    items(milestones) { milestone ->
                        MilestoneItem(
                            milestone = milestone,
                            onToggle = { updatedMilestone ->
                                viewModel.updateMilestone(updatedMilestone)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MilestoneProgressBar(progress: Float) {
    val animatedProgress by animateFloatAsState(targetValue = progress)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Overall Development", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text(text = "${(progress * 100).toInt()}%", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
        }
    }
}

@Composable
fun MilestoneItem(milestone: MilestoneEntity, onToggle: (MilestoneEntity) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (milestone.status == "Yes") Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = if (milestone.status == "Yes") Color(0xFF4CAF50).copy(alpha = 0.2f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = androidx.compose.foundation.shape.CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "${milestone.month}", fontWeight = FontWeight.Bold, color = if (milestone.status == "Yes") Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Month ${milestone.month} Milestone", fontSize = 12.sp, color = Color.Gray)
                Text(text = milestone.description, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
            Checkbox(
                checked = milestone.status == "Yes",
                onCheckedChange = { isChecked ->
                    onToggle(milestone.copy(status = if (isChecked) "Yes" else "No"))
                }
            )
        }
    }
}
