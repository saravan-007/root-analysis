package com.example.rootanalysis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.PersonAdd
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
fun CreateNewCaseScreen(
    patients: List<Patient> = patientsList,
    onBackClick: () -> Unit = {},
    onContinueClick: (Patient) -> Unit = {},
    onAddNewPatientClick: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(value = false) }
    var selectedPatientName by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }
    val isPatientSelected = selectedPatientName.isNotEmpty()

    val filteredPatients = if (searchText.isEmpty()) {
        patients
    } else {
        patients.filter {
            it.name.contains(searchText, ignoreCase = true) || 
            it.id.contains(searchText, ignoreCase = true) 
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Case", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                .padding(24.dp)
        ) {
            // Case Creation Steps Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F9FB))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Case Creation Steps",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    StepItem(1, "Select Patient", isActive = true)
                    StepItem(2, "Clinical Data Entry")
                    StepItem(3, "Tooth Selection")
                    StepItem(4, "Upload Radiograph")
                    StepItem(5, "AI Analysis")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Select Patient",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            // Patient Selector
            Box(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    shadowElevation = 0.5.dp
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isPatientSelected) selectedPatientName else "Choose a patient.",
                            color = if (isPatientSelected) Color(0xFF0F172A) else Color(0xFF64748B)
                        )
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color(0xFF64748B)
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { 
                        expanded = false
                        searchText = "" 
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(Color.White)
                ) {
                    // Search field inside dropdown
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        placeholder = { Text("Search name or ID...", fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedBorderColor = Color(0xFF1396B2),
                            unfocusedContainerColor = Color(0xFFF8FAFC),
                            focusedContainerColor = Color(0xFFF8FAFC)
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    Box(modifier = Modifier.heightIn(max = 300.dp)) {
                        Column {
                            if (filteredPatients.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No patients found", color = Color.Red, fontSize = 14.sp) },
                                    onClick = { },
                                    enabled = false
                                )
                            } else {
                                filteredPatients.forEach { patient ->
                                    DropdownMenuItem(
                                        text = { 
                                            Column {
                                                Text(patient.name, fontWeight = FontWeight.Bold)
                                                Text(patient.id, fontSize = 12.sp, color = Color(0xFF1396B2))
                                            }
                                        },
                                        onClick = {
                                            selectedPatientName = patient.name
                                            expanded = false
                                            searchText = ""
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Or",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color(0xFF94A3B8),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add New Patient Button
            OutlinedButton(
                onClick = onAddNewPatientClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(0.5.dp, Color(0xFFE2E8F0)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0F172A))
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Add New Patient", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Continue Button
            Button(
                onClick = {
                    val chosen = patients.find { it.name == selectedPatientName }
                    if (chosen != null) {
                        onContinueClick(chosen)
                    }
                },
                enabled = isPatientSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1396B2),
                    disabledContainerColor = Color(0xFF1396B2).copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Continue to Clinical Data", fontWeight = FontWeight.Bold, color = if (isPatientSelected) Color.White else Color.White.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp), tint = if (isPatientSelected) Color.White else Color.White.copy(alpha = 0.6f))
                }
            }
        }
    }
}

@Composable
fun StepItem(number: Int, label: String, isActive: Boolean = false) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isActive) Color(0xFF1396B2) else Color(0xFFCBD5E1)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = if (isActive) Color(0xFF64748B) else Color(0xFF94A3B8),
            fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateNewCasePreview() {
    CreateNewCaseScreen()
}
