package com.eliyas.fundmanagementapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eliyas.fundmanagementapp.data.local.dao.*
import com.eliyas.fundmanagementapp.data.local.entity.*

@Database(
    entities = [
        MemberEntity::class,
        ContributionEntity::class,
        PaymentEntity::class,
        ExpenseEntity::class,
        DonationEntity::class,
        NotificationEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun paymentDao(): PaymentDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun notificationDao(): NotificationDao
}
