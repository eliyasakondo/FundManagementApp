package com.eliyas.fundmanagementapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberDto(
    @SerialName("id") val id: String,
    @SerialName("member_id") val memberId: String,
    @SerialName("full_name_bn") val fullNameBn: String,
    @SerialName("full_name_en") val fullNameEn: String,
    @SerialName("mobile_number") val mobileNumber: String,
    @SerialName("alt_mobile_number") val altMobileNumber: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("designation") val designation: String? = null,
    @SerialName("membership_type") val membershipType: String = "General",
    @SerialName("monthly_contribution") val monthlyContribution: Double = 500.0,
    @SerialName("account_status") val accountStatus: String = "ACTIVE",
    @SerialName("role") val role: String = "MEMBER",
    @SerialName("profile_photo_url") val profilePhotoUrl: String? = null
)

@Serializable
data class ContributionDto(
    @SerialName("id") val id: String,
    @SerialName("member_id") val memberId: String,
    @SerialName("month") val month: Int,
    @SerialName("year") val year: Int,
    @SerialName("required_amount") val requiredAmount: Double,
    @SerialName("paid_amount") val paidAmount: Double = 0.0,
    @SerialName("status") val status: String = "UNPAID"
)

@Serializable
data class PaymentDto(
    @SerialName("id") val id: String,
    @SerialName("receipt_number") val receiptNumber: String,
    @SerialName("member_id") val memberId: String,
    @SerialName("payment_date") val paymentDate: String,
    @SerialName("total_amount_received") val totalAmountReceived: Double,
    @SerialName("contribution_portion") val contributionPortion: Double,
    @SerialName("donation_portion") val donationPortion: Double = 0.0,
    @SerialName("payment_method") val paymentMethod: String,
    @SerialName("transaction_reference") val transactionReference: String? = null,
    @SerialName("recorded_by") val recordedBy: String,
    @SerialName("approval_status") val approvalStatus: String = "APPROVED",
    @SerialName("notes") val notes: String? = null
)
