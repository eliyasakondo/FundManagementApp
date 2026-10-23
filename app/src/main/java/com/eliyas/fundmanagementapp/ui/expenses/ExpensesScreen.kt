package com.eliyas.fundmanagementapp.ui.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
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
fun ExpensesScreen(onOpenRecordExpense: () -> Unit) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val expenses by OrganizationRepository.expenses.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    val totalExpensesSpent = expenses.sumOf { it.amount }

    val filteredExpenses = expenses.filter { expense ->
        expense.titleEn.contains(searchQuery, ignoreCase = true) ||
        expense.titleBn.contains(searchQuery, ignoreCase = true) ||
        expense.category.contains(searchQuery, ignoreCase = true) ||
        expense.categoryBn.contains(searchQuery, ignoreCase = true) ||
        expense.id.contains(searchQuery, ignoreCase = true) ||
        expense.addedBy.contains(searchQuery, ignoreCase = true)
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
                        text = if (lang == Language.BN) "সংগঠনের ব্যয় ও খরচের খাতা" else "Organizational Expenses Ledger",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                    Text(
                        text = if (lang == Language.BN) "অফিস ভাড়া, ইভেন্ট খরচ ও অন্যান্য ভাউচার ট্র্যাকিং" else "Track operational expenses & office vouchers",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = onOpenRecordExpense,
                    colors = ButtonDefaults.buttonColors(containerColor = StatusError, contentColor = SurfaceWhite),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (lang == Language.BN) "খরচ যুক্ত করুন" else "Add Expense", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = StatusError),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (lang == Language.BN) "মোট ব্যয়কৃত অর্থ" else "Total Operational Expenses",
                        fontSize = 12.sp,
                        color = SurfaceWhite.copy(alpha = 0.8f)
                    )
                    Text(
                        text = Strings.formatCurrency(totalExpensesSpent, lang),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurfaceWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${if (lang == Language.BN) "সর্বমোট ভাউচার সংখ্যা:" else "Total Expense Vouchers:"} ${Strings.formatDigits(expenses.size.toString(), lang)}",
                        fontSize = 11.sp,
                        color = SurfaceWhite.copy(alpha = 0.9f)
                    )
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(if (lang == Language.BN) "খাত, বিবরণী বা ভাউচার নং দিয়ে খুঁজুন..." else "Search by category, description or voucher #...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text(
                text = "${if (lang == Language.BN) "ব্যয় রেজিস্টার বিবরণী" else "Expense Register Entries"} (${Strings.formatDigits(filteredExpenses.size.toString(), lang)})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }

        items(filteredExpenses) { expense ->
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(StatusError.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = null, tint = StatusError, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (lang == Language.BN) expense.titleBn else expense.titleEn,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Voucher: ${expense.id} | ${if (lang == Language.BN) expense.categoryBn else expense.category}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Text(
                            text = Strings.formatCurrency(expense.amount, lang),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = StatusError
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = SoftBackground)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${if (lang == Language.BN) "তারিখ:" else "Date:"} ${expense.date}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "${if (lang == Language.BN) "এন্ট্রিদাতা:" else "Logged by:"} ${expense.addedBy}",
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
