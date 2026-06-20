package com.example.rootanalysis

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rootanalysis.network.Doctor
import com.example.rootanalysis.network.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onBackClick: () -> Unit = {},
    onSignInClick: (Doctor) -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var isSubmitting by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF0F172A)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Welcome Back",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sign in to continue to RootCheck",
            fontSize = 16.sp,
            color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Email Address
        Text(
            text = "Email Address",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0F172A),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Enter your email", color = Color(0xFF94A3B8)) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(20.dp)
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFF1F5F9),
                focusedBorderColor = Color(0xFF1396B2),
                unfocusedContainerColor = Color(0xFFF8FAFC),
                focusedContainerColor = Color(0xFFF8FAFC)
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Password
        Text(
            text = "Password",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0F172A),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Enter your password", color = Color(0xFF94A3B8)) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFF1F5F9),
                focusedBorderColor = Color(0xFF1396B2),
                unfocusedContainerColor = Color(0xFFF8FAFC),
                focusedContainerColor = Color(0xFFF8FAFC)
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = onForgotPasswordClick,
            modifier = Modifier.align(Alignment.End),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Forgot Password?",
                color = Color(0xFF1396B2),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Please fill in email and password", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                isSubmitting = true
                coroutineScope.launch {
                    try {
                        val response = RetrofitClient.apiService.login(email, password)
                        if (response.success && response.id != null) {
                            val doctor = Doctor(
                                id = response.id,
                                name = response.name ?: "",
                                email = response.email ?: "",
                                medicalLicense = response.medicalLicense ?: "",
                                phone = response.phone,
                                clinic = response.clinic
                            )
                            Toast.makeText(context, "Welcome back, ${doctor.name}!", Toast.LENGTH_SHORT).show()
                            onSignInClick(doctor)
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Connection failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
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
                Text(
                    text = "Sign In",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? ",
                color = Color(0xFF64748B),
                fontSize = 14.sp
            )
            TextButton(
                onClick = onSignUpClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Sign Up",
                    color = Color(0xFF1396B2),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
