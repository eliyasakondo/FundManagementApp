package com.eliyas.fundmanagementapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.*
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.common.CollectionProgressBarCard
import com.eliyas.fundmanagementapp.ui.common.DonutChartCard
import com.eliyas.fundmanagementapp.ui.common.MetricCard
import com.eliyas.fundmanagementapp.ui.common.StatusBadge
import com.eliyas.fundmanagementapp.ui.theme.*

// 1. MEMBER HOME DASHBOARD
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
    val currentMonthDue = maxOf(0.0, member.monthlyContribution - currentMonthPaid)

    val totalContribPaid = history.sumOf { it.paidAmount }
    val totalExtraDonation = history.sumOf { it.donationAmount }
    val overallTotalPaid = totalContribPaid + totalExtraDonation

    val totalOrgIncome = incomeList.sumOf { it.amount } + totalContribPaid + totalExtraDonation
    val totalOrgExpenses = expenseList.sumOf { it.amount }
    val availableBalance = totalOrgIncome - totalOrgExpenses

    // Personal Fund Breakdown Data
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
        // Member Welcome & Status Card
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

        // Collection Progress Goal Card
        item {
            CollectionProgressBarCard(
                title = if (lang == Language.BN) "চলতি মাসের চাঁদা আদায় স্ট্যাটাস" else "Current Month Payment Progress",
                collected = currentMonthPaid,
                target = member.monthlyContribution,
                lang = lang
            )
        }

        // Personal Financial Breakdown Donut Chart
        item {
            DonutChartCard(
                title = if (lang == Language.BN) "আমার ফান্ডের বণ্টন ও গ্রাফ" else "My Contribution & Donation Ratio",
                items = personalDonutData,
                totalAmount = overallTotalPaid,
                lang = lang
            )
        }

        // Financial Metrics Grid
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

        // Organization Financial Transparency
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

        // Recent Notifications & Announcements
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

// 2. MY CONTRIBUTION DETAILS SCREEN
@Composable
fun MyContributionDetailsScreen() {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val currentMember by OrganizationRepository.currentMember.collectAsState()
    val memberHistory by OrganizationRepository.memberHistory.collectAsState()

    val member = currentMember ?: return
    val history = memberHistory[member.id] ?: emptyList()

    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredList = when (selectedFilter) {
        "PAID" -> history.filter { it.status == PaymentStatus.PAID || it.status == PaymentStatus.PAID_IN_ADVANCE }
        "PARTIAL" -> history.filter { it.status == PaymentStatus.PARTIALLY_PAID }
        "UNPAID" -> history.filter { it.status == PaymentStatus.UNPAID }
        else -> history
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .padding(16.dp)
    ) {
        Text(
            text = if (lang == Language.BN) "আমার মাসিক চাঁদার হিসাব" else "My Monthly Contribution Details",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("ALL" to "সবগুলো", "PAID" to "পরিশোধিত", "PARTIAL" to "আংশিক", "UNPAID" to "বকেয়া").forEach { (key, label) ->
                FilterChip(
                    selected = selectedFilter == key,
                    onClick = { selectedFilter = key },
                    label = { Text(if (lang == Language.BN) label else key) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filteredList) { record ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = Strings.getMonthName(record.monthKey, lang),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            StatusBadge(status = record.status, lang = lang)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = BorderLight)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(if (lang == Language.BN) "ধার্যকৃত চাঁদা" else "Required", fontSize = 11.sp, color = TextSecondary)
                                Text(Strings.formatCurrency(record.requiredAmount, lang), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }

                            Column {
                                Text(if (lang == Language.BN) "পরিশোধিত" else "Paid", fontSize = 11.sp, color = TextSecondary)
                                Text(Strings.formatCurrency(record.paidAmount, lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = StatusSuccess)
                            }

                            Column {
                                val due = maxOf(0.0, record.requiredAmount - record.paidAmount)
                                Text(if (lang == Language.BN) "বকেয়া" else "Due", fontSize = 11.sp, color = TextSecondary)
                                Text(Strings.formatCurrency(due, lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (due > 0) StatusError else TextSecondary)
                            }
                        }
                    }
                }
            }
        }
    }
}

// 3. ALL MEMBERS CONTRIBUTION HISTORY SCREEN (With Admin Privacy Masking)
@Composable
fun AllMembersContributionHistoryScreen() {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val receipts by OrganizationRepository.paymentReceipts.collectAsState()
    val privacy by OrganizationRepository.privacySettings.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val filtered = receipts.filter {
        it.memberNameEn.contains(searchQuery, ignoreCase = true) ||
        it.memberNameBn.contains(searchQuery, ignoreCase = true) ||
        it.memberId.contains(searchQuery, ignoreCase = true) ||
        it.receiptNo.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .padding(16.dp)
    ) {
        Text(
            text = if (lang == Language.BN) "সকল সদস্যের চাঁদার ইতিহাস" else "All Members' Payment History",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(if (lang == Language.BN) "সদস্য নাম, আইডি বা রসিদ দিয়ে খুঁজুন..." else "Search by member name, ID or receipt...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filtered) { item ->
                val displayName = if (privacy.showMemberName) {
                    if (lang == Language.BN) item.memberNameBn else item.memberNameEn
                } else {
                    if (lang == Language.BN) "সদস্য (${item.memberId})" else "Member (${item.memberId})"
                }

                val displayAmount = if (privacy.showAmount) {
                    Strings.formatCurrency(item.totalReceived, lang)
                } else {
                    "***"
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Receipt, contentDescription = null, tint = PrimaryGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(displayName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                            Text(displayAmount, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = StatusSuccess)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${if (lang == Language.BN) "তারিখ:" else "Date:"} ${item.paymentDate}", fontSize = 12.sp, color = TextSecondary)
                            Text("${if (lang == Language.BN) "মাধ্যম:" else "Method:"} ${item.paymentMethod}", fontSize = 12.sp, color = TextSecondary)
                        }

                        if (item.donationAmount > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${if (lang == Language.BN) "অতিরিক্ত অনুদান:" else "Extra Donation:"} ${Strings.formatCurrency(item.donationAmount, lang)}",
                                fontSize = 12.sp,
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
