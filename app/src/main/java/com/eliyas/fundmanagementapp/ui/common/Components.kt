package com.eliyas.fundmanagementapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.model.PaymentStatus
import com.eliyas.fundmanagementapp.model.UserRole
import com.eliyas.fundmanagementapp.ui.theme.*

@Composable
fun StatusBadge(status: PaymentStatus, lang: Language) {
    val (bgColor, textColor, icon) = when (status) {
        PaymentStatus.PAID -> Triple(StatusSuccess.copy(alpha = 0.15f), StatusSuccess, Icons.Default.CheckCircle)
        PaymentStatus.PARTIALLY_PAID -> Triple(StatusWarning.copy(alpha = 0.15f), Color(0xFFB45309), Icons.Default.PieChart)
        PaymentStatus.PAID_IN_ADVANCE -> Triple(StatusInfo.copy(alpha = 0.15f), StatusInfo, Icons.Default.ElectricBolt)
        PaymentStatus.UNPAID -> Triple(StatusError.copy(alpha = 0.15f), StatusError, Icons.Default.Warning)
        PaymentStatus.OVERDUE -> Triple(StatusError.copy(alpha = 0.2f), StatusError, Icons.Default.Error)
        PaymentStatus.WAIVED -> Triple(Color.Gray.copy(alpha = 0.15f), Color.DarkGray, Icons.Default.RemoveCircle)
        PaymentStatus.ADJUSTED -> Triple(WarmGold.copy(alpha = 0.2f), DarkGreen, Icons.Default.Sync)
        PaymentStatus.CANCELLED -> Triple(Color.Red.copy(alpha = 0.1f), Color.Red, Icons.Default.Close)
    }

    val label = if (lang == Language.BN) status.labelBn else status.labelEn

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = textColor, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RoleSwitcherBar(
    currentRole: UserRole,
    currentLang: Language,
    onRoleChange: (UserRole) -> Unit,
    onLangToggle: () -> Unit
) {
    Surface(
        color = DarkGreen,
        contentColor = SurfaceWhite,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (currentLang == Language.BN) "ভিউ:" else "View:",
                    fontSize = 11.sp,
                    color = WarmGold,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))

                UserRole.values().forEach { role ->
                    val isSelected = currentRole == role
                    val label = when (role) {
                        UserRole.MEMBER -> if (currentLang == Language.BN) "সদস্য" else "Member"
                        UserRole.MANAGER -> if (currentLang == Language.BN) "ম্যানেজার" else "Manager"
                        UserRole.ADMIN -> if (currentLang == Language.BN) "এডমিন" else "Admin"
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) WarmGold else Color.White.copy(alpha = 0.15f))
                            .clickable { onRoleChange(role) }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) DarkGreen else SurfaceWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }

            // Language Toggle
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryGreen)
                    .border(1.dp, WarmGold, RoundedCornerShape(12.dp))
                    .clickable { onLangToggle() }
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = if (currentLang == Language.BN) "English" else "বাংলা",
                    color = SurfaceWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHeader(
    title: String,
    unreadNotifications: Int,
    onNotificationClick: () -> Unit,
    onRoleToggleClick: () -> Unit,
    currentLang: Language
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PrimaryGreen,
            titleContentColor = SurfaceWhite,
            actionIconContentColor = SurfaceWhite
        ),
        actions = {
            IconButton(onClick = onNotificationClick) {
                BadgedBox(
                    badge = {
                        if (unreadNotifications > 0) {
                            Badge(containerColor = WarmGold, contentColor = DarkGreen) {
                                Text(Strings.formatDigits(unreadNotifications.toString(), currentLang))
                            }
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
                }
            }
        }
    )
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String? = null,
    icon: ImageVector,
    containerColor: Color = SurfaceWhite,
    contentColor: Color = TextPrimary,
    accentColor: Color = PrimaryGreen,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 12.sp, color = TextSecondary)
                Text(text = value, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = contentColor)
                if (subtitle != null) {
                    Text(text = subtitle, fontSize = 11.sp, color = accentColor, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    currentLang: Language,
    currentRole: UserRole
) {
    NavigationBar(
        containerColor = SurfaceWhite,
        tonalElevation = 8.dp
    ) {
        val navItems = if (currentRole == UserRole.MEMBER) {
            listOf(
                Triple("home", Strings.navHome[currentLang]!!, Icons.Default.Home),
                Triple("history", Strings.navHistory[currentLang]!!, Icons.Default.ReceiptLong),
                Triple("members", Strings.navMembers[currentLang]!!, Icons.Default.People),
                Triple("messages", Strings.navMessages[currentLang]!!, Icons.Default.Chat),
                Triple("profile", Strings.navProfile[currentLang]!!, Icons.Default.Person)
            )
        } else {
            listOf(
                Triple("dashboard", Strings.navDashboard[currentLang]!!, Icons.Default.Dashboard),
                Triple("members", Strings.navMembers[currentLang]!!, Icons.Default.People),
                Triple("payments", Strings.navPayments[currentLang]!!, Icons.Default.Payments),
                Triple("reports", Strings.navReports[currentLang]!!, Icons.Default.Assessment),
                Triple("settings", Strings.navSettings[currentLang]!!, Icons.Default.Settings)
            )
        }

        navItems.forEach { (route, label, icon) ->
            val isSelected = selectedTab == route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(route) },
                icon = { Icon(imageVector = icon, contentDescription = label) },
                label = { Text(text = label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryGreen,
                    selectedTextColor = PrimaryGreen,
                    indicatorColor = PrimaryGreen.copy(alpha = 0.15f),
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}
