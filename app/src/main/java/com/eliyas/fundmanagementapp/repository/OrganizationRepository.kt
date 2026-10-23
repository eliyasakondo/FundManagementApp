package com.eliyas.fundmanagementapp.repository

import android.content.Context
import com.eliyas.fundmanagementapp.data.DummyData
import com.eliyas.fundmanagementapp.data.local.AppDatabase
import com.eliyas.fundmanagementapp.data.local.DatabaseProvider
import com.eliyas.fundmanagementapp.data.local.entity.*
import com.eliyas.fundmanagementapp.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object OrganizationRepository {

    private val scope = CoroutineScope(Dispatchers.IO)
    private var db: AppDatabase? = null

    private val _currentLanguage = MutableStateFlow(Language.BN)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private val _currentUserRole = MutableStateFlow(UserRole.MEMBER)
    val currentUserRole: StateFlow<UserRole> = _currentUserRole.asStateFlow()

    private val _members = MutableStateFlow<List<Member>>(emptyList())
    val members: StateFlow<List<Member>> = _members.asStateFlow()

    private val _currentMember = MutableStateFlow<Member?>(null)
    val currentMember: StateFlow<Member?> = _currentMember.asStateFlow()

    private val _memberHistory = MutableStateFlow<Map<String, List<MonthlyRecord>>>(emptyMap())
    val memberHistory: StateFlow<Map<String, List<MonthlyRecord>>> = _memberHistory.asStateFlow()

    private val _paymentReceipts = MutableStateFlow<List<PaymentRecord>>(emptyList())
    val paymentReceipts: StateFlow<List<PaymentRecord>> = _paymentReceipts.asStateFlow()

    private val _donations = MutableStateFlow<List<DonationRecord>>(emptyList())
    val donations: StateFlow<List<DonationRecord>> = _donations.asStateFlow()

    private val _income = MutableStateFlow<List<IncomeRecord>>(emptyList())
    val income: StateFlow<List<IncomeRecord>> = _income.asStateFlow()

    private val _expenses = MutableStateFlow<List<ExpenseRecord>>(emptyList())
    val expenses: StateFlow<List<ExpenseRecord>> = _expenses.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _messages = MutableStateFlow<List<MessageItem>>(emptyList())
    val messages: StateFlow<List<MessageItem>> = _messages.asStateFlow()

    private val _privacySettings = MutableStateFlow(PrivacySettings())
    val privacySettings: StateFlow<PrivacySettings> = _privacySettings.asStateFlow()

    private val _managerPermissions = MutableStateFlow(ManagerPermissions())
    val managerPermissions: StateFlow<ManagerPermissions> = _managerPermissions.asStateFlow()

    private val _orgProfile = MutableStateFlow(OrganizationProfile())
    val orgProfile: StateFlow<OrganizationProfile> = _orgProfile.asStateFlow()

    init {
        // Load Initial Seed Data
        val initialMembers = DummyData.generateMembers()
        _members.value = initialMembers
        _currentMember.value = initialMembers.firstOrNull { it.id == "MEM-1001" } ?: initialMembers.first()

        val initialHistory = DummyData.generateMemberPaymentHistory(initialMembers)
        _memberHistory.value = initialHistory

        _paymentReceipts.value = DummyData.generatePaymentReceipts(initialMembers)
        _donations.value = DummyData.generateDonations(initialMembers)
        _income.value = DummyData.generateIncome()
        _expenses.value = DummyData.generateExpenses()
        _notifications.value = DummyData.generateNotifications()
        _messages.value = DummyData.generateMessages()
    }

    fun initializeDatabase(context: Context) {
        if (db != null) return
        db = DatabaseProvider.getDatabase(context)

        scope.launch {
            db?.let { localDb ->
                // Collect Room DB Expenses
                localDb.expenseDao().getAllExpenses().collect { entities ->
                    if (entities.isNotEmpty()) {
                        _expenses.value = entities.map { e ->
                            ExpenseRecord(
                                id = e.id,
                                titleEn = e.titleEn,
                                titleBn = e.titleBn,
                                category = e.category,
                                categoryBn = e.categoryBn,
                                amount = e.amount,
                                date = e.date,
                                vendor = e.vendor,
                                paymentMethod = e.paymentMethod,
                                description = e.description,
                                addedBy = e.addedBy,
                                approvedBy = e.approvedBy,
                                isPublic = e.isPublic
                            )
                        }
                    }
                }
            }
        }
    }

    fun setLanguage(lang: Language) {
        _currentLanguage.value = lang
    }

    fun setUserRole(role: UserRole) {
        _currentUserRole.value = role
        if (role == UserRole.ADMIN) {
            _currentMember.value = _members.value.find { it.id == "ADM-1001" }
        } else if (role == UserRole.MANAGER) {
            _currentMember.value = _members.value.find { it.id == "MGR-1002" }
        } else {
            _currentMember.value = _members.value.find { it.id == "MEM-1001" } ?: _members.value.firstOrNull()
        }
    }

    fun login(usernameOrId: String, pass: String): Boolean {
        val member = _members.value.find {
            (it.username.equals(usernameOrId, ignoreCase = true) ||
             it.id.equals(usernameOrId, ignoreCase = true) ||
             it.mobile == usernameOrId) && it.password == pass
        }
        if (member != null) {
            _currentMember.value = member
            if (member.id.startsWith("ADM")) {
                _currentUserRole.value = UserRole.ADMIN
            } else if (member.id.startsWith("MGR")) {
                _currentUserRole.value = UserRole.MANAGER
            } else {
                _currentUserRole.value = UserRole.MEMBER
            }
            return true
        }
        return false
    }

    fun updatePassword(newPass: String) {
        val current = _currentMember.value ?: return
        val updated = current.copy(password = newPass, mustChangePassword = false)
        _currentMember.value = updated
        _members.value = _members.value.map { if (it.id == updated.id) updated else it }
    }

    fun updateMemberProfile(updatedMember: Member) {
        _members.value = _members.value.map { if (it.id == updatedMember.id) updatedMember else it }
        if (_currentMember.value?.id == updatedMember.id) {
            _currentMember.value = updatedMember
        }
    }

    fun recordPayment(
        memberId: String,
        paymentDate: String,
        totalReceived: Double,
        selectedMonths: List<String>,
        contribAmount: Double,
        donationAmount: Double,
        method: String,
        reference: String,
        notes: String
    ): PaymentRecord {
        val member = _members.value.find { it.id == memberId } ?: return PaymentRecord(
            "", memberId, "", "", paymentDate, totalReceived, contribAmount, donationAmount, selectedMonths, method, reference, "Admin", "Admin", "Cancelled", notes
        )

        val receiptNo = "RCP-${SimpleDateFormat("yyyy", Locale.US).format(Date())}-${1000 + _paymentReceipts.value.size + 1}"
        val record = PaymentRecord(
            receiptNo = receiptNo,
            memberId = member.id,
            memberNameEn = member.nameEn,
            memberNameBn = member.nameBn,
            paymentDate = paymentDate,
            totalReceived = totalReceived,
            contributionAmount = contribAmount,
            donationAmount = donationAmount,
            coveredMonths = selectedMonths,
            paymentMethod = method,
            reference = reference,
            recordedBy = if (_currentUserRole.value == UserRole.ADMIN) "Admin" else "Manager",
            approvedBy = "Central Accounts",
            status = "Approved",
            notes = notes
        )

        _paymentReceipts.value = listOf(record) + _paymentReceipts.value

        val currentHist = _memberHistory.value[memberId]?.toMutableList() ?: mutableListOf()
        val perMonthAmount = if (selectedMonths.isNotEmpty()) contribAmount / selectedMonths.size else 0.0

        for (mKey in selectedMonths) {
            val idx = currentHist.indexOfFirst { it.monthKey == mKey }
            if (idx >= 0) {
                val old = currentHist[idx]
                val newPaid = old.paidAmount + perMonthAmount
                val newStatus = when {
                    newPaid >= old.requiredAmount -> PaymentStatus.PAID
                    newPaid > 0 -> PaymentStatus.PARTIALLY_PAID
                    else -> PaymentStatus.UNPAID
                }
                currentHist[idx] = old.copy(
                    paidAmount = newPaid,
                    status = newStatus,
                    lastPaymentDate = paymentDate
                )
            } else {
                val newStatus = if (perMonthAmount >= member.monthlyContribution) PaymentStatus.PAID_IN_ADVANCE else PaymentStatus.PARTIALLY_PAID
                currentHist.add(
                    MonthlyRecord(
                        monthKey = mKey,
                        monthNameEn = mKey,
                        monthNameBn = mKey,
                        requiredAmount = member.monthlyContribution,
                        paidAmount = perMonthAmount,
                        status = newStatus,
                        lastPaymentDate = paymentDate
                    )
                )
            }
        }
        val newMap = _memberHistory.value.toMutableMap()
        newMap[memberId] = currentHist
        _memberHistory.value = newMap

        if (donationAmount > 0) {
            val donRecord = DonationRecord(
                id = "DON-${6000 + _donations.value.size + 1}",
                donorType = DonorType.MEMBER,
                donorNameEn = member.nameEn,
                donorNameBn = member.nameBn,
                memberId = member.id,
                amount = donationAmount,
                date = paymentDate,
                paymentMethod = method,
                purpose = "Extra Member Donation",
                isPublic = true,
                showDonorName = true,
                notes = "Auto-allocated extra payment from $receiptNo"
            )
            _donations.value = listOf(donRecord) + _donations.value
        }

        val notif = NotificationItem(
            id = "NOT-${System.currentTimeMillis()}",
            memberId = member.id,
            titleEn = "Payment Confirmed - $receiptNo",
            titleBn = "পেমেন্ট নিশ্চিত করা হয়েছে - $receiptNo",
            messageEn = "A payment of ৳${String.format("%.0f", totalReceived)} was recorded for ${selectedMonths.joinToString(", ")}.",
            messageBn = "৳${String.format("%.0f", totalReceived)} টাকা জমা সফলভাবে রসিদ $receiptNo তে অন্তর্ভুক্ত করা হয়েছে।",
            timestamp = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US).format(Date()),
            isRead = false,
            relatedReceiptNo = receiptNo
        )
        _notifications.value = listOf(notif) + _notifications.value

        // Persist to Room SQLite
        scope.launch {
            db?.let { localDb ->
                localDb.paymentDao().insertPayment(
                    PaymentEntity(
                        id = record.receiptNo,
                        receiptNumber = record.receiptNo,
                        memberId = record.memberId,
                        paymentDate = record.paymentDate,
                        totalAmountReceived = record.totalReceived,
                        contributionPortion = record.contributionAmount,
                        donationPortion = record.donationAmount,
                        paymentMethod = record.paymentMethod,
                        transactionReference = record.reference,
                        recordedBy = record.recordedBy,
                        approvalStatus = record.status,
                        notes = record.notes
                    )
                )
            }
        }

        return record
    }

    fun addMember(newMember: Member) {
        _members.value = listOf(newMember) + _members.value

        val months = listOf("2025-01", "2025-02", "2025-03", "2025-04", "2025-05", "2025-06")
        val recs = months.map {
            MonthlyRecord(it, it, it, newMember.monthlyContribution, 0.0, 0.0, PaymentStatus.UNPAID, null)
        }
        val map = _memberHistory.value.toMutableMap()
        map[newMember.id] = recs
        _memberHistory.value = map

        scope.launch {
            db?.let { localDb ->
                localDb.memberDao().insertMember(
                    MemberEntity(
                        id = newMember.id,
                        memberId = newMember.id,
                        fullNameBn = newMember.nameBn,
                        fullNameEn = newMember.nameEn,
                        mobileNumber = newMember.mobile,
                        altMobileNumber = newMember.altMobile,
                        email = newMember.email,
                        designation = newMember.designation,
                        monthlyContribution = newMember.monthlyContribution,
                        accountStatus = newMember.status
                    )
                )
            }
        }
    }

    fun updateMonthlyContribution(memberId: String, newAmount: Double, effectiveDate: String) {
        val member = _members.value.find { it.id == memberId } ?: return
        val updated = member.copy(
            previousContribution = member.monthlyContribution,
            monthlyContribution = newAmount,
            contributionEffectiveDate = effectiveDate
        )
        updateMemberProfile(updated)
    }

    fun addExpense(expense: ExpenseRecord) {
        _expenses.value = listOf(expense) + _expenses.value

        scope.launch {
            db?.let { localDb ->
                localDb.expenseDao().insertExpense(
                    ExpenseEntity(
                        id = expense.id,
                        titleEn = expense.titleEn,
                        titleBn = expense.titleBn,
                        category = expense.category,
                        categoryBn = expense.categoryBn,
                        amount = expense.amount,
                        date = expense.date,
                        vendor = expense.vendor,
                        paymentMethod = expense.paymentMethod,
                        description = expense.description,
                        addedBy = expense.addedBy,
                        approvedBy = expense.approvedBy,
                        isPublic = expense.isPublic
                    )
                )
            }
        }
    }

    fun addIncome(inc: IncomeRecord) {
        _income.value = listOf(inc) + _income.value
    }

    fun sendMessage(receiverId: String, text: String) {
        val sender = _currentMember.value ?: return
        val msg = MessageItem(
            id = "MSG-${System.currentTimeMillis()}",
            senderId = sender.id,
            senderName = if (_currentLanguage.value == Language.BN) sender.nameBn else sender.nameEn,
            senderRole = _currentUserRole.value,
            receiverId = receiverId,
            messageText = text,
            timestamp = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US).format(Date()),
            isRead = false
        )
        _messages.value = listOf(msg) + _messages.value
    }

    fun markNotificationRead(id: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    fun markAllNotificationsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
        scope.launch {
            db?.let { localDb ->
                localDb.notificationDao().markAllRead()
            }
        }
    }

    fun updatePrivacySettings(settings: PrivacySettings) {
        _privacySettings.value = settings
    }

    fun updateManagerPermissions(perms: ManagerPermissions) {
        _managerPermissions.value = perms
    }

    fun updateOrganizationProfile(profile: OrganizationProfile) {
        _orgProfile.value = profile
    }

    fun resetPassword(memberId: String): String {
        val temp = "pass1234"
        val member = _members.value.find { it.id == memberId } ?: return ""
        val updated = member.copy(password = temp, mustChangePassword = true)
        updateMemberProfile(updated)
        return temp
    }

    fun toggleMemberStatus(memberId: String) {
        val member = _members.value.find { it.id == memberId } ?: return
        val newStatus = if (member.status == "Active") "Inactive" else "Active"
        updateMemberProfile(member.copy(status = newStatus))
    }
}
