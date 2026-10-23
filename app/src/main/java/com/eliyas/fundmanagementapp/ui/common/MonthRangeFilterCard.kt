package com.eliyas.fundmanagementapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
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
import com.eliyas.fundmanagementapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthRangeFilterCard(
    startMonth: String,
    endMonth: String,
    onRangeSelected: (start: String, end: String) -> Unit,
    lang: Language,
    modifier: Modifier = Modifier
) {
    val monthsList = listOf(
        "2025-01" to if (lang == Language.BN) "জানুয়ারি ২০২৫" else "Jan 2025",
        "2025-02" to if (lang == Language.BN) "ফেব্রুয়ারি ২০২৫" else "Feb 2025",
        "2025-03" to if (lang == Language.BN) "মার্চ ২০২৫" else "Mar 2025",
        "2025-04" to if (lang == Language.BN) "এপ্রিল ২০২৫" else "Apr 2025",
        "2025-05" to if (lang == Language.BN) "মে ২০২৫" else "May 2025",
        "2025-06" to if (lang == Language.BN) "জুন ২০২৫" else "Jun 2025",
        "2025-07" to if (lang == Language.BN) "জুলাই ২০২৫" else "Jul 2025",
        "2025-08" to if (lang == Language.BN) "আগস্ট ২০২৫" else "Aug 2025",
        "2025-09" to if (lang == Language.BN) "সেপ্টেম্বর ২০২৫" else "Sep 2025",
        "2025-10" to if (lang == Language.BN) "অক্টোবর ২০২৫" else "Oct 2025",
        "2025-11" to if (lang == Language.BN) "নভেম্বর ২০২৫" else "Nov 2025",
        "2025-12" to if (lang == Language.BN) "ডিসেম্বর ২০২৫" else "Dec 2025"
    )

    var startExpanded by remember { mutableStateOf(false) }
    var endExpanded by remember { mutableStateOf(false) }
    var activePreset by remember { mutableStateOf("ALL") }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = PrimaryGreen)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (lang == Language.BN) "মাসভিত্তিক সময়কাল ফিল্টার" else "Filter by Month Range",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PrimaryGreen.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${Strings.getMonthName(startMonth, lang)} - ${Strings.getMonthName(endMonth, lang)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    )
                }
            }

            // Quick Preset Filter Chips Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    Triple("ALL", "সব মাস", "All Months"),
                    Triple("CURR", "চলতি মাস", "Current Month"),
                    Triple("3M", "গত ৩ মাস", "Last 3 Months"),
                    Triple("6M", "গত ৬ মাস", "Last 6 Months")
                ).forEach { (presetKey, labelBn, labelEn) ->
                    val isSelected = activePreset == presetKey
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            activePreset = presetKey
                            when (presetKey) {
                                "ALL" -> onRangeSelected("2025-01", "2025-12")
                                "CURR" -> onRangeSelected("2025-01", "2025-01")
                                "3M" -> onRangeSelected("2025-01", "2025-03")
                                "6M" -> onRangeSelected("2025-01", "2025-06")
                            }
                        },
                        label = { Text(if (lang == Language.BN) labelBn else labelEn, fontSize = 11.sp) }
                    )
                }
            }

            HorizontalDivider(color = SoftBackground)

            // Custom Start Month & End Month Dropdowns
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Start Month Dropdown
                ExposedDropdownMenuBox(
                    expanded = startExpanded,
                    onExpandedChange = { startExpanded = !startExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = Strings.getMonthName(startMonth, lang),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (lang == Language.BN) "শুরু মাস" else "Start Month", fontSize = 11.sp) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = startExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
                    )

                    ExposedDropdownMenu(
                        expanded = startExpanded,
                        onDismissRequest = { startExpanded = false }
                    ) {
                        monthsList.forEach { (key, name) ->
                            DropdownMenuItem(
                                text = { Text(name, fontSize = 12.sp) },
                                onClick = {
                                    activePreset = "CUSTOM"
                                    startExpanded = false
                                    onRangeSelected(key, maxOf(key, endMonth))
                                }
                            )
                        }
                    }
                }

                // End Month Dropdown
                ExposedDropdownMenuBox(
                    expanded = endExpanded,
                    onExpandedChange = { endExpanded = !endExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = Strings.getMonthName(endMonth, lang),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (lang == Language.BN) "শেষ মাস" else "End Month", fontSize = 11.sp) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = endExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
                    )

                    ExposedDropdownMenu(
                        expanded = endExpanded,
                        onDismissRequest = { endExpanded = false }
                    ) {
                        monthsList.forEach { (key, name) ->
                            DropdownMenuItem(
                                text = { Text(name, fontSize = 12.sp) },
                                onClick = {
                                    activePreset = "CUSTOM"
                                    endExpanded = false
                                    onRangeSelected(minOf(startMonth, key), key)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
