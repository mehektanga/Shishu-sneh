package com.example.shishu_sneh_healthcare.presentation.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.shishu_sneh_healthcare.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    navController: NavHostController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val pages = listOf(
        OnboardingPage(
            title = "Track Your Baby's Growth",
            description = "Monitor weight, height and head circumference with easy-to-read charts.",
            icon = "📈"
        ),
        OnboardingPage(
            title = "Never Miss a Vaccine",
            description = "Get timely reminders for your baby's vaccination schedule.",
            icon = "💉"
        ),
        OnboardingPage(
            title = "Expert Tips, Every Week",
            description = "Receive age-appropriate feeding guides and developmental milestone alerts.",
            icon = "🤱"
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Skip Button
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = {
                viewModel.completeOnboarding()
                navController.navigate(Screen.Login.route) { 
                    popUpTo(Screen.Onboarding.route) { inclusive = true } 
                }
            }) {
                Text(text = "Skip", color = MaterialTheme.colorScheme.primary)
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(8f)
        ) { position ->
            PagerScreen(onboardingPage = pages[position])
        }

        // Bottom Navigation Section
        Column(
            modifier = Modifier.weight(2.5f).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Indicator
            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pages.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (pagerState.currentPage == iteration) 12.dp else 8.dp)
                            .background(color, MaterialTheme.shapes.small)
                    )
                }
            }

            NavigationButtons(
                pagerState = pagerState,
                totalTabs = pages.size,
                onNextClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                onFinishClick = {
                    viewModel.completeOnboarding()
                    navController.navigate(Screen.Login.route) { 
                        popUpTo(Screen.Onboarding.route) { inclusive = true } 
                    }
                }
            )
        }
    }
}

@Composable
fun PagerScreen(onboardingPage: OnboardingPage) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(280.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = onboardingPage.icon,
                    fontSize = 100.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = onboardingPage.title,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = onboardingPage.description,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = 40.dp)
        )
    }
}

@Composable
fun NavigationButtons(
    pagerState: PagerState,
    totalTabs: Int,
    onNextClick: () -> Unit,
    onFinishClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
        if (pagerState.currentPage < totalTabs - 1) {
            Button(
                onClick = onNextClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Next", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Button(
                onClick = onFinishClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Get Started", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: String
)
