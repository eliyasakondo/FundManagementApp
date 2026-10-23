package com.eliyas.fundmanagementapp.ui.members

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
fun MembersScreen(onOpenAddMember: () -> Unit) {
    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val members by OrganizationRepository.members.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedMemberForView by remember { mutableStateOf<Member?>(null) }
    var selectedMemberForEdit by remember { mutableStateOf<Member?>(null) }
    var selectedMemberForRateChange by remember { mutableStateOf<Member?>(null) }

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

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = SoftBackground)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OutlinedButton(
                                onClick = { selectedMemberForView = member },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp), tint = PrimaryGreen)
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(if (lang == Language.BN) "দেখুন" else "View", fontSize = 11.sp, color = PrimaryGreen)
                            }

                            OutlinedButton(
                                onClick = { selectedMemberForEdit = member },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = PrimaryGreen)
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(if (lang == Language.BN) "এডিট" else "Edit", fontSize = 11.sp, color = PrimaryGreen)
                            }

                            OutlinedButton(
                                onClick = { selectedMemberForRateChange = member },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.AttachMoney, contentDescription = null, modifier = Modifier.size(14.dp), tint = WarmGold)
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(if (lang == Language.BN) "রেট" else "Rate", fontSize = 11.sp, color = DarkGreen)
                            }

                            IconButton(
                                onClick = { OrganizationRepository.toggleMemberStatus(member.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (member.status == "Active") Icons.Default.LockOpen else Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = if (member.status == "Active") StatusSuccess else StatusError,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (selectedMemberForView != null) {
        ViewMemberProfileModal(
            member = selectedMemberForView!!,
            onDismiss = { selectedMemberForView = null }
        )
    }

    if (selectedMemberForEdit != null) {
        EditMemberModal(
            member = selectedMemberForEdit!!,
            onDismiss = { selectedMemberForEdit = null },
            onSuccess = { selectedMemberForEdit = null }
        )
    }

    if (selectedMemberForRateChange != null) {
        ChangeMonthlyRateModal(
            member = selectedMemberForRateChange!!,
            onDismiss = { selectedMemberForRateChange = null },
            onSuccess = { selectedMemberForRateChange = null }
        )
    }
}
