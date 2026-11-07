package com.eliyas.fundmanagementapp.data.local.dao

import androidx.room.*
import com.eliyas.fundmanagementapp.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(notification: NotificationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotifications(notifications: List<NotificationEntity>): List<Long>

    @Query("UPDATE notifications SET isRead = 1")
    fun markAllRead(): Int
}
