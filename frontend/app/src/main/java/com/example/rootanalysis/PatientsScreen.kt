package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.google.gson.annotations.SerializedName

data class Patient(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    @SerializedName("last_visit") val lastVisit: String,
    @SerializedName("active_cases") val activeCases: Int,
    val guardian: String? = null,
    val phone: String? = null,
    val email: String? = null
)

val patientsList = listOf(
    Patient("P-2026-0001", "Emma Rodriguez", 7, "Female", "Mar 18, 2026", 2),
    Patient("P-2026-0002", "Noah Chen", 6, "Male", "Mar 18, 2026", 1),
    Patient("P-2026-0003", "Olivia Martinez", 8, "Female", "Mar 17, 2026", 3),
    Patient("P-2026-0004", "Liam Johnson", 7, "Male", "Mar 17, 2026", 0),
    Patient("P-2026-0005", "Sophia Williams", 6, "Female", "Mar 16, 2026", 1),
    Patient("P-2026-0006", "Jackson Brown", 8, "Male", "Mar 16, 2026", 2)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientsScreen(
    patients: List<Patient> = patientsList,
    onAddNewPatientClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onPatientClick: (Patient) -> Unit = {},
    showAddButton: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Custom Top App Bar
        TopAppBar(
            title = { Text("Patients", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(Icons.Outlined.Search, contentDescription = "Search")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (showAddButton) {
                item {
                    Button(
                        onClick = onAddNewPatientClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add New Patient", fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(patients) { patient ->
                PatientCard(patient, onPatientClick)
            }
        }
    }
}

@Composable
fun PatientCard(patient: Patient, onClick: (Patient) -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(patient) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0F7F9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF1396B2),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = patient.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = patient.id,
                    fontSize = 13.sp,
                    color = Color(0xFF1396B2),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${patient.age} years • ${patient.gender}",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "Last visit: ${patient.lastVisit}",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            if (patient.activeCases > 0) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF0FDF4))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${patient.activeCases} active",
                        fontSize = 12.sp,
                        color = Color(0xFF1396B2),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientsScreenPreview() {
    PatientsScreen()
}
