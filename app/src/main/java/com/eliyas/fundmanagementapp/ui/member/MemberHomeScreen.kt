package com.eliyas.fundmanagementapp.ui.member

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.model.PaymentStatus
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.common.CollectionProgressBarCard
import com.eliyas.fundmanagementapp.ui.common.DonutChartCard
import com.eliyas.fundmanagementapp.ui.common.MetricCard
import com.eliyas.fundmanagementapp.ui.common.StatusBadge
import com.eliyas.fundmanagementapp.ui.theme.*

@Composable
fun MemberHomeScreen(
    onNavigateToTab: (String) -> Unit
) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val currentMember by OrganizationRepository.currentMember.collectAsState()
    val memberHistory by OrganizationRepository.memberHistory.collectAsState()
    val incomeList by OrganizationRepository.income.collectAsState()
    val expenseList by OrganizationRepository.expenses.collectAsState()
    val notifications by OrganizationRepository.notifications.collectAsState()

    val member = currentMember ?: return
    val history = memberHistory[member.id] ?: emptyList()

    val currentMonthRecord = history.firstOrNull { it.monthKey == "2025-01" }
    val currentMonthStatus = currentMonthRecord?.status ?: PaymentStatus.UNPAID
    val currentMonthPaid = currentMonthRecord?.paidAmount ?: 0.0

    val totalContribPaid = history.sumOf { it.paidAmount }
    val totalExtraDonation = history.sumOf { it.donationAmount }
    val overallTotalPaid = totalContribPaid + totalExtraDonation

    val totalOrgIncome = incomeList.sumOf { it.amount } + totalContribPaid + totalExtraDonation
    val totalOrgExpenses = expenseList.sumOf { it.amount }
    val availableBalance = totalOrgIncome - totalOrgExpenses

    val personalDonutData = listOf(
        Triple(if (lang == Language.BN) "পরিশোধিত চাঁদা" else "Contrib Paid", totalContribPaid, StatusSuccess),
        Triple(if (lang == Language.BN) "অতিরিক্ত অনুদান" else "Extra Donation", totalExtraDonation, WarmGold)
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(WarmGold),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = member.nameEn.take(1).uppercase(),
                                    fontSize = 22.sp,
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
                                    text = "${Strings.fieldMemberId[lang]}: ${Strings.formatDigits(member.id, lang)} | ${member.membershipType.let { if (lang == Language.BN) it.labelBn else it.labelEn }}",
                                    fontSize = 12.sp,
                                    color = WarmGold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = SurfaceWhite.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = Strings.lblMonthlyContribution[lang]!!,
                                fontSize = 12.sp,
                                color = SurfaceWhite.copy(alpha = 0.8f)
                            )
                            Text(
                                text = Strings.formatCurrency(member.monthlyContribution, lang),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarmGold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = Strings.lblCurrentMonthStatus[lang]!!,
                                fontSize = 12.sp,
                                color = SurfaceWhite.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            StatusBadge(status = currentMonthStatus, lang = lang)
                        }
                    }
                }
            }
        }

        item {
            CollectionProgressBarCard(
                title = if (lang == Language.BN) "চলতি মাসের চাঁদা আদায় স্ট্যাটাস" else "Current Month Payment Progress",
                collected = currentMonthPaid,
                target = member.monthlyContribution,
                lang = lang
            )
        }

        item {
            DonutChartCard(
                title = if (lang == Language.BN) "আমার ফান্ডের বণ্টন ও গ্রাফ" else "My Contribution & Donation Ratio",
                items = personalDonutData,
                totalAmount = overallTotalPaid,
                lang = lang
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = Strings.lblTotalContributionsPaid[lang]!!,
                    value = Strings.formatCurrency(totalContribPaid, lang),
                    icon = Icons.Default.CheckCircle,
                    accentColor = StatusSuccess,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = Strings.lblTotalExtraDonation[lang]!!,
                    value = Strings.formatCurrency(totalExtraDonation, lang),
                    icon = Icons.Default.VolunteerActivism,
                    accentColor = WarmGold,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (lang == Language.BN) "সংগঠনের সামগ্রিক তহবিল" else "Org Financial Transparency",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                        Icon(Icons.Default.Shield, contentDescription = null, tint = PrimaryGreen)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(Strings.lblTotalIncome[lang]!!, fontSize = 11.sp, color = TextSecondary)
                            Text(Strings.formatCurrency(totalOrgIncome, lang), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = StatusSuccess)
                        }

                        Column {
                            Text(Strings.lblTotalExpenses[lang]!!, fontSize = 11.sp, color = TextSecondary)
                            Text(Strings.formatCurrency(totalOrgExpenses, lang), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = StatusError)
                        }

                        Column {
                            Text(Strings.lblAvailableBalance[lang]!!, fontSize = 11.sp, color = TextSecondary)
                            Text(Strings.formatCurrency(availableBalance, lang), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (lang == Language.BN) "সাম্প্রতিক নোটিফিকেশন" else "Recent Notifications",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
            }
        }

        items(notifications.take(3)) { notif ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PrimaryGreen.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (lang == Language.BN) notif.titleBn else notif.titleEn,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (lang == Language.BN) notif.messageBn else notif.messageEn,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
