package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Shield
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
fun SettingsScreen(
    doctor: com.example.rootanalysis.network.Doctor? = null,
    onUpdateProfileClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        TopAppBar(
            title = { Text("Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F7F9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF1396B2),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = doctor?.name ?: "Teja",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = doctor?.email ?: "mrking87627@gmail.com",
                                fontSize = 14.sp,
                                color = Color(0xFF64748B)
                            )
                            if (doctor != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "License: ${doctor.medicalLicense}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF1396B2),
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (!doctor.clinic.isNullOrBlank()) {
                                    Text(
                                        text = "Clinic: ${doctor.clinic}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Profile Options
            item {
                AccountActionItem(
                    icon = Icons.Outlined.Edit,
                    title = "Update Profile Information",
                    onClick = onUpdateProfileClick
                )
            }

            item {
                AccountActionItem(
                    icon = Icons.Outlined.Lock,
                    title = "Change Password",
                    onClick = onChangePasswordClick
                )
            }

            item {
                AccountActionItem(
                    icon = Icons.Outlined.PrivacyTip,
                    title = "Privacy Policy",
                    onClick = onPrivacyPolicyClick
                )
            }

            item {
                AccountActionItem(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = "Logout",
                    iconTint = Color.Red,
                    textColor = Color.Red,
                    onClick = onLogoutClick
                )
            }
        }
    }
}

@Composable
fun AccountActionItem(
    icon: ImageVector,
    title: String,
    iconTint: Color = Color(0xFF1396B2),
    textColor: Color = Color(0xFF0F172A),
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 0.5.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconTint.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFCBD5E1),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    SettingsScreen()
}
