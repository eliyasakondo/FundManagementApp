package com.eliyas.fundmanagementapp.domain.model

enum class Role {
    ADMIN, MANAGER, MEMBER
}

enum class AccountStatus {
    ACTIVE, INACTIVE, LOCKED
}

enum class ContributionStatus {
    UNPAID, PARTIAL, PAID, ADVANCE, OVERDUE, WAIVED, ADJUSTED
}

enum class PaymentMethod {
    BKASH, NAGAD, BANK_TRANSFER, CASH, OTHER
}

enum class ApprovalStatus {
    PENDING, APPROVED, REJECTED
}

enum class DonationType {
    SURPLUS, GENERAL, CAMPAIGN, EXTERNAL
}

data class Member(
    val id: String,
    val memberId: String,
    val fullNameBn: String,
    val fullNameEn: String,
    val mobileNumber: String,
    val altMobileNumber: String? = null,
    val email: String? = null,
    val designation: String? = null,
    val membershipType: String = "General",
    val monthlyContribution: Double = 500.0,
    val accountStatus: AccountStatus = AccountStatus.ACTIVE,
    val role: Role = Role.MEMBER,
    val profilePhotoUrl: String? = null
)

data class Contribution(
    val id: String,
    val memberId: String,
    val month: Int,
    val year: Int,
    val requiredAmount: Double,
    val paidAmount: Double = 0.0,
    val status: ContributionStatus = ContributionStatus.UNPAID
) {
    val remainingAmount: Double
        get() = (requiredAmount - paidAmount).coerceAtLeast(0.0)
}

data class Payment(
    val id: String,
    val receiptNumber: String,
    val memberId: String,
    val paymentDate: String,
    val totalAmountReceived: Double,
    val contributionPortion: Double,
    val donationPortion: Double = 0.0,
    val paymentMethod: PaymentMethod,
    val transactionReference: String? = null,
    val recordedBy: String,
    val approvalStatus: ApprovalStatus = ApprovalStatus.APPROVED,
    val notes: String? = null
)

data class ExtraDonation(
    val id: String,
    val paymentId: String? = null,
    val memberId: String? = null,
    val donorName: String? = null,
    val donationType: DonationType = DonationType.SURPLUS,
    val amount: Double,
    val purpose: String? = null,
    val donationDate: String
)

data class OrganizationExpense(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val expenseDate: String,
    val vendorReceiver: String? = null,
    val description: String? = null,
    val attachmentUrl: String? = null
)

data class Notice(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val contentBn: String,
    val contentEn: String,
    val isPinned: Boolean = false,
    val createdAt: String
)

data class FundAccount(
    val id: String,
    val accountName: String,
    val accountType: String,
    val accountNumber: String? = null,
    val currentBalance: Double = 0.0
)
