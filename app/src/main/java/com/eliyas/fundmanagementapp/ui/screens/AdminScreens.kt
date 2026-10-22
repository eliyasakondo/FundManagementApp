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
import androidx.compose.ui.tooling.preview.Preview
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
import java.util.Locale

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

@Composable
fun AddMemberModal(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()

    var memberId by remember { mutableStateOf("MEM-${(1000..9999).random()}") }
    var nameBn by remember { mutableStateOf("") }
    var nameEn by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("+8801") }
    var altMobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var designation by remember { mutableStateOf("সাধারণ সদস্য") }
    var profession by remember { mutableStateOf("ব্যবসায়ী") }
    var monthlyRateText by remember { mutableStateOf("1000") }
    var initialPassword by remember { mutableStateOf("Pass1234") }
    var presentAddress by remember { mutableStateOf("ঢাকা, বাংলাদেশ") }
    var emergencyContact by remember { mutableStateOf("+8801700000000") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Strings.btnAddMember[lang]!!,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 450.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = if (lang == Language.BN) "নতুন সদস্য অ্যাকাউন্ট ও তথ্য পূরণ করুন" else "Fill Member Account & Personal Info",
                        fontSize = 12.sp,
                        color = WarmGold,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    OutlinedTextField(
                        value = memberId,
                        onValueChange = { memberId = it },
                        label = { Text(if (lang == Language.BN) "সদস্য আইডি (Member ID)" else "Member ID") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

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
                        label = { Text(if (lang == Language.BN) "মোবাইল নম্বর (+880)" else "Mobile Number (+880)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = altMobile,
                        onValueChange = { altMobile = it },
                        label = { Text(if (lang == Language.BN) "বিকল্প নম্বর" else "Alternative Mobile") },
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
                        value = monthlyRateText,
                        onValueChange = { monthlyRateText = it },
                        label = { Text(if (lang == Language.BN) "মাসিক চাঁদার পরিমাণ (৳500-৳3000)" else "Monthly Contribution Rate (৳)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = designation,
                        onValueChange = { designation = it },
                        label = { Text(if (lang == Language.BN) "সংগঠনের পদবী" else "Organization Designation") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = initialPassword,
                        onValueChange = { initialPassword = it },
                        label = { Text(if (lang == Language.BN) "প্রাথমিক পাসওয়ার্ড (১ম লগইনে পরিবর্তন)" else "Initial Password") },
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

                item {
                    OutlinedTextField(
                        value = emergencyContact,
                        onValueChange = { emergencyContact = it },
                        label = { Text(if (lang == Language.BN) "জরুরী যোগাযোগ নম্বর" else "Emergency Contact") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val rate = monthlyRateText.toDoubleOrNull() ?: 1000.0
                    if (nameEn.isNotEmpty() || nameBn.isNotEmpty()) {
                        val newMember = Member(
                            id = memberId,
                            username = memberId.lowercase(Locale.ROOT),
                            nameEn = if (nameEn.isNotEmpty()) nameEn else "New Member",
                            nameBn = if (nameBn.isNotEmpty()) nameBn else "নতুন সদস্য",
                            mobile = mobile,
                            altMobile = altMobile,
                            email = if (email.isNotEmpty()) email else "${memberId.lowercase(Locale.ROOT)}@org.bd",
                            designation = designation,
                            monthlyContribution = rate,
                            joinDate = "2025-02-20",
                            presentAddress = presentAddress,
                            emergencyContact = emergencyContact,
                            password = initialPassword,
                            status = "Active"
                        )
                        OrganizationRepository.addMember(newMember)
                        onSuccess()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(if (lang == Language.BN) "সদস্য অ্যাকাউন্ট তৈরি করুন" else "Create Member Account")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Strings.btnCancel[lang]!!)
            }
        }
    )
}

@Preview
@Composable
fun AddMemberModalPreview() {
    FundManagementTheme {
        AddMemberModal(onDismiss = {}, onSuccess = {})
    }
}

@Composable
fun AdminSettingsScreen() {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val managerPerms by OrganizationRepository.managerPermissions.collectAsState()
    val privacySettings by OrganizationRepository.privacySettings.collectAsState()

    // Editable Organization Profile State
    var orgNameBn by remember { mutableStateOf("তহবিল ব্যবস্থাপনা সামাজিক সংগঠন") }
    var orgNameEn by remember { mutableStateOf("Member Contribution & Fund Management System") }
    var regNumber by remember { mutableStateOf("REG-2025-88901") }
    var fiscalYear by remember { mutableStateOf("2025 - 2026") }
    var currencySymbol by remember { mutableStateOf("৳ (BDT)") }
    var contactEmail by remember { mutableStateOf("info@fundmanagement.org.bd") }

    // Editable Financial Policy State
    var defaultMonthlyRateText by remember { mutableStateOf("1000") }
    var gracePeriodDaysText by remember { mutableStateOf("10") }
    var isSavedNoticeVisible by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = if (lang == Language.BN) "এডমিন সিস্টেম সেটিংস ও তথ্য এডিট" else "Admin System Settings & Profile Editor",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
            Text(
                text = if (lang == Language.BN) "সংগঠনের নাম, রেজিস্ট্রেশন, অর্থবছর, পলিসি ও পারমিশন পরিবর্তন করুন" else "Edit Organization Info, Registration, Fiscal Year & Rules",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        if (isSavedNoticeVisible) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StatusSuccess.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lang == Language.BN) "সংগঠনের প্রোফাইল ও পলিসি সফলভাবে সেভ করা হয়েছে!" else "Organization Profile & Policies updated successfully!",
                            color = StatusSuccess,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // 1. EDITABLE ORGANIZATION PROFILE CARD
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Business, contentDescription = null, tint = PrimaryGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lang == Language.BN) "সংগঠনের তথ্য পরিবর্তন (Editable Profile)" else "Edit Organization Profile",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = PrimaryGreen
                        )
                    }

                    OutlinedTextField(
                        value = orgNameBn,
                        onValueChange = { orgNameBn = it },
                        label = { Text(if (lang == Language.BN) "সংগঠনের নাম (বাংলা)" else "Org Name (Bangla)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = orgNameEn,
                        onValueChange = { orgNameEn = it },
                        label = { Text(if (lang == Language.BN) "সংগঠনের নাম (ইংরেজি)" else "Org Name (English)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = regNumber,
                            onValueChange = { regNumber = it },
                            label = { Text(if (lang == Language.BN) "রেজিস্ট্রেশন নং" else "Reg Number") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = fiscalYear,
                            onValueChange = { fiscalYear = it },
                            label = { Text(if (lang == Language.BN) "অর্থবছর" else "Fiscal Year") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = currencySymbol,
                            onValueChange = { currencySymbol = it },
                            label = { Text(if (lang == Language.BN) "মুদ্রা চিহ্ন" else "Currency Symbol") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = contactEmail,
                            onValueChange = { contactEmail = it },
                            label = { Text(if (lang == Language.BN) "অফিশিয়াল ইমেইল" else "Contact Email") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 2. EDITABLE FINANCIAL POLICY & DUES RULES
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = StatusInfo)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lang == Language.BN) "আর্থিক পলিসি ও চাঁদার রুলস" else "Financial Dues & Policy Rules",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = PrimaryGreen
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = defaultMonthlyRateText,
                            onValueChange = { defaultMonthlyRateText = it },
                            label = { Text(if (lang == Language.BN) "ডিফল্ট চাঁদার হার (৳)" else "Default Monthly Rate (৳)") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = gracePeriodDaysText,
                            onValueChange = { gracePeriodDaysText = it },
                            label = { Text(if (lang == Language.BN) "গ্রেস পিরিয়ড (দিন)" else "Grace Period (Days)") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Button(
                        onClick = {
                            isSavedNoticeVisible = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (lang == Language.BN) "সংগঠনের প্রোফাইল সেভ করুন" else "Save Organization Profile",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 1. MANAGER ROLE PERMISSIONS MATRIX (Isolated to Manager Role)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = WarmGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lang == Language.BN) "ম্যানেজার রোল পারমিশন সেটিংস (Manager Role Only)" else "Manager Role Permissions Matrix",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = PrimaryGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Manager Option 1: Record Expenses
                    PermissionToggleRow(
                        title = if (lang == Language.BN) "সংগঠনের খরচ যুক্ত করার অনুমতি" else "Allow Managers to Record Expenses",
                        subtitle = if (lang == Language.BN) "বন্ধ করলে ম্যানেজার ভিউতে খরচ অপশন থাকবে না (এডমিনে প্রভাব ফেলবে না)" else "Disabling removes expense entry ONLY for Manager role",
                        isChecked = managerPerms.canAddExpense,
                        onCheckedChange = { isChecked ->
                            OrganizationRepository.updateManagerPermissions(managerPerms.copy(canAddExpense = isChecked))
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftBackground)

                    // Manager Option 2: Approve Payments Directly
                    PermissionToggleRow(
                        title = if (lang == Language.BN) "পেমেন্ট সরাসরি অনুমোদন করার অনুমতি" else "Allow Managers to Approve Payments",
                        subtitle = if (lang == Language.BN) "বন্ধ করলে ম্যানেজারের এন্ট্রি এডমিন এপ্রুভালের জন্য পেন্ডিং থাকবে" else "Disabling requires Admin approval for Manager payment logs",
                        isChecked = managerPerms.canApprovePayments,
                        onCheckedChange = { isChecked ->
                            OrganizationRepository.updateManagerPermissions(managerPerms.copy(canApprovePayments = isChecked))
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftBackground)

                    // Manager Option 3: View All Reports
                    PermissionToggleRow(
                        title = if (lang == Language.BN) "সকল আর্থিক রিপোর্ট দেখার অনুমতি" else "Allow Managers to View All Reports",
                        subtitle = if (lang == Language.BN) "বন্ধ করলে ম্যানেজাররা বিস্তারিত রিপোর্ট দেখতে পারবে না" else "Disabling restricts detailed report screens for Manager role",
                        isChecked = managerPerms.canViewAllReports,
                        onCheckedChange = { isChecked ->
                            OrganizationRepository.updateManagerPermissions(managerPerms.copy(canViewAllReports = isChecked))
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftBackground)

                    // Manager Option 4: Send Notifications
                    PermissionToggleRow(
                        title = if (lang == Language.BN) "সদস্যদের নোটিফিকেশন পাঠানোর অনুমতি" else "Allow Managers to Send Notifications",
                        subtitle = if (lang == Language.BN) "বন্ধ করলে ম্যানেজাররা নোটিফিকেশন ও বার্তা পাঠাতে পারবে না" else "Disabling restricts broadcast notices for Manager role",
                        isChecked = managerPerms.canSendNotification,
                        onCheckedChange = { isChecked ->
                            OrganizationRepository.updateManagerPermissions(managerPerms.copy(canSendNotification = isChecked))
                        }
                    )
                }
            }
        }

        // 2. MEMBER ROLE VISIBILITY & PRIVACY MATRIX (Isolated to Member Role)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Visibility, contentDescription = null, tint = PrimaryGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lang == Language.BN) "মেম্বার রোল প্রাইভেসি ও ভিউ সেটিংস (Member Role Only)" else "Member Privacy Controls",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = PrimaryGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Member Option 1: Show Member Names Publicly
                    PermissionToggleRow(
                        title = if (lang == Language.BN) "তালিকায় সদস্যের নাম প্রকাশ করা" else "Show Member Names Publicly",
                        subtitle = if (lang == Language.BN) "বন্ধ করলে মেম্বার ভিউতে নামগুলো গোপন থাকবে" else "Hides member names ONLY in Member role view",
                        isChecked = privacySettings.showMemberName,
                        onCheckedChange = { isChecked ->
                            OrganizationRepository.updatePrivacySettings(privacySettings.copy(showMemberName = isChecked))
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftBackground)

                    // Member Option 2: Show Amounts Publicly
                    PermissionToggleRow(
                        title = if (lang == Language.BN) "অনুদান ও চাঁদার টাকা প্রকাশ করা" else "Show Donation Amounts Publicly",
                        subtitle = if (lang == Language.BN) "বন্ধ করলে মেম্বাররা টাকার পরিমাণ দেখতে পাবে না" else "Hides transaction amounts ONLY in Member role view",
                        isChecked = privacySettings.showAmount,
                        onCheckedChange = { isChecked ->
                            OrganizationRepository.updatePrivacySettings(privacySettings.copy(showAmount = isChecked))
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftBackground)

                    // Member Option 3: Full Contribution History
                    PermissionToggleRow(
                        title = if (lang == Language.BN) "সম্পূর্ণ কন্ট্রিবিউশন হিস্ট্রি দেখানো" else "Show Full Contribution History",
                        subtitle = if (lang == Language.BN) "মেম্বাররা বিগত বছরের সম্পূর্ণ চাঁদার হিসাব দেখতে পাবে" else "Allow members to browse full historical logs",
                        isChecked = privacySettings.showFullContributionHistory,
                        onCheckedChange = { isChecked ->
                            OrganizationRepository.updatePrivacySettings(privacySettings.copy(showFullContributionHistory = isChecked))
                        }
                    )
                }
            }
        }

        // 3. SECURITY AUDIT LOGS SUMMARY
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.History, contentDescription = null, tint = StatusInfo)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lang == Language.BN) "সিকিউরিটি অডিট ট্রেইল ও পারমিশন লগ" else "Security Audit Logs & History",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = PrimaryGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (lang == Language.BN)
                            "• ২০ ফেব্রু ২০২৫ - Md. Eliyas Akondo (Admin) দ্বারা নতুন মেম্বার তৈরি করা হয়েছে\n• ১৯ ফেব্রু ২০২৫ - ম্যানেজার রোলকে খরচ যুক্ত করার অনুমতি দেওয়া হয়েছে\n• ১৮ ফেব্রু ২০২৫ - মেম্বারদের জন্য ফান্ড ব্যালেন্স দৃশ্যমানতা টগল আপডেট করা হয়েছে"
                        else
                            "• Feb 20, 2025 - New Member created by Md. Eliyas Akondo (Admin)\n• Feb 19, 2025 - Granted Manager role permission to record expenses\n• Feb 18, 2025 - Updated public fund balance visibility setting for members",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Export All Data Button
        item {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Icon(Icons.Default.Download, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (lang == Language.BN) "সম্পূর্ণ পারমিশন ও ফিন্যান্সিয়াল লেজার ব্যাকআপ নিন" else "Backup All Permissions & Ledger Data (PDF/Excel)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PermissionToggleRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = SurfaceWhite,
                checkedTrackColor = PrimaryGreen,
                uncheckedThumbColor = SurfaceWhite,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}


