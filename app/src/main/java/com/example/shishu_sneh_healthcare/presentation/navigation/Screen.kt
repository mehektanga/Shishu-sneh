package com.example.shishu_sneh_healthcare.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Onboarding : Screen("onboarding_screen")
    object Login : Screen("login_screen")
    object ProfileSetup : Screen("profile_setup_screen")
    object Dashboard : Screen("dashboard_screen")
    object GrowthChart : Screen("growth_chart_screen")
    object Vaccination : Screen("vaccination_screen")
    object Feeding : Screen("feeding_screen")
    object Milestone : Screen("milestone_screen")
    object HealthRecords : Screen("health_records_screen")
    object Medications : Screen("medications_screen")
    object Settings : Screen("settings_screen")
    object Profile : Screen("profile_screen")
}
