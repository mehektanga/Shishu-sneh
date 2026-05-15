package com.example.shishu_sneh_healthcare.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shishu_sneh_healthcare.presentation.auth.LoginScreen
import com.example.shishu_sneh_healthcare.presentation.dashboard.DashboardScreen
import com.example.shishu_sneh_healthcare.presentation.feeding.FeedingScreen
import com.example.shishu_sneh_healthcare.presentation.growth.GrowthChartScreen
import com.example.shishu_sneh_healthcare.presentation.medication.MedicationScreen
import com.example.shishu_sneh_healthcare.presentation.milestone.MilestoneScreen
import com.example.shishu_sneh_healthcare.presentation.onboarding.OnboardingScreen
import com.example.shishu_sneh_healthcare.presentation.profile.ProfileScreen
import com.example.shishu_sneh_healthcare.presentation.profile.ProfileSetupScreen
import com.example.shishu_sneh_healthcare.presentation.records.HealthRecordsScreen
import com.example.shishu_sneh_healthcare.presentation.settings.SettingsScreen
import com.example.shishu_sneh_healthcare.presentation.splash.SplashScreen
import com.example.shishu_sneh_healthcare.presentation.vaccine.VaccinationScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn(tween(300)) + slideInHorizontally(tween(300)) { 1000 } },
        exitTransition = { fadeOut(tween(300)) + slideOutHorizontally(tween(300)) { -1000 } },
        popEnterTransition = { fadeIn(tween(300)) + slideInHorizontally(tween(300)) { -1000 } },
        popExitTransition = { fadeOut(tween(300)) + slideOutHorizontally(tween(300)) { 1000 } }
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.ProfileSetup.route) {
            ProfileSetupScreen(navController = navController)
        }
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(
            route = Screen.GrowthChart.route + "/{babyId}",
            arguments = listOf(navArgument("babyId") { type = NavType.LongType })
        ) { backStackEntry ->
            val babyId = backStackEntry.arguments?.getLong("babyId") ?: -1L
            GrowthChartScreen(babyId = babyId, onBackClick = { navController.popBackStack() })
        }
        composable(
            route = Screen.Vaccination.route + "/{babyId}",
            arguments = listOf(navArgument("babyId") { type = NavType.LongType })
        ) { backStackEntry ->
            val babyId = backStackEntry.arguments?.getLong("babyId") ?: -1L
            VaccinationScreen(babyId = babyId, onBackClick = { navController.popBackStack() })
        }
        composable(
            route = Screen.Feeding.route + "/{babyId}",
            arguments = listOf(navArgument("babyId") { type = NavType.LongType })
        ) { backStackEntry ->
            val babyId = backStackEntry.arguments?.getLong("babyId") ?: -1L
            FeedingScreen(babyId = babyId, onBackClick = { navController.popBackStack() })
        }
        composable(
            route = Screen.Milestone.route + "/{babyId}",
            arguments = listOf(navArgument("babyId") { type = NavType.LongType })
        ) { backStackEntry ->
            val babyId = backStackEntry.arguments?.getLong("babyId") ?: -1L
            MilestoneScreen(babyId = babyId, onBackClick = { navController.popBackStack() })
        }
        composable(
            route = Screen.HealthRecords.route + "/{babyId}",
            arguments = listOf(navArgument("babyId") { type = NavType.LongType })
        ) { backStackEntry ->
            val babyId = backStackEntry.arguments?.getLong("babyId") ?: -1L
            HealthRecordsScreen(babyId = babyId, onBackClick = { navController.popBackStack() })
        }
        composable(
            route = Screen.Medications.route + "/{babyId}",
            arguments = listOf(navArgument("babyId") { type = NavType.LongType })
        ) { backStackEntry ->
            val babyId = backStackEntry.arguments?.getLong("babyId") ?: -1L
            MedicationScreen(babyId = babyId, onBackClick = { navController.popBackStack() })
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController, onBackClick = { navController.popBackStack() })
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController, onBackClick = { navController.popBackStack() })
        }
    }
}
