package com.example.rootanalysis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.net.Uri
import com.example.rootanalysis.network.CaseAnalysisResponse
import com.example.rootanalysis.network.Doctor
import com.example.rootanalysis.network.RetrofitClient
import com.example.rootanalysis.ui.theme.RootAnalysisTheme
import kotlinx.coroutines.launch

enum class Screen {
    Onboarding, Login, SignUp, VerifyCode, ForgotPassword, Home, PatientsList, MyCasesList, SearchPatients, PatientDetails, CreateNewCase, AddNewPatient, ClinicalDataEntry, UploadRadiograph, AIAnalysis, AnalysisSummary, DiagnosisResult, DecisionTree, TreatmentPlan, CaseReport, ShareReport, ExportReport, Settings, UpdateProfile, ChangePassword, PrivacyPolicy, SeverityClassification, RiskIndicators
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RootAnalysisTheme {
                var currentScreen by remember { mutableStateOf(Screen.Onboarding) }
                var selectedPatient by remember { mutableStateOf<Patient?>(null) }
                var forgotPasswordOrigin by remember { mutableStateOf(Screen.Login) }
                var caseReportOrigin by remember { mutableStateOf(Screen.TreatmentPlan) }
                var patientOrigin by remember { mutableStateOf(Screen.Home) }

                var loggedInDoctor by remember { mutableStateOf<Doctor?>(null) }
                var currentCaseAnalysis by remember { mutableStateOf<CaseAnalysisResponse?>(null) }
                var selectedToothNumber by remember { mutableStateOf(55) }
                var clinicalNotesInput by remember { mutableStateOf("") }
                var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

                val patientListState = remember { mutableStateListOf<Patient>() }
                val coroutineScope = rememberCoroutineScope()

                LaunchedEffect(currentScreen) {
                    if (currentScreen in listOf(Screen.Home, Screen.PatientsList, Screen.CreateNewCase, Screen.MyCasesList)) {
                        try {
                            val dbPatients = RetrofitClient.apiService.getPatients(null)
                            patientListState.clear()
                            patientListState.addAll(dbPatients)
                        } catch (e: Exception) {
                            if (patientListState.isEmpty()) {
                                patientListState.addAll(patientsList)
                            }
                        }
                    }
                }

                Scaffold(
                    bottomBar = {
                        if (currentScreen in listOf(Screen.Home, Screen.PatientsList, Screen.MyCasesList, Screen.Settings)) {
                            AppBottomNavigation(
                                currentScreen = currentScreen,
                                onTabSelected = { screen -> currentScreen = screen },
                                onAddMatchClick = { currentScreen = Screen.CreateNewCase }
                            )
                        }
                    }
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        when (currentScreen) {
                            Screen.Onboarding -> {
                                OnboardingScreen {
                                    currentScreen = Screen.Login
                                }
                            }
                            Screen.Login -> {
                                LoginScreen(
                                    onBackClick = { currentScreen = Screen.Onboarding },
                                    onSignUpClick = { currentScreen = Screen.SignUp },
                                    onForgotPasswordClick = { 
                                        forgotPasswordOrigin = Screen.Login
                                        currentScreen = Screen.ForgotPassword 
                                    },
                                    onSignInClick = { doctor ->
                                        loggedInDoctor = doctor
                                        currentScreen = Screen.Home
                                    }
                                )
                            }
                            Screen.SignUp -> {
                                SignUpScreen(
                                    onBackClick = { currentScreen = Screen.Login },
                                    onSignInClick = { currentScreen = Screen.Login },
                                    onCreateAccountClick = { currentScreen = Screen.VerifyCode }
                                )
                            }
                            Screen.ForgotPassword -> {
                                ForgotPasswordScreen(
                                    onBackClick = { currentScreen = forgotPasswordOrigin },
                                    onSendCodeClick = { currentScreen = Screen.VerifyCode },
                                    onBackToSignInClick = { currentScreen = Screen.Login },
                                    showBackToSignIn = forgotPasswordOrigin == Screen.Login
                                )
                            }
                            Screen.VerifyCode -> {
                                VerifyCodeScreen(
                                    onBackClick = { currentScreen = Screen.SignUp },
                                    onVerifyClick = { currentScreen = Screen.Login }
                                )
                            }
                            Screen.Home -> {
                                HomeScreen(
                                    onNewCaseClick = { currentScreen = Screen.CreateNewCase },
                                    onAddPatientClick = { 
                                        patientOrigin = Screen.Home
                                        currentScreen = Screen.PatientsList 
                                    },
                                    onMyCasesClick = { currentScreen = Screen.MyCasesList } 
                                )
                            }
                            Screen.PatientsList -> {
                                PatientsScreen(
                                    patients = patientListState,
                                    onAddNewPatientClick = { 
                                        patientOrigin = Screen.PatientsList
                                        currentScreen = Screen.AddNewPatient 
                                    },
                                    onSearchClick = { currentScreen = Screen.SearchPatients },
                                    onPatientClick = { patient ->
                                        selectedPatient = patient
                                        currentScreen = Screen.PatientDetails
                                    },
                                    showAddButton = true
                                )
                            }
                            Screen.MyCasesList -> {
                                PatientsScreen(
                                    patients = patientListState,
                                    onSearchClick = { currentScreen = Screen.SearchPatients },
                                    onPatientClick = { patient ->
                                        selectedPatient = patient
                                        coroutineScope.launch {
                                            try {
                                                val details = RetrofitClient.apiService.getPatientDetails(patient.id)
                                                val latestCase = details.cases.firstOrNull()
                                                if (latestCase != null) {
                                                    currentCaseAnalysis = latestCase
                                                    caseReportOrigin = Screen.MyCasesList
                                                    currentScreen = Screen.CaseReport
                                                } else {
                                                    patientOrigin = Screen.MyCasesList
                                                    currentScreen = Screen.PatientDetails
                                                }
                                            } catch (e: Exception) {
                                                patientOrigin = Screen.MyCasesList
                                                currentScreen = Screen.PatientDetails
                                            }
                                        }
                                    },
                                    showAddButton = false
                                )
                            }
                            Screen.SearchPatients -> {
                                SearchPatientsScreen(
                                    patients = patientListState,
                                    onBackClick = { currentScreen = Screen.PatientsList },
                                    onPatientClick = { patient ->
                                        selectedPatient = patient
                                        currentScreen = Screen.PatientDetails
                                    }
                                )
                            }
                            Screen.PatientDetails -> {
                                selectedPatient?.let { patient ->
                                    PatientDetailsScreen(
                                        patient = patient,
                                        onBackClick = { currentScreen = Screen.PatientsList },
                                        onNewCaseClick = { currentScreen = Screen.CreateNewCase },
                                        onCaseClick = { case ->
                                            currentCaseAnalysis = case
                                            caseReportOrigin = Screen.PatientDetails
                                            currentScreen = Screen.CaseReport
                                        },
                                        showHistory = patientOrigin != Screen.Home
                                    )
                                }
                            }
                            Screen.CreateNewCase -> {
                                CreateNewCaseScreen(
                                    patients = patientListState,
                                    onBackClick = { currentScreen = Screen.Home },
                                    onContinueClick = { patient ->
                                        selectedPatient = patient
                                        currentScreen = Screen.ClinicalDataEntry
                                    },
                                    onAddNewPatientClick = { 
                                        patientOrigin = Screen.CreateNewCase
                                        currentScreen = Screen.AddNewPatient 
                                    }
                                )
                            }
                            Screen.ClinicalDataEntry -> {
                                ClinicalDataEntryScreen(
                                    onBackClick = { currentScreen = Screen.CreateNewCase },
                                    onContinueClick = { tooth, notes ->
                                        selectedToothNumber = tooth
                                        clinicalNotesInput = notes
                                        currentScreen = Screen.UploadRadiograph
                                    }
                                )
                            }
                            Screen.UploadRadiograph -> {
                                UploadRadiographScreen(
                                    patientName = selectedPatient?.name ?: "",
                                    toothNumber = selectedToothNumber,
                                    onBackClick = { currentScreen = Screen.ClinicalDataEntry },
                                    onContinueClick = { uri ->
                                        selectedImageUri = uri
                                        currentScreen = Screen.AIAnalysis
                                    }
                                )
                            }
                            Screen.AIAnalysis -> {
                                AIAnalysisScreen(
                                    patientId = selectedPatient?.id ?: "",
                                    toothNumber = selectedToothNumber,
                                    clinicalNotes = clinicalNotesInput,
                                    doctorId = loggedInDoctor?.id,
                                    imageUri = selectedImageUri,
                                    onAnalysisComplete = { result ->
                                        currentCaseAnalysis = result
                                        currentScreen = Screen.AnalysisSummary
                                    },
                                    onAnalysisFailed = {
                                        currentScreen = Screen.UploadRadiograph
                                    }
                                )
                            }
                            Screen.AnalysisSummary -> {
                                currentCaseAnalysis?.let { analysis ->
                                    AnalysisSummaryScreen(
                                        analysis = analysis,
                                        onBackClick = { currentScreen = Screen.UploadRadiograph },
                                        onViewFullDiagnosisClick = { currentScreen = Screen.DiagnosisResult }
                                    )
                                }
                            }
                            Screen.DiagnosisResult -> {
                                currentCaseAnalysis?.let { analysis ->
                                    DiagnosisResultScreen(
                                        analysis = analysis,
                                        onBackClick = { currentScreen = Screen.AnalysisSummary },
                                        onSeverityDetailsClick = { currentScreen = Screen.SeverityClassification },
                                        onTreatmentPlanClick = { currentScreen = Screen.TreatmentPlan },
                                        onViewDecisionTreeClick = { currentScreen = Screen.DecisionTree }
                                    )
                                }
                            }
                            Screen.SeverityClassification -> {
                                SeverityClassificationScreen(
                                    onBackClick = { currentScreen = Screen.DiagnosisResult }
                                )
                            }
                            Screen.DecisionTree -> {
                                DecisionTreeScreen(
                                    onBackClick = { currentScreen = Screen.DiagnosisResult }
                                )
                            }
                            Screen.TreatmentPlan -> {
                                currentCaseAnalysis?.let { analysis ->
                                    TreatmentPlanScreen(
                                        analysis = analysis,
                                        onBackClick = { currentScreen = Screen.DiagnosisResult },
                                        onGenerateReportClick = { 
                                            caseReportOrigin = Screen.TreatmentPlan
                                            currentScreen = Screen.CaseReport 
                                        },
                                        onRiskFactorsClick = { currentScreen = Screen.RiskIndicators }
                                    )
                                }
                            }
                            Screen.RiskIndicators -> {
                                RiskIndicatorsScreen(
                                    onBackClick = { currentScreen = Screen.TreatmentPlan }
                                )
                            }
                            Screen.CaseReport -> {
                                currentCaseAnalysis?.let { analysis ->
                                    CaseReportScreen(
                                        patient = selectedPatient,
                                        analysis = analysis,
                                        onBackClick = { currentScreen = caseReportOrigin },
                                        onShareReportClick = { currentScreen = Screen.ShareReport },
                                        onExportPdfClick = { currentScreen = Screen.ExportReport },
                                        onSaveReportClick = { currentScreen = Screen.Home },
                                        showSaveButton = caseReportOrigin == Screen.TreatmentPlan
                                    )
                                }
                            }
                            Screen.ExportReport -> {
                                ExportReportScreen(
                                    onBackClick = { currentScreen = Screen.CaseReport }
                                )
                            }
                            Screen.ShareReport -> {
                                ShareReportScreen(
                                    onBackClick = { currentScreen = Screen.CaseReport }
                                )
                            }
                            Screen.AddNewPatient -> {
                                AddNewPatientScreen(
                                    onBackClick = { currentScreen = patientOrigin },
                                    onAddPatientClick = { newPatient ->
                                        patientListState.add(0, newPatient)
                                        currentScreen = patientOrigin 
                                    }
                                )
                            }
                            Screen.Settings -> {
                                SettingsScreen(
                                    doctor = loggedInDoctor,
                                    onUpdateProfileClick = { currentScreen = Screen.UpdateProfile },
                                    onChangePasswordClick = { currentScreen = Screen.ChangePassword },
                                    onPrivacyPolicyClick = { currentScreen = Screen.PrivacyPolicy },
                                    onLogoutClick = { 
                                        loggedInDoctor = null
                                        currentScreen = Screen.Login 
                                    }
                                )
                            }
                            Screen.UpdateProfile -> {
                                UpdateProfileScreen(
                                    doctor = loggedInDoctor,
                                    onBackClick = { currentScreen = Screen.Settings },
                                    onProfileUpdated = { updatedDoctor ->
                                        loggedInDoctor = updatedDoctor
                                    }
                                )
                            }
                            Screen.ChangePassword -> {
                                ChangePasswordScreen(
                                    onBackClick = { currentScreen = Screen.Settings },
                                    onForgotPasswordClick = { 
                                        forgotPasswordOrigin = Screen.ChangePassword
                                        currentScreen = Screen.ForgotPassword 
                                    }
                                )
                            }
                            Screen.PrivacyPolicy -> {
                                PrivacyPolicyScreen(
                                    onBackClick = { currentScreen = Screen.Settings }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppBottomNavigation(
    currentScreen: Screen,
    onTabSelected: (Screen) -> Unit,
    onAddMatchClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") },
            selected = currentScreen == Screen.Home,
            onClick = { onTabSelected(Screen.Home) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1396B2),
                selectedTextColor = Color(0xFF1396B2),
                indicatorColor = Color(0xFFE0F7F9)
            )
        )
        NavigationBarItem(
            icon = { 
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E40AF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                }
            },
            label = { Text("Add Match") },
            selected = false,
            onClick = onAddMatchClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1396B2),
                selectedTextColor = Color(0xFF1396B2),
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Profile") },
            selected = currentScreen == Screen.Settings,
            onClick = { onTabSelected(Screen.Settings) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1396B2),
                selectedTextColor = Color(0xFF1396B2),
                indicatorColor = Color(0xFFE0F7F9)
            )
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RootAnalysisTheme {
        Greeting("Android")
    }
}
