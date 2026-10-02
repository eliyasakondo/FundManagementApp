package com.eliyas.fundmanagementapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey val id: String,
    val memberId: String,
    val fullNameBn: String,
    val fullNameEn: String,
    val mobileNumber: String,
    val altMobileNumber: String? = null,
    val email: String? = null,
    val designation: String? = null,
    val membershipType: String = "General",
    val monthlyContribution: Double = 500.0,
    val accountStatus: String = "ACTIVE",
    val role: String = "MEMBER",
    val profilePhotoUrl: String? = null
)

@Entity(tableName = "contributions")
data class ContributionEntity(
    @PrimaryKey val id: String,
    val memberId: String,
    val month: Int,
    val year: Int,
    val requiredAmount: Double,
    val paidAmount: Double = 0.0,
    val status: String = "UNPAID"
)

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey val id: String,
    val receiptNumber: String,
    val memberId: String,
    val paymentDate: String,
    val totalAmountReceived: Double,
    val contributionPortion: Double,
    val donationPortion: Double = 0.0,
    val paymentMethod: String,
    val transactionReference: String? = null,
    val recordedBy: String,
    val approvalStatus: String = "APPROVED",
    val notes: String? = null
)
