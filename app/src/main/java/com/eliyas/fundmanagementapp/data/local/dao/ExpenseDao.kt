package com.eliyas.fundmanagementapp.data.local.dao

import androidx.room.*
import com.eliyas.fundmanagementapp.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(expense: ExpenseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpenses(expenses: List<ExpenseEntity>): List<Long>

    @Query("DELETE FROM expenses")
    fun deleteAllExpenses(): Int
}
