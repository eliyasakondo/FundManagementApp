package com.eliyas.fundmanagementapp.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.model.OrganizationProfile
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.theme.*

@Composable
fun SettingsScreen() {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val managerPerms by OrganizationRepository.managerPermissions.collectAsState()
    val privacySettings by OrganizationRepository.privacySettings.collectAsState()
    val orgProfile by OrganizationRepository.orgProfile.collectAsState()

    var isEditMode by remember { mutableStateOf(false) }

    var editNameBn by remember(orgProfile) { mutableStateOf(orgProfile.nameBn) }
    var editNameEn by remember(orgProfile) { mutableStateOf(orgProfile.nameEn) }
    var editRegNo by remember(orgProfile) { mutableStateOf(orgProfile.regNumber) }
    var editFiscalYear by remember(orgProfile) { mutableStateOf(orgProfile.fiscalYear) }
    var editCurrency by remember(orgProfile) { mutableStateOf(orgProfile.currencySymbol) }
    var editEmail by remember(orgProfile) { mutableStateOf(orgProfile.contactEmail) }
    var editDefaultRateText by remember(orgProfile) { mutableStateOf(orgProfile.defaultMonthlyRate.toInt().toString()) }
    var editGracePeriodText by remember(orgProfile) { mutableStateOf(orgProfile.gracePeriodDays.toString()) }

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
                text = if (lang == Language.BN) "এডমিন সিস্টেম সেটিংস ও প্রোফাইল কন্ট্রোল" else "Admin System Settings & Profile Control",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
            Text(
                text = if (lang == Language.BN) "সংগঠনের প্রোফাইল ভিউ, এডিটর ও পারমিশন কন্ট্রোল" else "View & Edit Organization Profile, Financial Policies & Permissions",
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
                            text = if (lang == Language.BN) "সংগঠনের প্রোফাইল সফলভাবে আপডেট করা হয়েছে!" else "Organization Profile updated successfully!",
                            color = StatusSuccess,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Business, contentDescription = null, tint = PrimaryGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (lang == Language.BN) "সংগঠনের প্রোফাইল ও তথ্য" else "Organization Profile & Info",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = PrimaryGreen
                            )
                        }

                        if (!isEditMode) {
                            OutlinedButton(
                                onClick = { isEditMode = true },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = PrimaryGreen)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (lang == Language.BN) "সম্পাদনা করুন" else "Edit Profile", fontSize = 12.sp, color = PrimaryGreen)
                            }
                        }
                    }

                    if (!isEditMode) {
                        Text(
                            text = if (lang == Language.BN)
                                "• সংগঠনের নাম: ${orgProfile.nameBn}\n• রেজিস্ট্রেশন নং: ${orgProfile.regNumber}\n• অর্থবছর: ${orgProfile.fiscalYear}\n• কারেন্সি চিহ্ন: ${orgProfile.currencySymbol}\n• ইমেইল: ${orgProfile.contactEmail}\n• ডিফল্ট চাঁদার হার: ${Strings.formatCurrency(orgProfile.defaultMonthlyRate, lang)} / মাস\n• বকেয়া গ্রেস পিরিয়ড: ${Strings.formatDigits(orgProfile.gracePeriodDays.toString(), lang)} দিন"
                            else
                                "• Org Name: ${orgProfile.nameEn}\n• Reg No: ${orgProfile.regNumber}\n• Fiscal Year: ${orgProfile.fiscalYear}\n• Currency Symbol: ${orgProfile.currencySymbol}\n• Email: ${orgProfile.contactEmail}\n• Default Monthly Rate: ${Strings.formatCurrency(orgProfile.defaultMonthlyRate, lang)} / month\n• Grace Period: ${Strings.formatDigits(orgProfile.gracePeriodDays.toString(), lang)} Days",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 22.sp
                        )
                    } else {
                        OutlinedTextField(
                            value = editNameBn,
                            onValueChange = { editNameBn = it },
                            label = { Text(if (lang == Language.BN) "সংগঠনের নাম (বাংলা)" else "Org Name (Bangla)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = editNameEn,
                            onValueChange = { editNameEn = it },
                            label = { Text(if (lang == Language.BN) "সংগঠনের নাম (ইংরেজি)" else "Org Name (English)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = editRegNo,
                                onValueChange = { editRegNo = it },
                                label = { Text(if (lang == Language.BN) "রেজিস্ট্রেশন নং" else "Reg Number") },
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = editFiscalYear,
                                onValueChange = { editFiscalYear = it },
                                label = { Text(if (lang == Language.BN) "অর্থবছর" else "Fiscal Year") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = editCurrency,
                                onValueChange = { editCurrency = it },
                                label = { Text(if (lang == Language.BN) "মুদ্রা চিহ্ন" else "Currency Symbol") },
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = editEmail,
                                onValueChange = { editEmail = it },
                                label = { Text(if (lang == Language.BN) "অফিশিয়াল ইমেইল" else "Contact Email") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = editDefaultRateText,
                                onValueChange = { editDefaultRateText = it },
                                label = { Text(if (lang == Language.BN) "ডিফল্ট চাঁদা (৳)" else "Default Rate (৳)") },
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = editGracePeriodText,
                                onValueChange = { editGracePeriodText = it },
                                label = { Text(if (lang == Language.BN) "গ্রেস পিরিয়ড (দিন)" else "Grace Period (Days)") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { isEditMode = false },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(Strings.btnCancel[lang]!!)
                            }

                            Button(
                                onClick = {
                                    val updated = OrganizationProfile(
                                        nameBn = editNameBn,
                                        nameEn = editNameEn,
                                        regNumber = editRegNo,
                                        fiscalYear = editFiscalYear,
                                        currencySymbol = editCurrency,
                                        contactEmail = editEmail,
                                        defaultMonthlyRate = editDefaultRateText.toDoubleOrNull() ?: 1000.0,
                                        gracePeriodDays = editGracePeriodText.toIntOrNull() ?: 10
                                    )
                                    OrganizationRepository.updateOrganizationProfile(updated)
                                    isEditMode = false
                                    isSavedNoticeVisible = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (lang == Language.BN) "সংরক্ষণ করুন" else "Save Changes")
                            }
                        }
                    }
                }
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

                    PermissionToggleRow(
                        title = if (lang == Language.BN) "সংগঠনের খরচ যুক্ত করার অনুমতি" else "Allow Managers to Record Expenses",
                        subtitle = if (lang == Language.BN) "বন্ধ করলে ম্যানেজার ভিউতে খরচ অপশন থাকবে না (এডমিনে প্রভাব ফেলবে না)" else "Disabling removes expense entry ONLY for Manager role",
                        isChecked = managerPerms.canAddExpense,
                        onCheckedChange = { isChecked: Boolean ->
                            OrganizationRepository.updateManagerPermissions(managerPerms.copy(canAddExpense = isChecked))
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftBackground)

                    PermissionToggleRow(
                        title = if (lang == Language.BN) "পেমেন্ট সরাসরি অনুমোদন করার অনুমতি" else "Allow Managers to Approve Payments",
                        subtitle = if (lang == Language.BN) "বন্ধ করলে ম্যানেজারের এন্ট্রি এডমিন এপ্রুভালের জন্য পেন্ডিং থাকবে" else "Disabling requires Admin approval for Manager payment logs",
                        isChecked = managerPerms.canApprovePayments,
                        onCheckedChange = { isChecked: Boolean ->
                            OrganizationRepository.updateManagerPermissions(managerPerms.copy(canApprovePayments = isChecked))
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftBackground)

                    PermissionToggleRow(
                        title = if (lang == Language.BN) "সকল আর্থিক রিপোর্ট দেখার অনুমতি" else "Allow Managers to View All Reports",
                        subtitle = if (lang == Language.BN) "বন্ধ করলে ম্যানেজাররা বিস্তারিত রিপোর্ট দেখতে পারবে না" else "Disabling restricts detailed report screens for Manager role",
                        isChecked = managerPerms.canViewAllReports,
                        onCheckedChange = { isChecked: Boolean ->
                            OrganizationRepository.updateManagerPermissions(managerPerms.copy(canViewAllReports = isChecked))
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftBackground)

                    PermissionToggleRow(
                        title = if (lang == Language.BN) "সদস্যদের নোটিফিকেশন পাঠানোর অনুমতি" else "Allow Managers to Send Notifications",
                        subtitle = if (lang == Language.BN) "বন্ধ করলে ম্যানেজাররা নোটিফিকেশন ও বার্তা পাঠাতে পারবে না" else "Disabling restricts broadcast notices for Manager role",
                        isChecked = managerPerms.canSendNotification,
                        onCheckedChange = { isChecked: Boolean ->
                            OrganizationRepository.updateManagerPermissions(managerPerms.copy(canSendNotification = isChecked))
                        }
                    )
                }
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

                    PermissionToggleRow(
                        title = if (lang == Language.BN) "তালিকায় সদস্যের নাম প্রকাশ করা" else "Show Member Names Publicly",
                        subtitle = if (lang == Language.BN) "বন্ধ করলে মেম্বার ভিউতে নামগুলো গোপন থাকবে" else "Hides member names ONLY in Member role view",
                        isChecked = privacySettings.showMemberName,
                        onCheckedChange = { isChecked: Boolean ->
                            OrganizationRepository.updatePrivacySettings(privacySettings.copy(showMemberName = isChecked))
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftBackground)

                    PermissionToggleRow(
                        title = if (lang == Language.BN) "অনুদান ও চাঁদার টাকা প্রকাশ করা" else "Show Donation Amounts Publicly",
                        subtitle = if (lang == Language.BN) "বন্ধ করলে মেম্বাররা টাকার পরিমাণ দেখতে পাবে না" else "Hides transaction amounts ONLY in Member role view",
                        isChecked = privacySettings.showAmount,
                        onCheckedChange = { isChecked: Boolean ->
                            OrganizationRepository.updatePrivacySettings(privacySettings.copy(showAmount = isChecked))
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftBackground)

                    PermissionToggleRow(
                        title = if (lang == Language.BN) "সম্পূর্ণ কন্ট্রিবিউশন হিস্ট্রি দেখানো" else "Show Full Contribution History",
                        subtitle = if (lang == Language.BN) "মেম্বাররা বিগত বছরের সম্পূর্ণ চাঁদার হিসাব দেখতে পাবে" else "Allow members to browse full historical logs",
                        isChecked = privacySettings.showFullContributionHistory,
                        onCheckedChange = { isChecked: Boolean ->
                            OrganizationRepository.updatePrivacySettings(privacySettings.copy(showFullContributionHistory = isChecked))
                        }
                    )
                }
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
