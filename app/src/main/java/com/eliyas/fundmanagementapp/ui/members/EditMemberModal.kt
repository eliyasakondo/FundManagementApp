package com.eliyas.fundmanagementapp.ui.members

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.model.Member
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.theme.*

@Composable
fun EditMemberModal(
    member: Member,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()

    var nameBn by remember { mutableStateOf(member.nameBn) }
    var nameEn by remember { mutableStateOf(member.nameEn) }
    var mobile by remember { mutableStateOf(member.mobile) }
    var altMobile by remember { mutableStateOf(member.altMobile) }
    var email by remember { mutableStateOf(member.email) }
    var designation by remember { mutableStateOf(member.designation) }
    var presentAddress by remember { mutableStateOf(member.presentAddress) }
    var emergencyContact by remember { mutableStateOf(member.emergencyContact) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (lang == Language.BN) "সদস্য তথ্য এডিট করুন" else "Edit Member Information",
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen,
                fontSize = 18.sp
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
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
                        label = { Text(if (lang == Language.BN) "মোবাইল নম্বর" else "Mobile Number") },
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
                        value = designation,
                        onValueChange = { designation = it },
                        label = { Text(if (lang == Language.BN) "সংগঠনের পদবী" else "Designation") },
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
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = member.copy(
                        nameBn = nameBn,
                        nameEn = nameEn,
                        mobile = mobile,
                        altMobile = altMobile,
                        email = email,
                        designation = designation,
                        presentAddress = presentAddress,
                        emergencyContact = emergencyContact
                    )
                    OrganizationRepository.updateMemberProfile(updated)
                    onSuccess()
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(if (lang == Language.BN) "আপডেট করুন" else "Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Strings.btnCancel[lang]!!)
            }
        }
    )
}
