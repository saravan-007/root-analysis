package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicalDataEntryScreen(
    onBackClick: () -> Unit = {},
    onContinueClick: (tooth: Int, notes: String) -> Unit = { _, _ -> }
) {
    val symptoms = listOf(
        "Pain or discomfort",
        "Mobility of tooth",
        "Swelling or inflammation",
        "Delayed eruption",
        "Premature eruption",
        "Discoloration",
        "No symptoms (routine check)"
    )
    val selectedSymptoms = remember { mutableStateListOf<String>() }
    var clinicalFindings by remember { mutableStateOf("") }
    var medicalHistory by remember { mutableStateOf("") }
    var selectedTooth by remember { mutableStateOf(55) } // Default molar
    var toothExpanded by remember { mutableStateOf(false) }
    
    val toothOptions = listOf(51, 52, 53, 54, 55, 61, 62, 63, 64, 65, 71, 72, 73, 74, 75, 81, 82, 83, 84, 85)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clinical Data Entry", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = "Select Tooth Number",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(12.dp))
            ExposedDropdownMenuBox(
                expanded = toothExpanded,
                onExpandedChange = { toothExpanded = !toothExpanded }
            ) {
                OutlinedTextField(
                    value = "Tooth $selectedTooth",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = toothExpanded) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color(0xFF1396B2),
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )
                ExposedDropdownMenu(
                    expanded = toothExpanded,
                    onDismissRequest = { toothExpanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    toothOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text("Tooth $option") },
                            onClick = {
                                selectedTooth = option
                                toothExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Select Symptoms",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(12.dp))

            symptoms.forEach { symptom ->
                SymptomCheckboxItem(
                    label = symptom,
                    checked = selectedSymptoms.contains(symptom),
                    onCheckedChange = { isChecked ->
                        if (isChecked) selectedSymptoms.add(symptom)
                        else selectedSymptoms.remove(symptom)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Clinical Findings",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = clinicalFindings,
                onValueChange = { clinicalFindings = it },
                placeholder = { Text("Describe any relevant clinical observations.", color = Color(0xFF94A3B8)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color(0xFF1396B2),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Relevant Medical History",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = medicalHistory,
                onValueChange = { medicalHistory = it },
                placeholder = { Text("Any relevant medical conditions, medications...", color = Color(0xFF94A3B8)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color(0xFF1396B2),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    val combinedNotes = buildString {
                        if (selectedSymptoms.isNotEmpty()) {
                            append("Symptoms: ${selectedSymptoms.joinToString()}; ")
                        }
                        if (clinicalFindings.isNotBlank()) {
                            append("Findings: ${clinicalFindings}; ")
                        }
                        if (medicalHistory.isNotBlank()) {
                            append("History: ${medicalHistory}")
                        }
                    }.trim().removeSuffix(";").removeSuffix(";")
                    onContinueClick(selectedTooth, combinedNotes)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Continue", fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SymptomCheckboxItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .border(0.5.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF1396B2),
                    uncheckedColor = Color(0xFFCBD5E1)
                )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = label, fontSize = 16.sp, color = Color(0xFF475569))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClinicalDataEntryPreview() {
    ClinicalDataEntryScreen()
}
