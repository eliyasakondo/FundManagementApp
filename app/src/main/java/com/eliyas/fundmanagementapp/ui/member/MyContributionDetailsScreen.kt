package com.eliyas.fundmanagementapp.ui.member

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.model.PaymentStatus
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.common.StatusBadge
import com.eliyas.fundmanagementapp.ui.theme.*

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
