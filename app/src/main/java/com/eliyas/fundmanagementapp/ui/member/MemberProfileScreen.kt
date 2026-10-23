package com.eliyas.fundmanagementapp.ui.member

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.theme.*

@Composable
fun MemberProfileScreen() {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val currentMember by OrganizationRepository.currentMember.collectAsState()

    val member = currentMember ?: return
    var showChangePasswordModal by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = if (lang == Language.BN) "আমার প্রোফাইল ও অ্যাকাউন্ট তথ্য" else "My Profile & Account Info",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }

        // Digital Membership ID Badge Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(WarmGold),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = member.nameEn.take(1).uppercase(),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkGreen
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = if (lang == Language.BN) member.nameBn else member.nameEn,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SurfaceWhite
                                )
                                Text(
                                    text = "ID: ${member.id} | ${member.designation}",
                                    fontSize = 12.sp,
                                    color = WarmGold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (member.status == "Active") StatusSuccess.copy(alpha = 0.2f) else StatusError.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = member.status,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SurfaceWhite
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = SurfaceWhite.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = if (lang == Language.BN) "মাসিক চাঁদা" else "Monthly Contribution",
                                fontSize = 11.sp,
                                color = SurfaceWhite.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "${Strings.formatCurrency(member.monthlyContribution, lang)}/মাস",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarmGold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (lang == Language.BN) "যোগদানের তারিখ" else "Join Date",
                                fontSize = 11.sp,
                                color = SurfaceWhite.copy(alpha = 0.8f)
                            )
                            Text(
                                text = member.joinDate,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = SurfaceWhite
                            )
                        }
                    }
                }
            }
        }

        // Detailed Contact & Personal Information Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lang == Language.BN) "ব্যক্তিগত ও যোগাযোগের তথ্য" else "Personal & Contact Info",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                    }

                    HorizontalDivider(color = SoftBackground)

                    Text(
                        text = if (lang == Language.BN)
                            "• পূর্ণ নাম (বাংলা): ${member.nameBn}\n• Full Name (EN): ${member.nameEn}\n• মোবাইল নম্বর: ${member.mobile}\n• বিকল্প নম্বর: ${if (member.altMobile.isNotEmpty()) member.altMobile else "N/A"}\n• ইমেইল এড্রেস: ${member.email}\n• পদবী: ${member.designation}\n• বর্তমান ঠিকানা: ${if (member.presentAddress.isNotEmpty()) member.presentAddress else "ঢাকা, বাংলাদেশ"}\n• জরুরী যোগাযোগ: ${if (member.emergencyContact.isNotEmpty()) member.emergencyContact else "N/A"}"
                        else
                            "• Full Name (BN): ${member.nameBn}\n• Full Name (EN): ${member.nameEn}\n• Mobile: ${member.mobile}\n• Alt Mobile: ${if (member.altMobile.isNotEmpty()) member.altMobile else "N/A"}\n• Email: ${member.email}\n• Designation: ${member.designation}\n• Present Address: ${if (member.presentAddress.isNotEmpty()) member.presentAddress else "Dhaka, Bangladesh"}\n• Emergency Contact: ${if (member.emergencyContact.isNotEmpty()) member.emergencyContact else "N/A"}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Account Security & Action Pair
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { showChangePasswordModal = true },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Icon(Icons.Default.LockReset, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (lang == Language.BN) "পাসওয়ার্ড পরিবর্তন করুন" else "Change Password", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showChangePasswordModal) {
        ChangePasswordModal(
            onDismiss = { showChangePasswordModal = false },
            onSuccess = { showChangePasswordModal = false }
        )
    }
}

@Composable
private fun ChangePasswordModal(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (lang == Language.BN) "পাসওয়ার্ড পরিবর্তন করুন" else "Change Account Password",
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = StatusError,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text(if (lang == Language.BN) "বর্তমান পাসওয়ার্ড" else "Current Password") },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text(if (lang == Language.BN) "নতুন পাসওয়ার্ড" else "New Password") },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(if (lang == Language.BN) "নতুন পাসওয়ার্ড নিশ্চিত করুন" else "Confirm New Password") },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newPassword.length < 6) {
                        errorMessage = if (lang == Language.BN) "পাসওয়ার্ড অন্তত ৬ অক্ষরের হতে হবে!" else "Password must be at least 6 characters!"
                    } else if (newPassword != confirmPassword) {
                        errorMessage = if (lang == Language.BN) "নতুন পাসওয়ার্ড মিলছে না!" else "New passwords do not match!"
                    } else {
                        onSuccess()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(if (lang == Language.BN) "আপডেট করুন" else "Update Password")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Strings.btnCancel[lang]!!)
            }
        }
    )
}
