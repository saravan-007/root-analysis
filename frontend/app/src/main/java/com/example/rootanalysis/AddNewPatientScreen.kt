package com.example.rootanalysis

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rootanalysis.network.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewPatientScreen(
    onBackClick: () -> Unit = {},
    onAddPatientClick: (Patient) -> Unit = {}
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var guardianName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var medicalNotes by remember { mutableStateOf("") }

    var genderExpanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Male", "Female", "Other")

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val coroutineScope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current
    var isSubmitting by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val date = datePickerState.selectedDateMillis?.let {
                        val instant = java.time.Instant.ofEpochMilli(it)
                        val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy")
                        java.time.LocalDate.ofInstant(instant, java.time.ZoneId.systemDefault()).format(formatter)
                    } ?: ""
                    dob = date
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Patient", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            PatientTextField(
                label = "First Name",
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = "Enter first name",
                leadingIcon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(16.dp))

            PatientTextField(
                label = "Last Name",
                value = lastName,
                onValueChange = { lastName = it },
                placeholder = "Enter last name"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    PatientTextField(
                        label = "Age",
                        value = age,
                        onValueChange = { age = it },
                        placeholder = "Years",
                        keyboardType = KeyboardType.Number
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Gender",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = !genderExpanded }
                    ) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = { },
                            readOnly = true,
                            placeholder = { Text("Select", color = Color(0xFF94A3B8)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color(0xFF1396B2),
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = genderExpanded,
                            onDismissRequest = { genderExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            genderOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        gender = option
                                        genderExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Date of Birth",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0F172A),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = dob,
                onValueChange = { },
                readOnly = true,
                placeholder = { Text("Select date", color = Color(0xFF94A3B8)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                enabled = false, // Use disabled to make the entire field clickable via modifier above
                leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.Transparent,
                    disabledContainerColor = Color.White,
                    disabledTextColor = Color(0xFF0F172A),
                    disabledLeadingIconColor = Color(0xFF94A3B8),
                    disabledPlaceholderColor = Color(0xFF94A3B8)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            PatientTextField(
                label = "Parent/Guardian Name",
                value = guardianName,
                onValueChange = { guardianName = it },
                placeholder = "Enter guardian name"
            )

            Spacer(modifier = Modifier.height(16.dp))

            PatientTextField(
                label = "Contact Phone",
                value = phone,
                onValueChange = { phone = it },
                placeholder = "+1 (555) 000-0000",
                leadingIcon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(16.dp))

            PatientTextField(
                label = "Email (Optional)",
                value = email,
                onValueChange = { email = it },
                placeholder = "email@example.com",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Medical Notes (Optional)",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0F172A),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = medicalNotes,
                onValueChange = { medicalNotes = it },
                placeholder = { Text("Any relevant medical history or allergies...", color = Color(0xFF94A3B8)) },
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

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (firstName.isBlank() || lastName.isBlank() || age.isBlank() || gender.isBlank()) {
                        Toast.makeText(context, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val patientId = "P-${System.currentTimeMillis().toString().takeLast(4)}"
                    val newPatient = Patient(
                        id = patientId,
                        name = "$firstName $lastName",
                        age = age.toIntOrNull() ?: 0,
                        gender = gender,
                        lastVisit = "Today",
                        activeCases = 0,
                        guardian = guardianName,
                        phone = phone,
                        email = email
                    )
                    isSubmitting = true
                    coroutineScope.launch {
                        try {
                            val created = RetrofitClient.apiService.createPatient(newPatient)
                            Toast.makeText(context, "Patient registered successfully!", Toast.LENGTH_SHORT).show()
                            onAddPatientClick(created)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Connection fallback: saved locally.", Toast.LENGTH_SHORT).show()
                            onAddPatientClick(newPatient)
                        } finally {
                            isSubmitting = false
                        }
                    }
                },
                enabled = !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Add Patient", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PatientTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0F172A),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color(0xFF94A3B8)) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = leadingIcon?.let {
                { Icon(it, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp)) }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color(0xFF1396B2),
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddNewPatientPreview() {
    AddNewPatientScreen()
}
