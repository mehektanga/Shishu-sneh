package com.example.shishu_sneh_healthcare.presentation.profile

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.shishu_sneh_healthcare.presentation.navigation.Screen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    var step by remember { mutableStateOf(1) }
    var motherName by remember { mutableStateOf("") }
    var babyName by remember { mutableStateOf("") }
    var babyGender by remember { mutableStateOf("Girl") }
    var bloodGroup by remember { mutableStateOf("O+") }
    
    // Date Picker State
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val dateFormatted = remember(selectedDate) {
        selectedDate?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
        } ?: ""
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDate = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Setup Profile", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { step.toFloat() / 2f },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            AnimatedContent(targetState = step, label = "SetupStep") { currentStep ->
                if (currentStep == 1) {
                    ParentInfoStep(
                        motherName = motherName,
                        onNameChange = { motherName = it },
                        onNext = { step = 2 }
                    )
                } else {
                    BabyInfoStep(
                        babyName = babyName,
                        onBabyNameChange = { babyName = it },
                        babyDobString = dateFormatted,
                        onOpenDatePicker = { showDatePicker = true },
                        babyGender = babyGender,
                        onGenderChange = { babyGender = it },
                        bloodGroup = bloodGroup,
                        onBloodGroupChange = { bloodGroup = it },
                        onFinish = {
                            viewModel.saveBabyDetails(
                                name = babyName,
                                dob = selectedDate ?: 0L,
                                gender = babyGender,
                                motherName = motherName,
                                bloodGroup = bloodGroup,
                                onSuccess = {
                                    navController.navigate(Screen.Dashboard.route) {
                                        popUpTo(Screen.ProfileSetup.route) { inclusive = true }
                                    }
                                }
                            )
                        },
                        onBack = { step = 1 }
                    )
                }
            }
        }
    }
}

@Composable
fun ParentInfoStep(motherName: String, onNameChange: (String) -> Unit, onNext: () -> Unit) {
    Column {
        Text(text = "Step 1: Guardian Info", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text(text = "Help us personalize your experience", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = motherName,
            onValueChange = onNameChange,
            label = { Text("Mother / Guardian Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onNext,
            enabled = motherName.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Next: Baby Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun BabyInfoStep(
    babyName: String, 
    onBabyNameChange: (String) -> Unit,
    babyDobString: String,
    onOpenDatePicker: () -> Unit,
    babyGender: String,
    onGenderChange: (String) -> Unit,
    bloodGroup: String,
    onBloodGroupChange: (String) -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Step 2: Baby Info", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text(text = "Tell us about your little one", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = babyName,
            onValueChange = onBabyNameChange,
            label = { Text("Baby's Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = babyDobString,
            onValueChange = {},
            readOnly = true,
            label = { Text("Date of Birth") },
            trailingIcon = {
                IconButton(onClick = onOpenDatePicker) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = "Select Date")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(text = "Gender", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilterChip(
                selected = babyGender == "Boy",
                onClick = { onGenderChange("Boy") },
                label = { Text("Boy") }
            )
            Spacer(modifier = Modifier.width(12.dp))
            FilterChip(
                selected = babyGender == "Girl",
                onClick = { onGenderChange("Girl") },
                label = { Text("Girl") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Text(text = "Blood Group", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
           bloodGroups.take(4).forEach { bg ->
               FilterChip(
                   selected = bloodGroup == bg,
                   onClick = { onBloodGroupChange(bg) },
                   label = { Text(bg) }
               )
           }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
           bloodGroups.drop(4).forEach { bg ->
               FilterChip(
                   selected = bloodGroup == bg,
                   onClick = { onBloodGroupChange(bg) },
                   label = { Text(bg) }
               )
           }
        }

        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onFinish,
            enabled = babyName.isNotEmpty() && babyDobString.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Complete Setup", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text("Go Back", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
