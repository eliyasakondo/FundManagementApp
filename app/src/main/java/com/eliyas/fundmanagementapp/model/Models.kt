package com.eliyas.fundmanagementapp.model

enum class Language {
    EN, BN
}

enum class UserRole {
    ADMIN, MANAGER, MEMBER
}

enum class MembershipType(val labelEn: String, val labelBn: String) {
    GENERAL("General Member", "সাধারণ সদস্য"),
    LIFE("Life Member", "আজীবন সদস্য"),
    EXECUTIVE("Executive Member", "কার্যনির্বাহী সদস্য"),
    SENIOR("Senior Member", "জ্যেষ্ঠ সদস্য"),
    HONORARY("Honorary Member", "সম্মানিত সদস্য")
}

enum class PaymentStatus(val labelEn: String, val labelBn: String) {
    UNPAID("Unpaid", "অপরিশোধিত"),
    PARTIALLY_PAID("Partially Paid", "আংশিক পরিশোধিত"),
    PAID("Paid", "পরিশোধিত"),
    PAID_IN_ADVANCE("Paid in Advance", "অগ্রিম পরিশোধিত"),
    OVERDUE("Overdue", "বকেয়া"),
    WAIVED("Waived", "মওকুফ"),
    ADJUSTED("Adjusted", "সমন্বিত"),
    CANCELLED("Cancelled", "বাতিল")
}

enum class DonorType(val labelEn: String, val labelBn: String) {
    MEMBER("Member", "সদস্য"),
    ANONYMOUS("Anonymous", "বেনামী"),
    EXTERNAL("External Donor", "বহিরাগত দাতা"),
    CAMPAIGN("Campaign", "ক্যাম্পেইন")
}

data class Member(
    val id: String,
    val username: String,
    val nameEn: String,
    val nameBn: String,
    val photoUrl: String = "",
    val mobile: String,
    val altMobile: String = "",
    val email: String,
    val dob: String = "",
    val gender: String = "Male",
    val profession: String = "",
    val designation: String = "",
    val membershipType: MembershipType = MembershipType.GENERAL,
    val joinDate: String = "2023-01-01",
    val presentAddress: String = "",
    val permanentAddress: String = "",
    val emergencyContact: String = "",
    val monthlyContribution: Double = 1000.0,
    val contributionEffectiveDate: String = "2025-01-01",
    val previousContribution: Double? = null,
    val status: String = "Active", // "Active" / "Inactive"
    val password: String = "123456",
    val mustChangePassword: Boolean = false,
    val notes: String = "",
    val lastLogin: String = "2025-02-20 10:30 AM"
)

data class MonthlyRecord(
    val monthKey: String, // "2025-01"
    val monthNameEn: String, // "January 2025"
    val monthNameBn: String, // "জানুয়ারি ২০২৫"
    val requiredAmount: Double,
    val paidAmount: Double,
    val donationAmount: Double = 0.0,
    val status: PaymentStatus,
    val lastPaymentDate: String? = null
)

data class PaymentRecord(
    val receiptNo: String,
    val memberId: String,
    val memberNameEn: String,
    val memberNameBn: String,
    val paymentDate: String,
    val totalReceived: Double,
    val contributionAmount: Double,
    val donationAmount: Double,
    val coveredMonths: List<String>,
    val paymentMethod: String, // "bKash", "Nagad", "Bank Transfer", "Cash"
    val reference: String = "",
    val recordedBy: String,
    val approvedBy: String,
    val status: String = "Approved", // "Approved", "Pending", "Cancelled"
    val notes: String = ""
)

data class DonationRecord(
    val id: String,
    val donorType: DonorType,
    val donorNameEn: String,
    val donorNameBn: String,
    val memberId: String? = null,
    val amount: Double,
    val date: String,
    val paymentMethod: String,
    val purpose: String,
    val isPublic: Boolean = true,
    val showDonorName: Boolean = true,
    val notes: String = ""
)

data class IncomeRecord(
    val id: String,
    val category: String,
    val categoryBn: String,
    val amount: Double,
    val date: String,
    val source: String,
    val relatedMemberId: String? = null,
    val addedBy: String,
    val approvedBy: String,
    val notes: String = ""
)

data class ExpenseRecord(
    val id: String,
    val titleEn: String,
    val titleBn: String,
    val category: String,
    val categoryBn: String,
    val amount: Double,
    val date: String,
    val vendor: String,
    val paymentMethod: String,
    val description: String,
    val addedBy: String,
    val approvedBy: String,
    val isPublic: Boolean = true
)

data class NotificationItem(
    val id: String,
    val memberId: String? = null, // null = broadcast to all
    val titleEn: String,
    val titleBn: String,
    val messageEn: String,
    val messageBn: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val relatedReceiptNo: String? = null
)

data class MessageItem(
    val id: String,
    val senderId: String,
    val senderName: String,
    val senderRole: UserRole,
    val receiverId: String,
    val messageText: String,
    val timestamp: String,
    val isRead: Boolean = false
)

data class PrivacySettings(
    val showMemberName: Boolean = true,
    val showAmount: Boolean = true,
    val showAnonymous: Boolean = false,
    val showOnlyTotalDonation: Boolean = false,
    val showFullContributionHistory: Boolean = true
)

data class ManagerPermissions(
    val canAddExpense: Boolean = true,
    val canSendNotification: Boolean = true,
    val canViewAllReports: Boolean = true,
    val canApprovePayments: Boolean = true
)
