package com.eliyas.fundmanagementapp.ui.payments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
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
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.theme.*

@Composable
fun PaymentsScreen(onOpenRecordPayment: () -> Unit) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val receipts by OrganizationRepository.paymentReceipts.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") }

    val totalCollected = receipts.sumOf { it.totalReceived }
    val totalContribAlloc = receipts.sumOf { it.contributionAmount }
    val totalDonationAlloc = receipts.sumOf { it.donationAmount }

    val filteredReceipts = receipts.filter { receipt ->
        (receipt.memberNameEn.contains(searchQuery, ignoreCase = true) ||
         receipt.memberNameBn.contains(searchQuery, ignoreCase = true) ||
         receipt.memberId.contains(searchQuery, ignoreCase = true) ||
         receipt.receiptNo.contains(searchQuery, ignoreCase = true) ||
         receipt.reference.contains(searchQuery, ignoreCase = true)) &&
        (selectedFilter == "ALL" || (selectedFilter == "APPROVED" && receipt.status == "Approved") || (selectedFilter == "PENDING" && receipt.status == "Pending"))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (lang == Language.BN) "পেমেন্ট ও চাঁদা আদায় রেজিস্টার" else "Payments & Receipts Register",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                    Text(
                        text = if (lang == Language.BN) "ম্যানুয়াল রসিদ, অতিরিক্ত অনুদান ও এপ্রুভাল ট্র্যাকিং" else "Manage Offline Payment Receipts & Extra Donations",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = onOpenRecordPayment,
                    colors = ButtonDefaults.buttonColors(containerColor = WarmGold, contentColor = DarkGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(Strings.btnRecordPayment[lang]!!, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (lang == Language.BN) "মোট আদায়কৃত তহবিল" else "Total Collected Funds",
                        fontSize = 12.sp,
                        color = WarmGold
                    )
                    Text(
                        text = Strings.formatCurrency(totalCollected, lang),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurfaceWhite
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = if (lang == Language.BN) "মাসিক চাঁদা খাত:" else "Contribution Portion:",
                                fontSize = 11.sp,
                                color = SurfaceWhite.copy(alpha = 0.8f)
                            )
                            Text(
                                text = Strings.formatCurrency(totalContribAlloc, lang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = SurfaceWhite
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (lang == Language.BN) "অতিরিক্ত অনুদান খাত:" else "Extra Donation Portion:",
                                fontSize = 11.sp,
                                color = SurfaceWhite.copy(alpha = 0.8f)
                            )
                            Text(
                                text = Strings.formatCurrency(totalDonationAlloc, lang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarmGold
                            )
                        }
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(if (lang == Language.BN) "রসিদ নং, সদস্য নাম, আইডি বা ট্রানজেকশন দিয়ে খুঁজুন..." else "Search by receipt #, member name, ID or TRX...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text(
                text = "${if (lang == Language.BN) "সর্বমোট পেমেন্ট রসিদ" else "Total Payment Receipts"} (${Strings.formatDigits(filteredReceipts.size.toString(), lang)})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }

        items(filteredReceipts) { receipt ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (lang == Language.BN) receipt.memberNameBn else receipt.memberNameEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "ID: ${receipt.memberId} | Receipt: ${receipt.receiptNo}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = Strings.formatCurrency(receipt.totalReceived, lang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = StatusSuccess
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(StatusSuccess.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = receipt.status,
                                    fontSize = 10.sp,
                                    color = StatusSuccess,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = SoftBackground)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${if (lang == Language.BN) "চাঁদা:" else "Contrib:"} ${Strings.formatCurrency(receipt.contributionAmount, lang)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkGreen
                        )

                        if (receipt.donationAmount > 0) {
                            Text(
                                text = "+${if (lang == Language.BN) "অনুদান:" else "Donation:"} ${Strings.formatCurrency(receipt.donationAmount, lang)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarmGold
                            )
                        }

                        Text(
                            text = "${receipt.paymentMethod} (${receipt.paymentDate})",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    if (receipt.coveredMonths.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${if (lang == Language.BN) "পরিশোধিত মাস:" else "Covered Months:"} ${receipt.coveredMonths.joinToString(", ")}",
                            fontSize = 11.sp,
                            color = PrimaryGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
