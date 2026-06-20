package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun DecisionTreeScreen(
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Decision Tree", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DecisionStep(
                number = 1,
                title = "Root Resorption Detected?",
                result = "Yes - Proceed to classification"
            )
            
            ArrowDivider()
            
            DecisionStep(
                number = 2,
                title = "Determine Resorption Type",
                result = "Physiologic resorption identified"
            )
            
            ArrowDivider()
            
            DecisionStep(
                number = 3,
                title = "Assess Severity Level",
                result = "Grade 2 - Moderate (40% resorption)"
            )
            
            ArrowDivider()
            
            DecisionStep(
                number = 4,
                title = "Check Successor Status",
                result = "Permanent tooth developing normally"
            )
            
            ArrowDivider()
            
            // Final Decision Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22C55E))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF22C55E),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Treatment Decision",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF14532D)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Conservative monitoring recommended with follow-up in 3 months.",
                        fontSize = 15.sp,
                        color = Color(0xFF166534),
                        lineHeight = 22.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DecisionStep(number: Int, title: String, result: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBEAFE))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3B82F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number.toString(),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = result,
                    fontSize = 15.sp,
                    color = Color(0xFF065F46),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ArrowDivider() {
    Icon(
        imageVector = Icons.Default.ArrowDownward,
        contentDescription = null,
        tint = Color(0xFFCBD5E1),
        modifier = Modifier.padding(vertical = 12.dp).size(24.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DecisionTreePreview() {
    DecisionTreeScreen()
}
