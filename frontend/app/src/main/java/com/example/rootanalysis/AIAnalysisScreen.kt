package com.example.rootanalysis

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rootanalysis.network.CaseAnalysisResponse
import com.example.rootanalysis.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun AIAnalysisScreen(
    patientId: String,
    toothNumber: Int,
    clinicalNotes: String,
    doctorId: Int?,
    imageUri: Uri?,
    onAnalysisComplete: (CaseAnalysisResponse) -> Unit = {},
    onAnalysisFailed: () -> Unit = {}
) {
    val context = LocalContext.current
    var progress by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 2000),
        label = "progress"
    )

    var statusText by remember { mutableStateOf("Uploading radiograph scan...") }

    LaunchedEffect(Unit) {
        // Animate fake progress to 90% while uploading/analyzing
        launch {
            progress = 0.4f
            delay(1000)
            statusText = "Processing pixels..."
            progress = 0.75f
            delay(1500)
            statusText = "Running PyTorch CNN inference..."
            progress = 0.9f
        }

        try {
            val patientIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), patientId)
            val toothNumberBody = RequestBody.create("text/plain".toMediaTypeOrNull(), toothNumber.toString())
            val toothNameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), getToothName(toothNumber))
            val clinicalNotesBody = RequestBody.create("text/plain".toMediaTypeOrNull(), clinicalNotes)
            val doctorIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), (doctorId ?: 1).toString())

            // Get file part
            val filePart: MultipartBody.Part = if (imageUri != null) {
                uriToMultipartBodyPart(context, imageUri, "file")
            } else {
                val fallbackFile = createFallbackImageFile(context)
                val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), fallbackFile)
                MultipartBody.Part.createFormData("file", fallbackFile.name, requestFile)
            }

            val result = RetrofitClient.apiService.analyzeRadiograph(
                patientId = patientIdBody,
                toothNumber = toothNumberBody,
                toothName = toothNameBody,
                clinicalNotes = clinicalNotesBody,
                doctorId = doctorIdBody,
                file = filePart
            )

            statusText = "Compiling clinical findings..."
            progress = 1.0f
            delay(800)
            
            onAnalysisComplete(result)
        } catch (e: Exception) {
            Toast.makeText(context, "Analysis failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            onAnalysisFailed()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F9FB))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(2.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0F7F9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = null,
                tint = Color(0xFF1396B2),
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "AI Analysis in Progress",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = statusText,
            fontSize = 18.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = Color(0xFF1396B2),
                trackColor = Color(0xFFE2E8F0),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${(animatedProgress * 100).toInt()}% Complete",
                fontSize = 16.sp,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Text(
                text = "Our AI model is analyzing the radiograph using advanced deep learning algorithms trained on thousands of root resorption cases.",
                modifier = Modifier.padding(20.dp),
                fontSize = 14.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }
    }
}

fun getToothName(toothNumber: Int): String {
    val names = mapOf(
        55 to "Primary Upper Right Second Molar",
        54 to "Primary Upper Right First Molar",
        53 to "Primary Upper Right Canine",
        52 to "Primary Upper Right Lateral Incisor",
        51 to "Primary Upper Right Central Incisor",
        61 to "Primary Upper Left Central Incisor",
        62 to "Primary Upper Left Lateral Incisor",
        63 to "Primary Upper Left Canine",
        64 to "Primary Upper Left First Molar",
        65 to "Primary Upper Left Second Molar",
        75 to "Primary Lower Left Second Molar",
        74 to "Primary Lower Left First Molar",
        73 to "Primary Lower Left Canine",
        72 to "Primary Lower Left Lateral Incisor",
        71 to "Primary Lower Left Central Incisor",
        81 to "Primary Lower Right Central Incisor",
        82 to "Primary Lower Right Lateral Incisor",
        83 to "Primary Lower Right Canine",
        84 to "Primary Lower Right First Molar",
        85 to "Primary Lower Right Second Molar"
    )
    return names[toothNumber] ?: "Primary Tooth $toothNumber"
}

private fun uriToMultipartBodyPart(context: android.content.Context, uri: Uri, paramName: String): MultipartBody.Part {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri) ?: throw IOException("Unable to open input stream")
    val bytes = inputStream.readBytes()
    inputStream.close()
    
    val requestFile = RequestBody.create((contentResolver.getType(uri) ?: "image/*").toMediaTypeOrNull(), bytes)
    
    var fileName = "radiograph.jpg"
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                fileName = it.getString(nameIndex)
            }
        }
    }
    return MultipartBody.Part.createFormData(paramName, fileName, requestFile)
}

private fun createFallbackImageFile(context: android.content.Context): File {
    val file = File(context.cacheDir, "sample_radiograph.jpg")
    if (!file.exists()) {
        try {
            val bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint()
            paint.color = AndroidColor.DKGRAY
            canvas.drawRect(0f, 0f, 400f, 400f, paint)
            paint.color = AndroidColor.LTGRAY
            paint.textSize = 24f
            canvas.drawText("Sample Scan", 120f, 200f, paint)
            
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return file
}

@Preview(showBackground = true)
@Composable
fun AIAnalysisPreview() {
    AIAnalysisScreen(
        patientId = "P-1234",
        toothNumber = 55,
        clinicalNotes = "",
        doctorId = null,
        imageUri = null
    )
}
