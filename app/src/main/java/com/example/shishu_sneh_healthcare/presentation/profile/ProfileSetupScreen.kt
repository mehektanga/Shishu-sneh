package com.example.shishu_sneh_healthcare.presentation.profile

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.shishu_sneh_healthcare.data.local.entity.BabyEntity
import com.example.shishu_sneh_healthcare.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    var step by remember { mutableStateOf(1) }
    
    // Parent Details
    var motherName by remember { mutableStateOf("") }
    
    // Baby Details
    var babyName by remember { mutableStateOf("") }
    var babyDob by remember { mutableStateOf("") }
    var babyGender by remember { mutableStateOf("Girl") }

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
                        babyDob = babyDob,
                        onDobChange = { babyDob = it },
                        babyGender = babyGender,
                        onGenderChange = { babyGender = it },
                        onFinish = {
                            val baby = BabyEntity(
                                name = babyName,
                                dob = 0L, // Logic to parse date would go here
                                gender = babyGender,
                                bloodGroup = "O+",
                                birthWeight = 3.2,
                                birthHeight = 50.0,
                                photoUri = null,
                                motherName = motherName,
                                pediatrician = null,
                                hospital = null,
                                userId = "dummy_user_id"
                            )
                            viewModel.saveBabyDetails(baby) {
                                navController.navigate(Screen.Dashboard.route) {
                                    popUpTo(Screen.ProfileSetup.route) { inclusive = true }
                                }
                            }
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
    babyDob: String,
    onDobChange: (String) -> Unit,
    babyGender: String,
    onGenderChange: (String) -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    Column {
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
            value = babyDob,
            onValueChange = onDobChange,
            label = { Text("Date of Birth (DD/MM/YYYY)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = MaterialTheme.shapes.large
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = babyGender == "Boy", onClick = { onGenderChange("Boy") })
            Text("Boy")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = babyGender == "Girl", onClick = { onGenderChange("Girl") })
            Text("Girl")
        }
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onFinish,
            enabled = babyName.isNotEmpty() && babyDob.isNotEmpty(),
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
