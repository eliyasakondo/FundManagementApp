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
import com.eliyas.fundmanagementapp.ui.common.RoleSwitcherBar
import com.eliyas.fundmanagementapp.ui.common.TopBarHeader
import com.eliyas.fundmanagementapp.ui.screens.*
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
    var showAddMemberModal by remember { mutableStateOf(false) }

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
                            OrganizationRepository.markAllNotificationsRead()
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
                        "members" -> AllMembersContributionHistoryScreen()
                        "messages" -> AllMembersContributionHistoryScreen() // Shared tab
                        "profile" -> MyContributionDetailsScreen() // Profile view
                        else -> MemberHomeScreen(onNavigateToTab = { currentTab = it })
                    }
                } else {
                    when (currentTab) {
                        "dashboard" -> AdminDashboardScreen(
                            onNavigate = { currentTab = it },
                            onOpenRecordPayment = { showRecordPaymentModal = true },
                            onOpenAddMember = { showAddMemberModal = true }
                        )
                        "members" -> MemberManagementScreen(
                            onOpenAddMember = { showAddMemberModal = true }
                        )
                        "payments" -> AdminDashboardScreen(
                            onNavigate = { currentTab = it },
                            onOpenRecordPayment = { showRecordPaymentModal = true },
                            onOpenAddMember = { showAddMemberModal = true }
                        )
                        "reports" -> AllMembersContributionHistoryScreen()
                        "settings" -> MemberManagementScreen(onOpenAddMember = { showAddMemberModal = true })
                        else -> AdminDashboardScreen(
                            onNavigate = { currentTab = it },
                            onOpenRecordPayment = { showRecordPaymentModal = true },
                            onOpenAddMember = { showAddMemberModal = true }
                        )
                    }
                }

                if (showRecordPaymentModal) {
                    RecordPaymentModal(
                        onDismiss = { showRecordPaymentModal = false },
                        onSuccess = { showRecordPaymentModal = false }
                    )
                }
            }
        }
    }
}
