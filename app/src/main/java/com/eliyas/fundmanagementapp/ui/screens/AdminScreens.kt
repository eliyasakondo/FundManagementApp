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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.*
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.common.BarChartCard
import com.eliyas.fundmanagementapp.ui.common.CollectionProgressBarCard
import com.eliyas.fundmanagementapp.ui.common.DonutChartCard
import com.eliyas.fundmanagementapp.ui.common.MetricCard
import com.eliyas.fundmanagementapp.ui.theme.*

@Composable
fun AdminDashboardScreen(
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

    // Dummy Trend Data for Bar Chart
    val barChartData = listOf(
        Pair(if (lang == Language.BN) "জানু" else "Jan", 320000.0),
        Pair(if (lang == Language.BN) "ফেব্রু" else "Feb", 280000.0),
        Pair(if (lang == Language.BN) "মার্চ" else "Mar", 310000.0),
        Pair(if (lang == Language.BN) "এপ্রিল" else "Apr", 350000.0),
        Pair(if (lang == Language.BN) "মে" else "May", 290000.0),
        Pair(if (lang == Language.BN) "জুন" else "Jun", 380000.0)
    )

    // Donut Chart Data
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
        // Control Panel Banner & Quick Actions
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

        // Collection Target Progress Bar
        item {
            CollectionProgressBarCard(
                title = if (lang == Language.BN) "চলতি মাসের চাঁদা আদায়ের অগ্রগতি" else "Monthly Collection Goal Progress",
                collected = collectedThisMonth,
                target = expectedThisMonth,
                lang = lang
            )
        }

        // Key Metrics Summary Cards Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = if (lang == Language.BN) "মোট সদস্য" else "Total Members",
                    value = Strings.formatDigits(totalMembersCount.toString(), lang),
                    subtitle = "${Strings.formatDigits(activeMembersCount.toString(), lang)} ${if (lang == Language.BN) "সক্রিয়" else "Active"}",
                    icon = Icons.Default.Groups,
                    accentColor = PrimaryGreen,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = Strings.lblAvailableBalance[lang]!!,
                    value = Strings.formatCurrency(balance, lang),
                    subtitle = "${if (lang == Language.BN) "ব্যয়:" else "Exp:"} ${Strings.formatCurrency(totalExpenses, lang)}",
                    icon = Icons.Default.AccountBalance,
                    accentColor = StatusInfo,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // GRAPHICAL CHARTS SECTION
        item {
            Text(
                text = if (lang == Language.BN) "আর্থিক এনালাইটিক্স ও চার্ট" else "Graphical Financial Visualizations",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }

        // Bar Chart - Monthly Collection Trend
        item {
            BarChartCard(
                title = if (lang == Language.BN) "মাসভিত্তিক চাঁদা সংগ্রহের ট্রেন্ড" else "Monthly Collection Trend (2025)",
                data = barChartData,
                lang = lang
            )
        }

        // Donut Chart - Fund Breakdown
        item {
            DonutChartCard(
                title = if (lang == Language.BN) "সংগঠনের ফান্ডের উৎস ও বণ্টন" else "Fund Sources & Distribution",
                items = donutData,
                totalAmount = totalIncome,
                lang = lang
            )
        }

        // Recent Payments
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

                // Allocation Breakdown Card
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

@Composable
fun MemberManagementScreen(onOpenAddMember: () -> Unit) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val members by OrganizationRepository.members.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val filtered = members.filter {
        it.nameEn.contains(searchQuery, ignoreCase = true) ||
        it.nameBn.contains(searchQuery, ignoreCase = true) ||
        it.id.contains(searchQuery, ignoreCase = true) ||
        it.mobile.contains(searchQuery)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${if (lang == Language.BN) "সদস্য তালিকা" else "Member Directory"} (${Strings.formatDigits(filtered.size.toString(), lang)})",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )

            Button(
                onClick = onOpenAddMember,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(Strings.btnAddMember[lang]!!, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(if (lang == Language.BN) "২০০+ সদস্যের নাম, আইডি বা মোবাইল দিয়ে খুঁজুন..." else "Search 200+ members by name, ID or mobile...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filtered) { member ->
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
                            Column {
                                Text(
                                    text = if (lang == Language.BN) member.nameBn else member.nameEn,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "ID: ${member.id} | ${member.mobile}",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = Strings.formatCurrency(member.monthlyContribution, lang),
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryGreen,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = member.status,
                                    fontSize = 11.sp,
                                    color = if (member.status == "Active") StatusSuccess else StatusError,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
