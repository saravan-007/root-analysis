package com.example.rootanalysis

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import com.example.rootanalysis.network.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onCreateAccountClick: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var medicalLicense by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var isSubmitting by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
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
            text = "Create Account",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Join RootCheck to get started",
            fontSize = 16.sp,
            color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Full Name
        SignUpTextField(
            label = "Full Name",
            value = fullName,
            onValueChange = { fullName = it },
            placeholder = "Dr. John Doe",
            leadingIcon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Medical License
        SignUpTextField(
            label = "Medical License Number",
            value = medicalLicense,
            onValueChange = { medicalLicense = it },
            placeholder = "e.g. LIC-12345-US",
            leadingIcon = Icons.Default.Assignment
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Email Address
        SignUpTextField(
            label = "Email Address",
            value = email,
            onValueChange = { email = it },
            placeholder = "your.email@example.com",
            leadingIcon = Icons.Default.Email,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Password
        SignUpTextField(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            placeholder = "Create a strong password",
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            passwordVisible = passwordVisible,
            onVisibilityToggle = { passwordVisible = !passwordVisible }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Confirm Password
        SignUpTextField(
            label = "Confirm Password",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "Re-enter your password",
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            passwordVisible = confirmPasswordVisible,
            onVisibilityToggle = { confirmPasswordVisible = !confirmPasswordVisible }
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                if (fullName.isBlank() || medicalLicense.isBlank() || email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                isSubmitting = true
                coroutineScope.launch {
                    try {
                        val response = RetrofitClient.apiService.signup(
                            name = fullName,
                            license = medicalLicense,
                            email = email,
                            password = password
                        )
                        if (response.success) {
                            Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                            onCreateAccountClick()
                        } else {
                            Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_LONG).show()
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
                    text = "Create Account",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account? ",
                color = Color(0xFF64748B),
                fontSize = 14.sp
            )
            TextButton(
                onClick = onSignInClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Sign In",
                    color = Color(0xFF1396B2),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun SignUpTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onVisibilityToggle: () -> Unit = {}
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
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = if (isPassword) {
                {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = onVisibilityToggle) {
                        Icon(
                            imageVector = image,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFF1F5F9),
                focusedBorderColor = Color(0xFF1396B2),
                unfocusedContainerColor = Color(0xFFF8FAFC),
                focusedContainerColor = Color(0xFFF8FAFC)
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else keyboardType)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}
