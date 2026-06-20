package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPatientsScreen(
    patients: List<Patient> = patientsList,
    onBackClick: () -> Unit = {},
    onPatientClick: (Patient) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Search Patients", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Search Text Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by name, ID, or phone...", color = Color(0xFF94A3B8)) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(24.dp)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedBorderColor = Color(0xFF1396B2),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Filtered Patients List
            val filteredPatients = patients.filter {
                it.name.contains(searchQuery, ignoreCase = true) || 
                it.id.contains(searchQuery, ignoreCase = true)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredPatients) { patient ->
                    SearchPatientCard(patient, onPatientClick)
                }
            }
        }
    }
}

@Composable
fun SearchPatientCard(patient: Patient, onClick: (Patient) -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(patient) },
        shape = RoundedCornerShape(16.dp),
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
                    text = "Age ${patient.age} • ${patient.activeCases + 1} cases",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "Last visit: ${patient.lastVisit}",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPatientsPreview() {
    SearchPatientsScreen()
}
