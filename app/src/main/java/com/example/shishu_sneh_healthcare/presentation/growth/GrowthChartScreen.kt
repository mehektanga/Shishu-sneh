package com.example.shishu_sneh_healthcare.presentation.growth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.example.shishu_sneh_healthcare.data.local.entity.GrowthEntryEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowthChartScreen(
    babyId: Long,
    onBackClick: () -> Unit,
    viewModel: GrowthViewModel = hiltViewModel()
) {
    val growthEntries by viewModel.growthEntries.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = babyId) {
        viewModel.loadGrowthEntries(babyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Growth Analytics", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Growth Entry")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (growthEntries.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Weight Progress (kg)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GrowthLineChart(entries = growthEntries.mapIndexed { index, entry ->
                            Entry(index.toFloat(), entry.weight.toFloat())
                        })
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    Text("No growth data yet. Tap + to add.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Recent History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            growthEntries.reversed().forEach { entry ->
                GrowthEntryItem(entry)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (showAddDialog) {
            AddGrowthDialog(
                onDismiss = { showAddDialog = false },
                onSave = { weight, height, head ->
                    viewModel.addGrowthEntry(
                        GrowthEntryEntity(
                            babyId = babyId,
                            date = System.currentTimeMillis(),
                            weight = weight.toDoubleOrNull() ?: 0.0,
                            height = height.toDoubleOrNull() ?: 0.0,
                            headCirc = head.toDoubleOrNull() ?: 0.0,
                            notes = ""
                        )
                    )
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun GrowthEntryItem(entry: GrowthEntryEntity) {
    val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(entry.date))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = dateStr, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = "Weight: ${entry.weight} kg | Height: ${entry.height} cm", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (entry.headCirc > 0) {
                Text(text = "HC: ${entry.headCirc} cm", fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun AddGrowthDialog(onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var head by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Baby Growth", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Height (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = head,
                    onValueChange = { head = it },
                    label = { Text("Head Circumference (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(weight, height, head) },
                enabled = weight.isNotEmpty() && height.isNotEmpty()
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
fun GrowthLineChart(entries: List<Entry>) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                setScaleEnabled(true)
                setDrawGridBackground(false)
                
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                }
                
                axisLeft.apply {
                    setDrawGridLines(true)
                    gridColor = android.graphics.Color.LTGRAY
                }
                
                axisRight.isEnabled = false
                legend.isEnabled = true
            }
        },
        update = { chart ->
            val dataSet = LineDataSet(entries, "Weight").apply {
                setColor(android.graphics.Color.parseColor("#F48FB1"))
                setCircleColor(android.graphics.Color.parseColor("#F48FB1"))
                lineWidth = 3f
                circleRadius = 5f
                setDrawCircleHole(true)
                circleHoleRadius = 2f
                valueTextSize = 10f
                setDrawFilled(true)
                fillColor = android.graphics.Color.parseColor("#F48FB1")
                fillAlpha = 40
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            chart.data = LineData(dataSet)
            chart.animateX(1000)
            chart.invalidate()
        }
    )
}
