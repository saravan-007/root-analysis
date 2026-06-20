package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareReportScreen(
    onBackClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Share Report", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            // Share Link Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Link, contentDescription = null, tint = Color(0xFF1396B2), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Share Link", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Anyone with this link can view the report", fontSize = 14.sp, color = Color(0xFF64748B))
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = "https://rootresorption.app/reports/1",
                        onValueChange = { },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color(0xFF64748B))
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            unfocusedContainerColor = Color(0xFFF8FAFC)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Share via Email Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF1396B2), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Share via Email", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Recipient Email", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("colleague@example.com", color = Color(0xFF94A3B8)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            unfocusedContainerColor = Color(0xFFF1F5F9).copy(alpha = 0.5f)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Message (Optional)", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        placeholder = { Text("Add a personal message...", color = Color(0xFF94A3B8)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            unfocusedContainerColor = Color(0xFFF1F5F9).copy(alpha = 0.5f)
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Send Email", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Share Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.Message, contentDescription = null, tint = Color(0xFF1396B2), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Quick Share", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        QuickShareItem(Icons.AutoMirrored.Filled.Message, "WhatsApp", Color(0xFFEBF5FF), Color(0xFF1D4ED8), modifier = Modifier.weight(1f))
                        QuickShareItem(Icons.Default.Email, "Messages", Color(0xFFF0FDF4), Color(0xFF15803D), modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Privacy Notice
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFEF3C7))
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Privacy Notice: ")
                        addStyle(
                            style = androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF92400E)),
                            start = 0,
                            end = 16
                        )
                        append("Shared reports contain sensitive patient data. Ensure you have proper consent before sharing.")
                    },
                    modifier = Modifier.padding(16.dp),
                    fontSize = 13.sp,
                    color = Color(0xFF92400E),
                    lineHeight = 20.sp
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun QuickShareItem(icon: ImageVector, label: String, bg: Color, tint: Color, modifier: Modifier) {
    OutlinedButton(
        onClick = { },
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = label, fontSize = 14.sp, color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
        }
    }
}

private fun buildAnnotatedString(block: androidx.compose.ui.text.AnnotatedString.Builder.() -> Unit) =
    androidx.compose.ui.text.AnnotatedString.Builder().apply(block).toAnnotatedString()

@Preview(showBackground = true)
@Composable
fun ShareReportPreview() {
    ShareReportScreen()
}
