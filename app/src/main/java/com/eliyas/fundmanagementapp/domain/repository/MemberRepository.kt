package com.eliyas.fundmanagementapp.domain.repository

import com.eliyas.fundmanagementapp.domain.model.Member
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    fun getAllMembers(): Flow<List<Member>>
    fun getMemberById(id: String): Flow<Member?>
    suspend fun insertMember(member: Member)
    suspend fun updateMember(member: Member)
    suspend fun deleteMember(id: String)
}
