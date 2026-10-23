package com.eliyas.fundmanagementapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "donations")
data class DonationEntity(
    @PrimaryKey val id: String,
    val donorType: String, // "MEMBER" or "GUEST"
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
