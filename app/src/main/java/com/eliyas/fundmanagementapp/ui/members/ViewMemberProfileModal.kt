package com.eliyas.fundmanagementapp.ui.members

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun ViewMemberProfileModal(
    member: Member,
    onDismiss: () -> Unit
) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (lang == Language.BN) "সদস্য পূর্ণাঙ্গ প্রোফাইল" else "Full Member Profile",
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
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PrimaryGreen.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (lang == Language.BN) member.nameBn else member.nameEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = PrimaryGreen
                            )
                            Text(
                                text = "ID: ${member.id} | ${member.designation}",
                                fontSize = 12.sp,
                                color = DarkGreen
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = if (lang == Language.BN)
                            "• নাম (বাংলা): ${member.nameBn}\n• Name (English): ${member.nameEn}\n• মোবাইল: ${member.mobile}\n• বিকল্প মোবাইল: ${if (member.altMobile.isNotEmpty()) member.altMobile else "N/A"}\n• ইমেইল: ${member.email}\n• পদবী: ${member.designation}\n• মাসিক চাঁদা: ${Strings.formatCurrency(member.monthlyContribution, lang)}/মাস\n• যোগদানের তারিখ: ${member.joinDate}\n• বর্তমান ঠিকানা: ${if (member.presentAddress.isNotEmpty()) member.presentAddress else "ঢাকা, বাংলাদেশ"}\n• জরুরী যোগাযোগ: ${if (member.emergencyContact.isNotEmpty()) member.emergencyContact else "N/A"}\n• অ্যাকাউন্ট স্ট্যাটাস: ${member.status}"
                        else
                            "• Name (BN): ${member.nameBn}\n• Name (EN): ${member.nameEn}\n• Mobile: ${member.mobile}\n• Alt Mobile: ${if (member.altMobile.isNotEmpty()) member.altMobile else "N/A"}\n• Email: ${member.email}\n• Designation: ${member.designation}\n• Monthly Rate: ${Strings.formatCurrency(member.monthlyContribution, lang)}/mo\n• Join Date: ${member.joinDate}\n• Present Address: ${if (member.presentAddress.isNotEmpty()) member.presentAddress else "Dhaka, Bangladesh"}\n• Emergency Contact: ${if (member.emergencyContact.isNotEmpty()) member.emergencyContact else "N/A"}\n• Account Status: ${member.status}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(if (lang == Language.BN) "বন্ধ করুন" else "Close")
            }
        }
    )
}
