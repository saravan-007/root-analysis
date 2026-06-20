package com.example.rootanalysis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rootanalysis.network.PatientDetailResponse
import com.example.rootanalysis.network.RetrofitClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreen(
    patient: Patient,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onNewCaseClick: () -> Unit = {},
    onFullHistoryClick: () -> Unit = {},
    onCaseClick: (com.example.rootanalysis.network.CaseAnalysisResponse) -> Unit = {},
    showHistory: Boolean = true
) {
    var patientDetails by remember { mutableStateOf<PatientDetailResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(patient.id) {
        isLoading = true
        try {
            val response = RetrofitClient.apiService.getPatientDetails(patient.id)
            patientDetails = response
        } catch (e: Exception) {
            // Fallback or silently fail
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patient Details", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Edit")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Patient Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F7F9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF1396B2),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text(
                                text = patient.name,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "ID: ${patient.id}",
                                fontSize = 14.sp,
                                color = Color(0xFF1396B2),
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${patient.age} years • ${patient.gender}",
                                fontSize = 16.sp,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF0FDF4))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Active Patient",
                                    fontSize = 12.sp,
                                    color = Color(0xFF15803D),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    DetailRow(icon = Icons.Default.CalendarToday, text = "Born: ~${2026 - patient.age}")
                    DetailRow(icon = Icons.Default.Person, text = "Guardian: ${patientDetails?.guardian ?: patient.guardian ?: "N/A"}")
                    DetailRow(icon = Icons.Default.Phone, text = patientDetails?.phone ?: patient.phone ?: "N/A")
                    DetailRow(icon = Icons.Default.Email, text = patientDetails?.email ?: patient.email ?: "N/A")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onNewCaseClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New Case", fontWeight = FontWeight.Bold)
                }
                
                if (showHistory) {
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedButton(
                        onClick = onFullHistoryClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0F172A))
                    ) {
                        Icon(Icons.Outlined.History, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Full History", fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (showHistory) {
                Spacer(modifier = Modifier.height(32.dp))

                // Recent Cases Section
                Text(
                    text = "Recent Cases",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val cases = patientDetails?.cases ?: emptyList()
                        if (cases.isEmpty()) {
                            if (isLoading) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = Color(0xFF1396B2))
                                }
                            } else {
                                Text(
                                    text = "No recorded cases yet.",
                                    color = Color(0xFF64748B),
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(vertical = 16.dp).align(Alignment.CenterHorizontally)
                                )
                            }
                        } else {
                            cases.forEachIndexed { index, case ->
                                val statusBg = when (case.severity.lowercase()) {
                                    "severe" -> Color(0xFFFEF2F2)
                                    "moderate" -> Color(0xFFFFF7ED)
                                    "mild" -> Color(0xFFF0FDF4)
                                    else -> Color(0xFFF1F5F9)
                                }
                                val statusText = when (case.severity.lowercase()) {
                                    "severe" -> Color(0xFFB91C1C)
                                    "moderate" -> Color(0xFFC2410C)
                                    "mild" -> Color(0xFF15803D)
                                    else -> Color(0xFF475569)
                                }
                                val dateStr = try {
                                    case.createdAt.split("T").first()
                                } catch (e: Exception) {
                                    case.createdAt
                                }
                                RecentCaseItem(
                                    title = "Tooth ${case.toothNumber} (${case.toothName})",
                                    date = dateStr,
                                    status = case.severity,
                                    statusBg = statusBg,
                                    statusText = statusText,
                                    onClick = { onCaseClick(case) }
                                )
                                if (index < cases.size - 1) {
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color(0xFFF1F5F9))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF64748B),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 16.sp, color = Color(0xFF475569))
    }
}

@Composable
fun RecentCaseItem(
    title: String, 
    date: String, 
    status: String, 
    statusBg: Color, 
    statusText: Color,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A), fontSize = 16.sp)
            Text(text = date, fontSize = 14.sp, color = Color(0xFF94A3B8))
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(statusBg)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(status, fontSize = 12.sp, color = statusText, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientDetailsPreview() {
    PatientDetailsScreen(
        patient = Patient("P-2026-0001", "Emma Rodriguez", 7, "Female", "Mar 18, 2026", 2)
    )
}
