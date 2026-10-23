package com.eliyas.fundmanagementapp.ui.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.common.MonthRangeFilterCard
import com.eliyas.fundmanagementapp.ui.theme.*

@Composable
fun ReportsScreen() {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val members by OrganizationRepository.members.collectAsState()
    val receipts by OrganizationRepository.paymentReceipts.collectAsState()

    var startMonth by remember { mutableStateOf("2025-01") }
    var endMonth by remember { mutableStateOf("2025-12") }

    val filteredReceipts = receipts.filter { receipt ->
        val covered = receipt.coveredMonths
        if (covered.isEmpty()) true
        else {
            val minCovered = covered.minOrNull() ?: "2025-01"
            val maxCovered = covered.maxOrNull() ?: "2025-12"
            minCovered >= startMonth && maxCovered <= endMonth
        }
    }

    val totalReceipts = filteredReceipts.size
    val totalCollected = filteredReceipts.sumOf { it.totalReceived }
    val totalContrib = filteredReceipts.sumOf { it.contributionAmount }
    val totalDonation = filteredReceipts.sumOf { it.donationAmount }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (lang == Language.BN) "আর্থিক রিপোর্ট ও কন্ট্রিবিউশন লেজার" else "Financial Reports & Ledger Matrix",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                    Text(
                        text = if (lang == Language.BN) "মাসভিত্তিক কাস্টম সময়কালের রিপোর্ট ও বিবরণী" else "Custom month range contribution matrix",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (lang == Language.BN) "এক্সপোর্ট" else "Export", fontSize = 12.sp)
                }
            }
        }

        item {
            MonthRangeFilterCard(
                startMonth = startMonth,
                endMonth = endMonth,
                onRangeSelected = { s, e ->
                    startMonth = s
                    endMonth = e
                },
                lang = lang
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Assessment, contentDescription = null, tint = PrimaryGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${if (lang == Language.BN) "কন্ট্রিবিউশন সামারি (" else "Contribution Summary ("}${Strings.getMonthName(startMonth, lang)} - ${Strings.getMonthName(endMonth, lang)})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = PrimaryGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (lang == Language.BN)
                            "• মোট নিবন্ধিত সদস্য: ${Strings.formatDigits(members.size.toString(), lang)} জন\n• ফিল্টারকৃত রসিদ সংখ্যা: ${Strings.formatDigits(totalReceipts.toString(), lang)} টি\n• আদায়কৃত মাসিক চাঁদা: ${Strings.formatCurrency(totalContrib, lang)}\n• আদায়কৃত অতিরিক্ত অনুদান: ${Strings.formatCurrency(totalDonation, lang)}\n• সর্বমোট সংগৃহীত অর্থ: ${Strings.formatCurrency(totalCollected, lang)}"
                        else
                            "• Total Registered Members: ${Strings.formatDigits(members.size.toString(), lang)}\n• Filtered Receipts Count: ${Strings.formatDigits(totalReceipts.toString(), lang)}\n• Collected Contributions: ${Strings.formatCurrency(totalContrib, lang)}\n• Collected Extra Donations: ${Strings.formatCurrency(totalDonation, lang)}\n• Total Funds Collected: ${Strings.formatCurrency(totalCollected, lang)}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        item {
            Text(
                text = "${if (lang == Language.BN) "সময়কালের পেমেন্ট লেজার" else "Time Period Payment Ledger"} (${Strings.formatDigits(filteredReceipts.size.toString(), lang)})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }

        items(filteredReceipts) { receipt ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Receipt, contentDescription = null, tint = PrimaryGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (lang == Language.BN) receipt.memberNameBn else receipt.memberNameEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Text(
                            text = Strings.formatCurrency(receipt.totalReceived, lang),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = StatusSuccess
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${if (lang == Language.BN) "মাস:" else "Months:"} ${receipt.coveredMonths.joinToString(", ")}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "${if (lang == Language.BN) "তারিখ:" else "Date:"} ${receipt.paymentDate}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}
