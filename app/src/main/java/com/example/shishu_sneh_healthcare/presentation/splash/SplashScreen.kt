package com.example.shishu_sneh_healthcare.presentation.splash

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.shishu_sneh_healthcare.ui.theme.LavenderHeader
import com.example.shishu_sneh_healthcare.ui.theme.PinkHeader
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val startDestination by viewModel.startDestination.collectAsState()
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = startDestination) {
        startAnimation = true
        if (startDestination != null) {
            delay(1500) // Reduced from 3000ms for faster start
            navController.popBackStack()
            navController.navigate(startDestination!!)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PinkHeader, LavenderHeader)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = startAnimation,
            enter = fadeIn() + expandVertically()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "👶", fontSize = 64.sp)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Shishu-Sneh",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Your Baby's Guardian Angel",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
