package com.eliyas.fundmanagementapp.ui.payments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.theme.*

@Composable
fun RecordPaymentModal(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val members by OrganizationRepository.members.collectAsState()

    var selectedMemberId by remember { mutableStateOf(members.firstOrNull()?.id ?: "") }
    var totalReceivedText by remember { mutableStateOf("1500") }
    var paymentMethod by remember { mutableStateOf("bKash") }
    var referenceText by remember { mutableStateOf("TRX889900") }
    var notesText by remember { mutableStateOf("Regular Payment") }

    val selectedMember = members.find { it.id == selectedMemberId }
    val monthlyContrib = selectedMember?.monthlyContribution ?: 1000.0

    val totalReceived = totalReceivedText.toDoubleOrNull() ?: 0.0
    val contribAlloc = minOf(totalReceived, monthlyContrib)
    val donationAlloc = maxOf(0.0, totalReceived - monthlyContrib)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = Strings.btnRecordPayment[lang]!!,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(if (lang == Language.BN) "সদস্য নির্বাচন করুন (২০০+ সদস্য)" else "Select Member (200+ Members)", fontSize = 12.sp, fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = selectedMemberId,
                    onValueChange = { selectedMemberId = it },
                    label = { Text(if (lang == Language.BN) "সদস্য আইডি (যেমন: MEM-1001)" else "Member ID (e.g. MEM-1001)") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (selectedMember != null) {
                    Text(
                        text = "${if (lang == Language.BN) "নাম:" else "Name:"} ${if (lang == Language.BN) selectedMember.nameBn else selectedMember.nameEn} (${Strings.formatCurrency(selectedMember.monthlyContribution, lang)}/মাস)",
                        fontSize = 12.sp,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedTextField(
                    value = totalReceivedText,
                    onValueChange = { totalReceivedText = it },
                    label = { Text(Strings.fieldTotalReceived[lang]!!) },
                    modifier = Modifier.fillMaxWidth()
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = WarmGold.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "${Strings.fieldContributionAllocation[lang]}: ${Strings.formatCurrency(contribAlloc, lang)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen
                        )
                        Text(
                            text = "${Strings.fieldDonationAllocation[lang]}: ${Strings.formatCurrency(donationAlloc, lang)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusSuccess
                        )
                    }
                }

                OutlinedTextField(
                    value = paymentMethod,
                    onValueChange = { paymentMethod = it },
                    label = { Text(Strings.fieldPaymentMethod[lang]!!) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = referenceText,
                    onValueChange = { referenceText = it },
                    label = { Text(if (lang == Language.BN) "রেফারেন্স / ট্রানজেকশন আইডি" else "Reference / TRX ID") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedMemberId.isNotEmpty() && totalReceived > 0) {
                        OrganizationRepository.recordPayment(
                            memberId = selectedMemberId,
                            paymentDate = "2025-02-20",
                            totalReceived = totalReceived,
                            selectedMonths = listOf("2025-01"),
                            contribAmount = contribAlloc,
                            donationAmount = donationAlloc,
                            method = paymentMethod,
                            reference = referenceText,
                            notes = notesText
                        )
                        onSuccess()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(if (lang == Language.BN) "নিশ্চিত করুন" else "Confirm Payment")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Strings.btnCancel[lang]!!)
            }
        }
    )
}
