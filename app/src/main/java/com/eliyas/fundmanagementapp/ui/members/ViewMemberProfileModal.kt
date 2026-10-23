package com.eliyas.fundmanagementapp.ui.members

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.model.Member
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.common.StatusBadge
import com.eliyas.fundmanagementapp.ui.theme.*

@Composable
fun ViewMemberProfileModal(
    member: Member,
    onDismiss: () -> Unit
) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val memberHistoryMap by OrganizationRepository.memberHistory.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val history = memberHistoryMap[member.id] ?: emptyList()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (lang == Language.BN) "সদস্য প্রোফাইল ও চাঁদার খাতা" else "Member Profile & Payment Ledger",
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    fontSize = 17.sp
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = TextSecondary)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Member Header Card
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
                            text = "ID: ${member.id} | ${member.designation} | ${Strings.formatCurrency(member.monthlyContribution, lang)}/মাস",
                            fontSize = 12.sp,
                            color = DarkGreen
                        )
                    }
                }

                // Tab Row (Profile Info vs Monthly History)
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = SoftBackground,
                    contentColor = PrimaryGreen,
                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = {
                            Text(
                                if (lang == Language.BN) "প্রোফাইল তথ্য" else "Profile Info",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = {
                            Text(
                                if (lang == Language.BN) "মাসিক চাঁদার খাতা" else "Monthly Ledger",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 360.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (selectedTabIndex == 0) {
                        // Profile Info Details
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
                    } else {
                        // Month-by-Month Payment History
                        items(history) { record ->
                            val dueAmount = maxOf(0.0, record.requiredAmount - record.paidAmount)

                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = SoftBackground),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = Strings.getMonthName(record.monthKey, lang),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        StatusBadge(status = record.status, lang = lang)
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${if (lang == Language.BN) "ধার্য:" else "Due:"} ${Strings.formatCurrency(record.requiredAmount, lang)}",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                        Text(
                                            text = "${if (lang == Language.BN) "জমা:" else "Paid:"} ${Strings.formatCurrency(record.paidAmount, lang)}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StatusSuccess
                                        )
                                        if (dueAmount > 0) {
                                            Text(
                                                text = "${if (lang == Language.BN) "বকেয়া:" else "Owed:"} ${Strings.formatCurrency(dueAmount, lang)}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = StatusError
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
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
