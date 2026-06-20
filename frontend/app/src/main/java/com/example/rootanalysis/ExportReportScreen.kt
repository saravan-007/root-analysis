package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Image
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
fun ExportReportScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedFormat by remember { mutableStateOf("PDF") }
    var includeImages by remember { mutableStateOf(true) }
    var includeAnalysis by remember { mutableStateOf(true) }
    var includeRecommendations by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Export Report", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            // Format Selection
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FormatOptionItem(
                    icon = Icons.Default.Description,
                    title = "PDF Document",
                    subtitle = "Best for printing and sharing",
                    selected = selectedFormat == "PDF",
                    onClick = { selectedFormat = "PDF" }
                )
                FormatOptionItem(
                    icon = Icons.Default.Description,
                    title = "Word Document",
                    subtitle = "Editable format",
                    selected = selectedFormat == "Word",
                    onClick = { selectedFormat = "Word" }
                )
                FormatOptionItem(
                    icon = Icons.Default.Image,
                    title = "Images Only",
                    subtitle = "PNG/JPG format",
                    selected = selectedFormat == "Images",
                    onClick = { selectedFormat = "Images" }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Include in Report Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Include in Report",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    IncludeOptionItem(
                        title = "Radiographic Images",
                        subtitle = "Original and annotated radiographs",
                        checked = includeImages,
                        onCheckedChange = { includeImages = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    IncludeOptionItem(
                        title = "AI Analysis Results",
                        subtitle = "Detailed findings and measurements",
                        checked = includeAnalysis,
                        onCheckedChange = { includeAnalysis = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    IncludeOptionItem(
                        title = "Treatment Recommendations",
                        subtitle = "Clinical decision support",
                        checked = includeRecommendations,
                        onCheckedChange = { includeRecommendations = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ExportDetailRow("File Name:", "Case_2026-0001.pdf")
                    ExportDetailRow("Estimated Size:", "2.4 MB")
                    ExportDetailRow("Pages:", "4 pages")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* Handle Export */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export Report", fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FormatOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = if (selected) 1.dp else 0.5.dp,
                color = if (selected) Color(0xFF1396B2) else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) Color(0xFF1396B2) else Color(0xFF64748B),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )
            }
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF1396B2))
            )
        }
    }
}

@Composable
fun IncludeOptionItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.Top) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF1396B2))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            Text(text = subtitle, fontSize = 13.sp, color = Color(0xFF64748B))
        }
    }
}

@Composable
fun ExportDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 15.sp, color = Color(0xFF64748B))
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
    }
}

@Preview(showBackground = true)
@Composable
fun ExportReportPreview() {
    ExportReportScreen()
}
