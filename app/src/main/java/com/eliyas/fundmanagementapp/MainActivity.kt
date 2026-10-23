package com.eliyas.fundmanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.model.UserRole
import com.eliyas.fundmanagementapp.repository.OrganizationRepository
import com.eliyas.fundmanagementapp.ui.common.BottomNavigationBar
import com.eliyas.fundmanagementapp.ui.common.NotificationBottomSheet
import com.eliyas.fundmanagementapp.ui.common.RoleSwitcherBar
import com.eliyas.fundmanagementapp.ui.common.TopBarHeader
import com.eliyas.fundmanagementapp.ui.dashboard.DashboardScreen
import com.eliyas.fundmanagementapp.ui.expenses.ExpensesScreen
import com.eliyas.fundmanagementapp.ui.expenses.RecordExpenseModal
import com.eliyas.fundmanagementapp.ui.member.MemberHomeScreen
import com.eliyas.fundmanagementapp.ui.member.MemberProfileScreen
import com.eliyas.fundmanagementapp.ui.member.MyContributionDetailsScreen
import com.eliyas.fundmanagementapp.ui.member.NotificationsScreen
import com.eliyas.fundmanagementapp.ui.members.AddMemberModal
import com.eliyas.fundmanagementapp.ui.members.MembersScreen
import com.eliyas.fundmanagementapp.ui.payments.PaymentsScreen
import com.eliyas.fundmanagementapp.ui.payments.RecordPaymentModal
import com.eliyas.fundmanagementapp.ui.reports.ReportsScreen
import com.eliyas.fundmanagementapp.ui.screens.SplashScreen
import com.eliyas.fundmanagementapp.ui.settings.SettingsScreen
import com.eliyas.fundmanagementapp.ui.theme.FundManagementTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FundManagementTheme {
                MainAppContent()
            }
        }
    }
}

@Composable
fun MainAppContent() {
    var isSplashActive by remember { mutableStateOf(true) }
    var currentTab by remember { mutableStateOf("home") }
    var showRecordPaymentModal by remember { mutableStateOf(false) }
    var showRecordExpenseModal by remember { mutableStateOf(false) }
    var showAddMemberModal by remember { mutableStateOf(false) }
    var showNotificationBottomSheet by remember { mutableStateOf(false) }

    val lang by OrganizationRepository.currentLanguage.collectAsState()
    val role by OrganizationRepository.currentUserRole.collectAsState()
    val notifications by OrganizationRepository.notifications.collectAsState()
    val unreadCount = notifications.count { !it.isRead }

    if (isSplashActive) {
        SplashScreen(
            onStartClick = { isSplashActive = false }
        )
    } else {
        Scaffold(
            topBar = {
                Column {
                    RoleSwitcherBar(
                        currentRole = role,
                        currentLang = lang,
                        onRoleChange = { newRole ->
                            OrganizationRepository.setUserRole(newRole)
                            currentTab = if (newRole == UserRole.MEMBER) "home" else "dashboard"
                        },
                        onLangToggle = {
                            val nextLang = if (lang == Language.BN)
                                Language.EN else Language.BN
                            OrganizationRepository.setLanguage(nextLang)
                        }
                    )

                    TopBarHeader(
                        title = Strings.appName[lang]!!,
                        unreadNotifications = unreadCount,
                        onNotificationClick = {
                            showNotificationBottomSheet = true
                        },
                        onRoleToggleClick = {},
                        currentLang = lang
                    )
                }
            },
            bottomBar = {
                BottomNavigationBar(
                    selectedTab = currentTab,
                    onTabSelected = { currentTab = it },
                    currentLang = lang,
                    currentRole = role
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (role == UserRole.MEMBER) {
                    when (currentTab) {
                        "home" -> MemberHomeScreen(onNavigateToTab = { currentTab = it })
                        "history" -> MyContributionDetailsScreen()
                        "members" -> ReportsScreen()
                        "messages" -> NotificationsScreen()
                        "profile" -> MemberProfileScreen()
                        else -> MemberHomeScreen(onNavigateToTab = { currentTab = it })
                    }
                } else {
                    when (currentTab) {
                        "dashboard" -> DashboardScreen(
                            onNavigate = { currentTab = it },
                            onOpenRecordPayment = { showRecordPaymentModal = true },
                            onOpenAddMember = { showAddMemberModal = true }
                        )
                        "members" -> MembersScreen(
                            onOpenAddMember = { showAddMemberModal = true }
                        )
                        "payments" -> PaymentsScreen(
                            onOpenRecordPayment = { showRecordPaymentModal = true }
                        )
                        "expenses" -> ExpensesScreen(
                            onOpenRecordExpense = { showRecordExpenseModal = true }
                        )
                        "reports" -> ReportsScreen()
                        "settings" -> {
                            if (role == UserRole.ADMIN) {
                                SettingsScreen()
                            } else {
                                MemberProfileScreen()
                            }
                        }
                        else -> DashboardScreen(
                            onNavigate = { currentTab = it },
                            onOpenRecordPayment = { showRecordPaymentModal = true },
                            onOpenAddMember = { showAddMemberModal = true }
                        )
                    }
                }

                if (showNotificationBottomSheet) {
                    NotificationBottomSheet(
                        onDismiss = { showNotificationBottomSheet = false }
                    )
                }

                if (showRecordPaymentModal) {
                    RecordPaymentModal(
                        onDismiss = { showRecordPaymentModal = false },
                        onSuccess = { showRecordPaymentModal = false }
                    )
                }

                if (showRecordExpenseModal) {
                    RecordExpenseModal(
                        onDismiss = { showRecordExpenseModal = false },
                        onSuccess = { showRecordExpenseModal = false }
                    )
                }

                if (showAddMemberModal) {
                    AddMemberModal(
                        onDismiss = { showAddMemberModal = false },
                        onSuccess = { showAddMemberModal = false }
                    )
                }
            }
        }
    }
}
