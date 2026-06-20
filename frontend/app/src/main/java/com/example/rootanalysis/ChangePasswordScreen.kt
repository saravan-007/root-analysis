package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    onBackClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var oldPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Password", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            PasswordField(
                label = "Current Password",
                value = oldPassword,
                onValueChange = { oldPassword = it },
                visible = oldPasswordVisible,
                onVisibilityChange = { oldPasswordVisible = !oldPasswordVisible }
            )

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

            Spacer(modifier = Modifier.height(16.dp))

            PasswordField(
                label = "New Password",
                value = newPassword,
                onValueChange = { newPassword = it },
                visible = newPasswordVisible,
                onVisibilityChange = { newPasswordVisible = !newPasswordVisible }
            )

            Spacer(modifier = Modifier.height(24.dp))

            PasswordField(
                label = "Confirm New Password",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                visible = confirmPasswordVisible,
                onVisibilityChange = { confirmPasswordVisible = !confirmPasswordVisible }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { /* Handle password change */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Update Password", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onVisibilityChange: () -> Unit
) {
    Column {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Outlined.Lock, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp))
            },
            trailingIcon = {
                val image = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = onVisibilityChange) {
                    Icon(image, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp))
                }
            },
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color(0xFF1396B2),
                unfocusedContainerColor = Color(0xFFF1F5F9).copy(alpha = 0.5f),
                focusedContainerColor = Color(0xFFF1F5F9).copy(alpha = 0.5f)
            ),
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordPreview() {
    ChangePasswordScreen()
}
