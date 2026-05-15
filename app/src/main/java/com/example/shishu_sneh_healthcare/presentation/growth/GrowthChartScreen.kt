package com.example.shishu_sneh_healthcare.presentation.growth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.example.shishu_sneh_healthcare.data.local.entity.GrowthEntryEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowthChartScreen(
    babyId: Long,
    onBackClick: () -> Unit,
    viewModel: GrowthViewModel = hiltViewModel()
) {
    val growthEntries by viewModel.growthEntries.collectAsState()

    LaunchedEffect(key1 = babyId) {
        viewModel.loadGrowthEntries(babyId)
    }

    // Mock data for professional preview if empty
    val displayedEntries = if (growthEntries.isEmpty()) {
        listOf(
            GrowthEntryEntity(0, babyId, 0, 3.2, 50.0, 34.0, "Birth"),
            GrowthEntryEntity(0, babyId, 1, 4.1, 54.0, 36.0, "Month 1"),
            GrowthEntryEntity(0, babyId, 2, 5.0, 58.0, 38.0, "Month 2"),
            GrowthEntryEntity(0, babyId, 3, 5.8, 61.0, 39.5, "Month 3")
        )
    } else {
        growthEntries
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
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
                    GrowthLineChart(entries = displayedEntries.mapIndexed { index, entry ->
                        Entry(index.toFloat(), entry.weight.toFloat())
                    })
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Key Statistics",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Current", "${displayedEntries.lastOrNull()?.weight ?: 0} kg", Modifier.weight(1f))
                StatCard("Gain", "+0.8 kg", Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        }
    }
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
                    labelCount = entries.size
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
