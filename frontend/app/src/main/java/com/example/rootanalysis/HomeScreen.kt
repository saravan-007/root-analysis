package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onNewCaseClick: () -> Unit = {},
    onAddPatientClick: () -> Unit = {},
    onMyCasesClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        HeaderSection()
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { QuickActionsSection(onNewCaseClick, onAddPatientClick, onMyCasesClick) }
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1E40AF), Color(0xFF1396B2))
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Welcome Back,",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Dr. Johnson",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Wednesday, March 18, 2026",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun QuickActionsSection(
    onNewCaseClick: () -> Unit = {},
    onAddPatientClick: () -> Unit = {},
    onMyCasesClick: () -> Unit = {}
) {
    Column {
        Text(
            text = "Quick Actions",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionButton(
                onClick = onNewCaseClick,
                icon = Icons.Default.Add,
                label = "New Case"
            )
            
            QuickActionButton(
                onClick = onAddPatientClick,
                icon = Icons.Default.PeopleOutline,
                label = "Add Patient"
            )
            
            QuickActionButton(
                onClick = onMyCasesClick,
                icon = Icons.Default.Description,
                label = "My Cases"
            )
        }
    }
}

@Composable
fun QuickActionButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen(onNewCaseClick = {})
}
