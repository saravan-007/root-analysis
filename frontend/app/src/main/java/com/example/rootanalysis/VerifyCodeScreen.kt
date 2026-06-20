package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VerifyCodeScreen(
    onBackClick: () -> Unit = {},
    onVerifyClick: () -> Unit = {},
    onResendClick: () -> Unit = {}
) {
    var code by remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = remember { List(6) { FocusRequester() } }

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
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFF1396B2)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Verify Code",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We've sent a 6-digit verification code to your email address.",
            fontSize = 16.sp,
            color = Color(0xFF64748B),
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // OTP Input Fields
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            code.forEachIndexed { index, digit ->
                OutlinedTextField(
                    value = digit,
                    onValueChange = { newValue ->
                        if (newValue.length <= 1) {
                            val newCode = code.toMutableList()
                            newCode[index] = newValue
                            code = newCode
                            if (newValue.isNotEmpty() && index < 5) {
                                focusRequesters[index + 1].requestFocus()
                            }
                        }
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .focusRequester(focusRequesters[index]),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFF1F5F9),
                        focusedBorderColor = Color(0xFF1396B2),
                        unfocusedContainerColor = Color(0xFFF8FAFC),
                        focusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Didn't receive the code? ",
                color = Color(0xFF64748B),
                fontSize = 14.sp
            )
            TextButton(
                onClick = onResendClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Resend Code",
                    color = Color(0xFF1396B2),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onVerifyClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1396B2).copy(alpha = 0.5f), // Matching the lighter teal in image
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Verify & Continue",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerifyCodeScreenPreview() {
    VerifyCodeScreen()
}
