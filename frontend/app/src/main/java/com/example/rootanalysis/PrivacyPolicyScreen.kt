package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
                text = "Privacy Policy for RootCheck",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Last updated: March 2026",
                fontSize = 14.sp,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            PolicySection(
                title = "1. Information We Collect",
                content = "We collect information you provide directly to us when you create an account, such as your name and email address. We also collect patient information that you input into the system for analysis."
            )
            
            PolicySection(
                title = "2. How We Use Information",
                content = "We use the information we collect to provide, maintain, and improve our services, including the AI-powered radiograph analysis. We do not sell your personal or patient data to third parties."
            )
            
            PolicySection(
                title = "3. Data Security",
                content = "We implement a variety of security measures to maintain the safety of your personal information and patient records. All sensitive data is transmitted via Secure Socket Layer (SSL) technology."
            )
            
            PolicySection(
                title = "4. Your Choices",
                content = "You may update your account information at any time by logging into your account settings. You can also request the deletion of your account and associated data."
            )
        }
    }
}

@Composable
fun PolicySection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            fontSize = 14.sp,
            color = Color(0xFF475569),
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacyPolicyPreview() {
    PrivacyPolicyScreen()
}
