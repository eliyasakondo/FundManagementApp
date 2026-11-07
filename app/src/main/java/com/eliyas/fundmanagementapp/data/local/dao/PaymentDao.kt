package com.eliyas.fundmanagementapp.data.local.dao

import androidx.room.*
import com.eliyas.fundmanagementapp.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payments ORDER BY paymentDate DESC")
    fun getAllPayments(): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE memberId = :memberId ORDER BY paymentDate DESC")
    fun getPaymentsByMember(memberId: String): Flow<List<PaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPayment(payment: PaymentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPayments(payments: List<PaymentEntity>): List<Long>

    @Query("DELETE FROM payments")
    fun deleteAllPayments(): Int
}
