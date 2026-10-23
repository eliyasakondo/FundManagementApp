package com.eliyas.fundmanagementapp.ui.members

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.model.Member
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.theme.*

@Composable
fun ChangeMonthlyRateModal(
    member: Member,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    var newRateText by remember { mutableStateOf(member.monthlyContribution.toInt().toString()) }
    var effectiveDate by remember { mutableStateOf("2025-03-01") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (lang == Language.BN) "মাসিক চাঁদার হার পরিবর্তন" else "Change Monthly Contribution Rate",
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen,
                fontSize = 17.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "${if (lang == Language.BN) "সদস্য:" else "Member:"} ${if (lang == Language.BN) member.nameBn else member.nameEn} (ID: ${member.id})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${if (lang == Language.BN) "বর্তমান হার:" else "Current Rate:"} ${Strings.formatCurrency(member.monthlyContribution, lang)}/মাস",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                OutlinedTextField(
                    value = newRateText,
                    onValueChange = { newRateText = it },
                    label = { Text(if (lang == Language.BN) "নতুন মাসিক চাঁদার পরিমাণ (৳)" else "New Monthly Rate (৳)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = effectiveDate,
                    onValueChange = { effectiveDate = it },
                    label = { Text(if (lang == Language.BN) "কার্যকরের তারিখ (YYYY-MM-DD)" else "Effective Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newRate = newRateText.toDoubleOrNull() ?: member.monthlyContribution
                    OrganizationRepository.updateMonthlyContribution(member.id, newRate, effectiveDate)
                    onSuccess()
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(if (lang == Language.BN) "রেট আপডেট করুন" else "Update Rate")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Strings.btnCancel[lang]!!)
            }
        }
    )
}
