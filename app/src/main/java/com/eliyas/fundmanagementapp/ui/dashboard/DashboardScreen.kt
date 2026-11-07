package com.eliyas.fundmanagementapp.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.common.BarChartCard
import com.eliyas.fundmanagementapp.ui.common.CollectionProgressBarCard
import com.eliyas.fundmanagementapp.ui.common.DonutChartCard
import com.eliyas.fundmanagementapp.ui.theme.*

data class DashboardCardItem(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val subtitleBn: String,
    val subtitleEn: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconTintColor: Color,
    val onClick: () -> Unit
)

@Composable
fun DashboardScreen(
    onNavigate: (String) -> Unit,
    onOpenRecordPayment: () -> Unit,
    onOpenAddMember: () -> Unit,
    onOpenRecordExpense: () -> Unit = {},
    onOpenNotifications: () -> Unit = {}
) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val members by OrganizationRepository.members.collectAsState()
    val receipts by OrganizationRepository.paymentReceipts.collectAsState()
    val donations by OrganizationRepository.donations.collectAsState()
    val incomeList by OrganizationRepository.income.collectAsState()
    val expenseList by OrganizationRepository.expenses.collectAsState()
    val notifications by OrganizationRepository.notifications.collectAsState()

    val totalMembersCount = members.size
    val activeMembersCount = members.count { it.status == "Active" }
    val expectedThisMonth = members.sumOf { it.monthlyContribution }
    val collectedThisMonth = receipts.filter { it.coveredMonths.contains("2025-01") }.sumOf { it.contributionAmount }
    val extraDonationsTotal = donations.sumOf { it.amount }
    val contribTotal = receipts.sumOf { it.contributionAmount }
    val totalIncome = incomeList.sumOf { it.amount } + receipts.sumOf { it.totalReceived }
    val totalExpenses = expenseList.sumOf { it.amount }
    val balance = totalIncome - totalExpenses
    val unreadNotifsCount = notifications.count { !it.isRead }

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

    val gridActionCards = listOf(
        DashboardCardItem(
            id = "pay",
            titleBn = "টাকা জমা",
            titleEn = "Record Payment",
            subtitleBn = "রসিদ তৈরি",
            subtitleEn = "New Receipt",
            icon = Icons.Default.Add,
            iconBgColor = WarmGold,
            iconTintColor = DarkGreen,
            onClick = onOpenRecordPayment
        ),
        DashboardCardItem(
            id = "add_mem",
            titleBn = "সদস্য যোগ",
            titleEn = "Add Member",
            subtitleBn = "নতুন অ্যাকাউন্ট",
            subtitleEn = "New Account",
            icon = Icons.Default.PersonAdd,
            iconBgColor = PrimaryGreen,
            iconTintColor = SurfaceWhite,
            onClick = onOpenAddMember
        ),
        DashboardCardItem(
            id = "add_exp",
            titleBn = "খরচ এন্ট্রি",
            titleEn = "Add Expense",
            subtitleBn = "ভাউচার এন্ট্রি",
            subtitleEn = "New Voucher",
            icon = Icons.Default.Remove,
            iconBgColor = StatusError,
            iconTintColor = SurfaceWhite,
            onClick = onOpenRecordExpense
        ),
        DashboardCardItem(
            id = "mems",
            titleBn = "সদস্য তালিকা",
            titleEn = "Members",
            subtitleBn = "$activeMembersCount/$totalMembersCount সক্রিয়",
            subtitleEn = "$activeMembersCount/$totalMembersCount Active",
            icon = Icons.Default.Groups,
            iconBgColor = PrimaryGreen.copy(alpha = 0.15f),
            iconTintColor = PrimaryGreen,
            onClick = { onNavigate("members") }
        ),
        DashboardCardItem(
            id = "receipts",
            titleBn = "পেমেন্ট রসিদ",
            titleEn = "Payments",
            subtitleBn = "${receipts.size} টি রসিদ",
            subtitleEn = "${receipts.size} Receipts",
            icon = Icons.Default.Payments,
            iconBgColor = WarmGold.copy(alpha = 0.2f),
            iconTintColor = DarkGreen,
            onClick = { onNavigate("payments") }
        ),
        DashboardCardItem(
            id = "exp_list",
            titleBn = "খরচের খাতা",
            titleEn = "Expenses",
            subtitleBn = "${expenseList.size} টি ভাউচার",
            subtitleEn = "${expenseList.size} Vouchers",
            icon = Icons.AutoMirrored.Filled.ReceiptLong,
            iconBgColor = StatusError.copy(alpha = 0.15f),
            iconTintColor = StatusError,
            onClick = { onNavigate("expenses") }
        ),
        DashboardCardItem(
            id = "reports",
            titleBn = "আর্থিক রিপোর্ট",
            titleEn = "Reports",
            subtitleBn = "লেজার ও চার্ট",
            subtitleEn = "Ledger Matrix",
            icon = Icons.Default.Assessment,
            iconBgColor = StatusInfo.copy(alpha = 0.2f),
            iconTintColor = StatusInfo,
            onClick = { onNavigate("reports") }
        ),
        DashboardCardItem(
            id = "notifs",
            titleBn = "নোটিফিকেশন",
            titleEn = "Notices",
            subtitleBn = "$unreadNotifsCount অপঠিত",
            subtitleEn = "$unreadNotifsCount Unread",
            icon = Icons.Default.NotificationsActive,
            iconBgColor = WarmGold,
            iconTintColor = DarkGreen,
            onClick = onOpenNotifications
        ),
        DashboardCardItem(
            id = "settings",
            titleBn = "সেটিংস",
            titleEn = "Settings",
            subtitleBn = "পারমিশন কন্ট্রোল",
            subtitleEn = "Admin Control",
            icon = Icons.Default.Settings,
            iconBgColor = PrimaryGreen.copy(alpha = 0.15f),
            iconTintColor = PrimaryGreen,
            onClick = { onNavigate("settings") }
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section 1: Hero Balance & Goal Progress Banner Card
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
                                text = if (lang == Language.BN) "সংগঠনের অবশিষ্টাংশ/ব্যালেন্স" else "Total Available Balance",
                                fontSize = 12.sp,
                                color = WarmGold
                            )
                            Text(
                                text = Strings.formatCurrency(balance, lang),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = SurfaceWhite
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(WarmGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = DarkGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = SurfaceWhite.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(if (lang == Language.BN) "মোট আয়:" else "Total Inc:", fontSize = 11.sp, color = SurfaceWhite.copy(alpha = 0.8f))
                            Text(Strings.formatCurrency(totalIncome, lang), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SurfaceWhite)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(if (lang == Language.BN) "মোট ব্যয়:" else "Total Exp:", fontSize = 11.sp, color = SurfaceWhite.copy(alpha = 0.8f))
                            Text(Strings.formatCurrency(totalExpenses, lang), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarmGold)
                        }
                    }
                }
            }
        }

        // Goal Progress Bar Card
        item {
            CollectionProgressBarCard(
                title = if (lang == Language.BN) "চলতি মাসের চাঁদা আদায়ের অগ্রগতি" else "Monthly Collection Goal Progress",
                collected = collectedThisMonth,
                target = expectedThisMonth,
                lang = lang
            )
        }

        // Section 2: Primary Operations Functional Cards Grid (3x3 Grid)
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
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
                            text = if (lang == Language.BN) "কুইক সার্ভিস ও কাজসমূহ" else "Quick Action Services",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                        Icon(Icons.Default.GridView, contentDescription = null, tint = PrimaryGreen)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        gridActionCards.chunked(3).forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowItems.forEach { item ->
                                    Card(
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = SoftBackground),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { item.onClick() }
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(42.dp)
                                                    .clip(CircleShape)
                                                    .background(item.iconBgColor),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = item.icon,
                                                    contentDescription = null,
                                                    tint = item.iconTintColor,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = if (lang == Language.BN) item.titleBn else item.titleEn,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                textAlign = TextAlign.Center,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Text(
                                                text = if (lang == Language.BN) item.subtitleBn else item.subtitleEn,
                                                fontSize = 9.sp,
                                                color = TextSecondary,
                                                textAlign = TextAlign.Center,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 3: Full-Width Information Summary Banner Card
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = PrimaryGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (lang == Language.BN) "বার্ষিক তহবিল ও আদায় সারসংক্ষেপ (২০২৫)" else "Annual Collection Summary (2025)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = PrimaryGreen
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(StatusSuccess.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (lang == Language.BN) "সক্রিয় তহবিল" else "Active Fund",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusSuccess
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (lang == Language.BN)
                            "• মোট নিবন্ধিত সদস্য: ${Strings.formatDigits(totalMembersCount.toString(), lang)} জন (${Strings.formatDigits(activeMembersCount.toString(), lang)} জন সক্রিয়)\n• মোট ইস্যুকৃত রসিদ: ${Strings.formatDigits(receipts.size.toString(), lang)} টি\n• অতিরিক্ত অনুদান সংগ্রহ: ${Strings.formatCurrency(extraDonationsTotal, lang)}"
                        else
                            "• Registered Members: ${Strings.formatDigits(totalMembersCount.toString(), lang)} ($activeMembersCount Active)\n• Issued Receipts: ${Strings.formatDigits(receipts.size.toString(), lang)}\n• Extra Donations Raised: ${Strings.formatCurrency(extraDonationsTotal, lang)}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Section 4: Analytics Chart Cards
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

        // Section 5: Recent Payment Receipts Activity Log Card
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
