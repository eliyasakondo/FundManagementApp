package com.eliyas.fundmanagementapp.ui.members

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.model.Member
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.theme.*
import java.util.Locale

@Composable
fun AddMemberModal(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()

    var memberId by remember { mutableStateOf("MEM-${(1000..9999).random()}") }
    var nameBn by remember { mutableStateOf("") }
    var nameEn by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("+8801") }
    var altMobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var designation by remember { mutableStateOf("সাধারণ সদস্য") }
    var monthlyRateText by remember { mutableStateOf("1000") }
    var initialPassword by remember { mutableStateOf("Pass1234") }
    var presentAddress by remember { mutableStateOf("ঢাকা, বাংলাদেশ") }
    var emergencyContact by remember { mutableStateOf("+8801700000000") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Strings.btnAddMember[lang]!!,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    fontSize = 18.sp
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = TextSecondary)
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 450.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = if (lang == Language.BN) "নতুন সদস্য অ্যাকাউন্ট ও তথ্য পূরণ করুন" else "Fill Member Account & Personal Info",
                        fontSize = 12.sp,
                        color = WarmGold,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    OutlinedTextField(
                        value = memberId,
                        onValueChange = { memberId = it },
                        label = { Text(if (lang == Language.BN) "সদস্য আইডি (Member ID)" else "Member ID") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = nameBn,
                        onValueChange = { nameBn = it },
                        label = { Text(if (lang == Language.BN) "পূর্ণ নাম (বাংলা)" else "Full Name (Bangla)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = nameEn,
                        onValueChange = { nameEn = it },
                        label = { Text(if (lang == Language.BN) "পূর্ণ নাম (ইংরেজি)" else "Full Name (English)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = mobile,
                        onValueChange = { mobile = it },
                        label = { Text(if (lang == Language.BN) "মোবাইল নম্বর (+880)" else "Mobile Number (+880)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = altMobile,
                        onValueChange = { altMobile = it },
                        label = { Text(if (lang == Language.BN) "বিকল্প নম্বর" else "Alternative Mobile") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(if (lang == Language.BN) "ইমেইল এড্রেস" else "Email Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = monthlyRateText,
                        onValueChange = { monthlyRateText = it },
                        label = { Text(if (lang == Language.BN) "মাসিক চাঁদার পরিমাণ (৳500-৳3000)" else "Monthly Contribution Rate (৳)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = designation,
                        onValueChange = { designation = it },
                        label = { Text(if (lang == Language.BN) "সংগঠনের পদবী" else "Organization Designation") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = initialPassword,
                        onValueChange = { initialPassword = it },
                        label = { Text(if (lang == Language.BN) "প্রাথমিক পাসওয়ার্ড (১ম লগইনে পরিবর্তন)" else "Initial Password") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = presentAddress,
                        onValueChange = { presentAddress = it },
                        label = { Text(if (lang == Language.BN) "বর্তমান ঠিকানা" else "Present Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = emergencyContact,
                        onValueChange = { emergencyContact = it },
                        label = { Text(if (lang == Language.BN) "জরুরী যোগাযোগ নম্বর" else "Emergency Contact") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val rate = monthlyRateText.toDoubleOrNull() ?: 1000.0
                    if (nameEn.isNotEmpty() || nameBn.isNotEmpty()) {
                        val newMember = Member(
                            id = memberId,
                            username = memberId.lowercase(Locale.ROOT),
                            nameEn = if (nameEn.isNotEmpty()) nameEn else "New Member",
                            nameBn = if (nameBn.isNotEmpty()) nameBn else "নতুন সদস্য",
                            mobile = mobile,
                            altMobile = altMobile,
                            email = if (email.isNotEmpty()) email else "${memberId.lowercase(Locale.ROOT)}@org.bd",
                            designation = designation,
                            monthlyContribution = rate,
                            joinDate = "2025-02-20",
                            presentAddress = presentAddress,
                            emergencyContact = emergencyContact,
                            password = initialPassword,
                            status = "Active"
                        )
                        OrganizationRepository.addMember(newMember)
                        onSuccess()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(if (lang == Language.BN) "সদস্য অ্যাকাউন্ট তৈরি করুন" else "Create Member Account")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Strings.btnCancel[lang]!!)
            }
        }
    )
}
