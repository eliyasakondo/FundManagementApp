package com.eliyas.fundmanagementapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val id: String,
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
