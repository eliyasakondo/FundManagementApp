package com.eliyas.fundmanagementapp.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.common.BarChartCard
import com.eliyas.fundmanagementapp.ui.common.CollectionProgressBarCard
import com.eliyas.fundmanagementapp.ui.common.DonutChartCard
import com.eliyas.fundmanagementapp.ui.common.MetricCard
import com.eliyas.fundmanagementapp.ui.theme.*

@Composable
fun DashboardScreen(
    onNavigate: (String) -> Unit,
    onOpenRecordPayment: () -> Unit,
    onOpenAddMember: () -> Unit
) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val members by OrganizationRepository.members.collectAsState()
    val receipts by OrganizationRepository.paymentReceipts.collectAsState()
    val donations by OrganizationRepository.donations.collectAsState()
    val incomeList by OrganizationRepository.income.collectAsState()
    val expenseList by OrganizationRepository.expenses.collectAsState()

    val totalMembersCount = members.size
    val activeMembersCount = members.count { it.status == "Active" }
    val expectedThisMonth = members.sumOf { it.monthlyContribution }
    val collectedThisMonth = receipts.filter { it.coveredMonths.contains("2025-01") }.sumOf { it.contributionAmount }
    val extraDonationsTotal = donations.sumOf { it.amount }
    val contribTotal = receipts.sumOf { it.contributionAmount }
    val totalIncome = incomeList.sumOf { it.amount } + receipts.sumOf { it.totalReceived }
    val totalExpenses = expenseList.sumOf { it.amount }
    val balance = totalIncome - totalExpenses

    val barChartData = listOf(
        Pair(if (lang == Language.BN) "জানু" else "Jan", 320000.0),
        Pair(if (lang == Language.BN) "ফেব্রু" else "Feb", 280000.0),
        Pair(if (lang == Language.BN) "মার্চ" else "Mar", 310000.0),
        Pair(if (lang == Language.BN) "এপ্রিল" else "Apr", 350000.0),
        Pair(if (lang == Language.BN) "মে" else "May", 290000.0),
        Pair(if (lang == Language.BN) "জুন" else "Jun", 380000.0)
    )

    val donutData = listOf(
        Triple(if (lang == Language.BN) "মাসিক চাঁদা" else "Contributions", contribTotal, PrimaryGreen),
        Triple(if (lang == Language.BN) "অতিরিক্ত অনুদান" else "Donations", extraDonationsTotal, WarmGold),
        Triple(if (lang == Language.BN) "অন্যান্য আয়" else "Other Income", incomeList.sumOf { it.amount }, StatusInfo)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                        Column {
                            Text(
                                text = if (lang == Language.BN) "এডমিন কন্ট্রোল ড্যাশবোর্ড" else "Admin Financial Control",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = SurfaceWhite
                            )
                            Text(
                                text = if (lang == Language.BN) "২০০+ সদস্যের রিয়েল-টাইম হিসাব ও এনালাইটিক্স" else "Real-time Analytics for 200+ Members",
                                fontSize = 12.sp,
                                color = WarmGold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(WarmGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Analytics, contentDescription = null, tint = DarkGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onOpenRecordPayment,
                            colors = ButtonDefaults.buttonColors(containerColor = WarmGold, contentColor = DarkGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(Strings.btnRecordPayment[lang]!!, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onOpenAddMember,
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceWhite, contentColor = PrimaryGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(Strings.btnAddMember[lang]!!, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            CollectionProgressBarCard(
                title = if (lang == Language.BN) "চলতি মাসের চাঁদা আদায়ের অগ্রগতি" else "Monthly Collection Goal Progress",
                collected = collectedThisMonth,
                target = expectedThisMonth,
                lang = lang
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = if (lang == Language.BN) "মোট সদস্য" else "Total Members",
                        value = Strings.formatDigits(totalMembersCount.toString(), lang),
                        subtitle = "${Strings.formatDigits(activeMembersCount.toString(), lang)} ${if (lang == Language.BN) "সক্রিয় সদস্য" else "Active Members"}",
                        icon = Icons.Default.Groups,
                        accentColor = PrimaryGreen,
                        modifier = Modifier.weight(1f)
                    )

                    MetricCard(
                        title = Strings.lblAvailableBalance[lang]!!,
                        value = Strings.formatCurrency(balance, lang),
                        subtitle = "${if (lang == Language.BN) "মোট আয়:" else "Inc:"} ${Strings.formatCurrency(totalIncome, lang)}",
                        icon = Icons.Default.AccountBalance,
                        accentColor = StatusInfo,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = if (lang == Language.BN) "অতিরিক্ত অনুদান" else "Extra Donations",
                        value = Strings.formatCurrency(extraDonationsTotal, lang),
                        subtitle = "${if (lang == Language.BN) "সংগৃহীত" else "Collected"}",
                        icon = Icons.Default.VolunteerActivism,
                        accentColor = WarmGold,
                        modifier = Modifier.weight(1f)
                    )

                    MetricCard(
                        title = if (lang == Language.BN) "প্রত্যাশিত চাঁদা" else "Expected Monthly Dues",
                        value = Strings.formatCurrency(expectedThisMonth, lang),
                        subtitle = "${if (lang == Language.BN) "চলতি মাস" else "This Month"}",
                        icon = Icons.Default.Payments,
                        accentColor = StatusSuccess,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        item {
            Text(
                text = if (lang == Language.BN) "আর্থিক এনালাইটিক্স ও চার্ট" else "Graphical Financial Visualizations",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }

        item {
            BarChartCard(
                title = if (lang == Language.BN) "মাসভিত্তিক চাঁদা সংগ্রহের ট্রেন্ড" else "Monthly Collection Trend (2025)",
                data = barChartData,
                lang = lang
            )
        }

        item {
            DonutChartCard(
                title = if (lang == Language.BN) "সংগঠনের ফান্ডের উৎস ও বণ্টন" else "Fund Sources & Distribution",
                items = donutData,
                totalAmount = totalIncome,
                lang = lang
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (lang == Language.BN) "সাম্প্রতিক পেমেন্ট এন্ট্রি" else "Recent Recorded Payments",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
            }
        }

        items(receipts.take(4)) { receipt ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (lang == Language.BN) receipt.memberNameBn else receipt.memberNameEn,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${receipt.receiptNo} | ${receipt.paymentMethod} | ${receipt.paymentDate}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = Strings.formatCurrency(receipt.totalReceived, lang),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = StatusSuccess
                        )
                        if (receipt.donationAmount > 0) {
                            Text(
                                text = "+${Strings.formatCurrency(receipt.donationAmount, lang)} ${if (lang == Language.BN) "অনুদান" else "don."}",
                                fontSize = 11.sp,
                                color = WarmGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
