package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import android.widget.Toast
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import com.example.rootanalysis.network.Doctor
import com.example.rootanalysis.network.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileScreen(
    doctor: Doctor? = null,
    onBackClick: () -> Unit = {},
    onProfileUpdated: (Doctor) -> Unit = {}
) {
    var name by remember { mutableStateOf(doctor?.name ?: "") }
    var email by remember { mutableStateOf(doctor?.email ?: "") }
    var phone by remember { mutableStateOf(doctor?.phone ?: "") }
    var clinic by remember { mutableStateOf(doctor?.clinic ?: "") }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var isSubmitting by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            Text(text = "Name", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color(0xFF1396B2),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Email Address", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color(0xFF1396B2),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Phone Number", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color(0xFF1396B2),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Clinic Name", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = clinic,
                onValueChange = { clinic = it },
                modifier = Modifier.fillMaxWidth(),
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
                    if (doctor == null) return@Button
                    if (name.isBlank() || email.isBlank()) {
                        Toast.makeText(context, "Name and Email are required", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isSubmitting = true
                    coroutineScope.launch {
                        try {
                            val response = RetrofitClient.apiService.updateProfile(
                                id = doctor.id,
                                name = name,
                                email = email,
                                phone = phone,
                                clinic = clinic
                            )
                            if (response.success) {
                                Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                                val updatedDoctor = Doctor(
                                    id = doctor.id,
                                    name = name,
                                    email = email,
                                    medicalLicense = doctor.medicalLicense,
                                    phone = phone,
                                    clinic = clinic
                                )
                                onProfileUpdated(updatedDoctor)
                                onBackClick()
                            } else {
                                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Failed to update profile: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
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
                    Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateProfilePreview() {
    UpdateProfileScreen()
}
