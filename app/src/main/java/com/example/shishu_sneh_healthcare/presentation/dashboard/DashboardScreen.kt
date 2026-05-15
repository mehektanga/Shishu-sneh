package com.example.shishu_sneh_healthcare.presentation.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.shishu_sneh_healthcare.presentation.navigation.Screen
import com.example.shishu_sneh_healthcare.ui.theme.LavenderHeader
import com.example.shishu_sneh_healthcare.ui.theme.PinkHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavHostController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val selectedBaby by viewModel.selectedBaby.collectAsState()
    val babies by viewModel.babies.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBabies("dummy_user_id")
    }

    Scaffold(
        bottomBar = { 
            DashboardBottomNavigation(
                navController = navController, 
                selectedTab = currentTab, 
                onTabSelected = viewModel::setCurrentTab,
                babyId = selectedBaby?.id ?: -1L
            ) 
        }
    ) { padding ->
        val babyId = selectedBaby?.id ?: -1L
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                DashboardHeader(
                    babyName = selectedBaby?.name ?: "Parent",
                    onProfileClick = { navController.navigate(Screen.Profile.route) }
                )
            }

            if (babies.isNotEmpty()) {
                item {
                    SummaryCardsSection(navController, babyId)
                }

                item {
                    QuickActionsGrid(navController, babyId)
                }

                item {
                    MedicineAlertSection(navController, babyId)
                }

                item {
                    UpcomingAlertsSection()
                }

                item {
                    AIInsightsCard()
                }
            } else {
                item {
                    EmptyDashboardState {
                        navController.navigate(Screen.ProfileSetup.route)
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun EmptyDashboardState(onAddBabyClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "👶", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No babies added yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Add your baby's details to get started with health tracking and insights.",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onAddBabyClick) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Baby Profile")
        }
    }
}


@Composable
fun DashboardHeader(babyName: String, onProfileClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PinkHeader, LavenderHeader)
                ),
                shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
            )
            .padding(24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Hello,",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp
                    )
                    Text(
                        text = "$babyName's Guardian 🍼",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Surface(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .clickable { onProfileClick() },
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "👩", fontSize = 32.sp)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Celebration, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Ananya turned 3 months today! 🎉",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MedicineAlertSection(navController: NavHostController, babyId: Long) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Medicine Reminders",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth().clickable { navController.navigate(Screen.Medications.route + "/$babyId") },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Medication, contentDescription = null, tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Vitamin D3 Drops", fontWeight = FontWeight.Bold)
                    Text(text = "Next dose at 8:00 PM", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                IconButton(onClick = { /* Mark as taken */ }) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun SummaryCardsSection(navController: NavHostController, babyId: Long) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Today's Summary",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            item {
                SummaryCard("Next Vaccine", "5 Days", Icons.Default.Vaccines, MaterialTheme.colorScheme.primary) {
                    navController.navigate(Screen.Vaccination.route + "/$babyId")
                }
            }
            item {
                SummaryCard("Growth", "Healthy", Icons.Default.Timeline, MaterialTheme.colorScheme.secondary) {
                    navController.navigate(Screen.GrowthChart.route + "/$babyId")
                }
            }
            item {
                SummaryCard("Feeding", "2h ago", Icons.Default.ChildCare, MaterialTheme.colorScheme.tertiary) {
                    navController.navigate(Screen.Feeding.route + "/$babyId")
                }
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier.size(width = 150.dp, height = 110.dp),
        shape = RoundedCornerShape(24.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = color.copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                }
            }
            Column {
                Text(text = title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
            }
        }
    }
}

@Composable
fun QuickActionsGrid(navController: NavHostController, babyId: Long) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            QuickActionButton("Log Growth", Icons.Default.Scale, Modifier.weight(1f), MaterialTheme.colorScheme.primaryContainer) {
                navController.navigate(Screen.GrowthChart.route + "/$babyId")
            }
            QuickActionButton("Add Medicine", Icons.Default.AddModerator, Modifier.weight(1f), MaterialTheme.colorScheme.secondaryContainer) {
                navController.navigate(Screen.Medications.route + "/$babyId")
            }
        }
    }
}

@Composable
fun QuickActionButton(label: String, icon: ImageVector, modifier: Modifier, containerColor: Color, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(70.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = containerColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

@Composable
fun UpcomingAlertsSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Upcoming Events",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "DPT Booster Overdue", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                    Text(text = "Scheduled for 2 days ago", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun AIInsightsCard() {
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "AI Health Insight", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Babies usually start smiling socially between 6-8 weeks. Keep engaging with your baby to foster development!",
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun DashboardBottomNavigation(
    navController: NavHostController,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    babyId: Long
) {
    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") },
            selected = selectedTab == 0,
            onClick = { 
                onTabSelected(0)
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Dashboard.route) { inclusive = true }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Timeline, contentDescription = null) },
            label = { Text("Growth") },
            selected = selectedTab == 1,
            onClick = { 
                onTabSelected(1)
                if (babyId != -1L) {
                    navController.navigate(Screen.GrowthChart.route + "/$babyId")
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.History, contentDescription = null) },
            label = { Text("Records") },
            selected = selectedTab == 2,
            onClick = { 
                onTabSelected(2)
                if (babyId != -1L) {
                    navController.navigate(Screen.HealthRecords.route + "/$babyId")
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text("Settings") },
            selected = selectedTab == 3,
            onClick = { 
                onTabSelected(3)
                navController.navigate(Screen.Settings.route)
            }
        )
    }
}
