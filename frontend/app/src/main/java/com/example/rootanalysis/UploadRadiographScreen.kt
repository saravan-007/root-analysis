package com.example.rootanalysis

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadRadiographScreen(
    patientName: String,
    toothNumber: Int,
    onBackClick: () -> Unit = {},
    onContinueClick: (Uri?) -> Unit = {}
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedImageUri = uri
        }
    )

    val toothName = when(toothNumber) {
        55 -> "Primary Upper Right Second Molar"
        54 -> "Primary Upper Right First Molar"
        53 -> "Primary Upper Right Canine"
        52 -> "Primary Upper Right Lateral Incisor"
        51 -> "Primary Upper Right Central Incisor"
        61 -> "Primary Upper Left Central Incisor"
        62 -> "Primary Upper Left Lateral Incisor"
        63 -> "Primary Upper Left Canine"
        64 -> "Primary Upper Left First Molar"
        65 -> "Primary Upper Left Second Molar"
        75 -> "Primary Lower Left Second Molar"
        74 -> "Primary Lower Left First Molar"
        73 -> "Primary Lower Left Canine"
        72 -> "Primary Lower Left Lateral Incisor"
        71 -> "Primary Lower Left Central Incisor"
        81 -> "Primary Lower Right Central Incisor"
        82 -> "Primary Lower Right Lateral Incisor"
        83 -> "Primary Lower Right Canine"
        84 -> "Primary Lower Right First Molar"
        85 -> "Primary Lower Right Second Molar"
        else -> "Primary Tooth $toothNumber"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Radiograph", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            // Patient Info Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1).copy(alpha = 0.3f)),
                border = BoxDefaults.patientInfoBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Patient: $patientName",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tooth: $toothNumber ($toothName)",
                        fontSize = 14.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Upload Area with Dashed Border
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .drawBehind {
                        val stroke = Stroke(
                            width = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                        drawRoundRect(
                            color = Color(0xFFCBD5E1),
                            style = stroke,
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx())
                        )
                    }
                    .background(Color.White, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                    if (selectedImageUri != null) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F7F9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = Color(0xFF1396B2),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Radiograph Selected!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1396B2),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = selectedImageUri?.lastPathSegment ?: "radiograph_image.png",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B),
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F7F9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = Color(0xFF1396B2),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Upload Radiograph",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Choose a method to upload\nthe radiographic image",
                            fontSize = 14.sp,
                            color = Color(0xFF64748B),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons
            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Select Image File", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onContinueClick(selectedImageUri) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (selectedImageUri != null) "Upload & Run AI Analysis" else "Run AI Analysis (Sample Scan)",
                        fontWeight = FontWeight.Bold, 
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Tip Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFEF3C7))
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Tip: ")
                        addStyle(
                            style = androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF92400E)),
                            start = 0,
                            end = 5
                        )
                        append("Ensure the radiograph is clear, properly oriented, and shows the complete root structure.")
                    },
                    modifier = Modifier.padding(16.dp),
                    fontSize = 13.sp,
                    color = Color(0xFF92400E),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

object BoxDefaults {
    @Composable
    fun patientInfoBorder() = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFB2EBF2).copy(alpha = 0.5f))
}

private fun buildAnnotatedString(block: androidx.compose.ui.text.AnnotatedString.Builder.() -> Unit) =
    androidx.compose.ui.text.AnnotatedString.Builder().apply(block).toAnnotatedString()

@Preview(showBackground = true)
@Composable
fun UploadRadiographPreview() {
    UploadRadiographScreen(patientName = "Emma Rodriguez", toothNumber = 55)
}
