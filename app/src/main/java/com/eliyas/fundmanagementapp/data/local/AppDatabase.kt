package com.eliyas.fundmanagementapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eliyas.fundmanagementapp.data.local.dao.MemberDao
import com.eliyas.fundmanagementapp.data.local.entity.ContributionEntity
import com.eliyas.fundmanagementapp.data.local.entity.MemberEntity
import com.eliyas.fundmanagementapp.data.local.entity.PaymentEntity

@Database(
    entities = [MemberEntity::class, ContributionEntity::class, PaymentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
}
