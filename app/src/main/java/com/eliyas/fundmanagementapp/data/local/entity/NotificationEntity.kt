package com.eliyas.fundmanagementapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val memberId: String? = null,
    val titleEn: String,
    val titleBn: String,
    val messageEn: String,
    val messageBn: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val relatedReceiptNo: String? = null
)
