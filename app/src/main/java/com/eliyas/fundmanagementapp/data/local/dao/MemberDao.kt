package com.eliyas.fundmanagementapp.data.local.dao

import androidx.room.*
import com.eliyas.fundmanagementapp.data.local.entity.MemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members ORDER BY memberId ASC")
    fun getAllMembers(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE id = :id LIMIT 1")
    fun getMemberById(id: String): Flow<MemberEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMember(member: MemberEntity): Long

    @Update
    fun updateMember(member: MemberEntity): Int

    @Query("DELETE FROM members WHERE id = :id")
    fun deleteMember(id: String): Int
}
