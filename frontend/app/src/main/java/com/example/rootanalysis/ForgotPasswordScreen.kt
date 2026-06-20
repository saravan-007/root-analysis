package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit = {},
    onSendCodeClick: () -> Unit = {},
    onBackToSignInClick: () -> Unit = {},
    showBackToSignIn: Boolean = true
) {
    var email by remember { mutableStateOf("") }

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

        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0F7F9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFF1396B2)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Forgot Password?",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "No worries! Enter your email address and we'll send you a code to reset your password.",
            fontSize = 16.sp,
            color = Color(0xFF64748B),
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

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

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onSendCodeClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Send Reset Code",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (showBackToSignIn) {
            TextButton(
                onClick = onBackToSignInClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Back to Sign In",
                    color = Color(0xFF1396B2),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen()
}
