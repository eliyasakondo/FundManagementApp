package com.eliyas.fundmanagementapp.ui.expenses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.ExpenseRecord
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.theme.*

@Composable
fun RecordExpenseModal(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()

    var expenseId by remember { mutableStateOf("EXP-${(1000..9999).random()}") }
    var titleBn by remember { mutableStateOf("") }
    var titleEn by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("অফিস খরচ") }
    var amountText by remember { mutableStateOf("") }
    var vendor by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Cash") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (lang == Language.BN) "নতুন খরচ রেকর্ড করুন" else "Log New Expense Voucher",
                    fontWeight = FontWeight.Bold,
                    color = StatusError,
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
                    .heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = expenseId,
                        onValueChange = { expenseId = it },
                        label = { Text(if (lang == Language.BN) "ভাউচার নম্বর" else "Voucher Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = titleBn,
                        onValueChange = { titleBn = it },
                        label = { Text(if (lang == Language.BN) "খরচের শিরোনাম (বাংলা)" else "Expense Title (Bangla)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = titleEn,
                        onValueChange = { titleEn = it },
                        label = { Text(if (lang == Language.BN) "খরচের শিরোনাম (ইংরেজি)" else "Expense Title (English)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text(if (lang == Language.BN) "খরচের খাত (Category)" else "Expense Category") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = { Text(if (lang == Language.BN) "মোট খরচের পরিমাণ (৳)" else "Expense Amount (৳)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = vendor,
                        onValueChange = { vendor = it },
                        label = { Text(if (lang == Language.BN) "গ্রহীতা / ভেন্ডর নাম" else "Vendor / Recipient Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = paymentMethod,
                        onValueChange = { paymentMethod = it },
                        label = { Text(if (lang == Language.BN) "পরিশোধের মাধ্যম" else "Payment Method") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(if (lang == Language.BN) "বিস্তারিত বিবরণ" else "Description / Notes") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    if (amount > 0 && (titleEn.isNotEmpty() || titleBn.isNotEmpty())) {
                        val newExpense = ExpenseRecord(
                            id = expenseId,
                            titleEn = if (titleEn.isNotEmpty()) titleEn else "Expense Item",
                            titleBn = if (titleBn.isNotEmpty()) titleBn else "খরচ বিবরণী",
                            category = category,
                            categoryBn = category,
                            amount = amount,
                            date = "2025-02-20",
                            vendor = vendor,
                            paymentMethod = paymentMethod,
                            description = description,
                            addedBy = "Md. Eliyas Akondo (Admin)",
                            approvedBy = "Approved"
                        )
                        OrganizationRepository.addExpense(newExpense)
                        onSuccess()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = StatusError)
            ) {
                Text(if (lang == Language.BN) "খরচ যুক্ত করুন" else "Log Expense")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Strings.btnCancel[lang]!!)
            }
        }
    )
}
