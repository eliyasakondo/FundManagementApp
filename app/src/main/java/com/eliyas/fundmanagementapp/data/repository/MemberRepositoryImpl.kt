package com.eliyas.fundmanagementapp.data.repository

import com.eliyas.fundmanagementapp.data.local.dao.MemberDao
import com.eliyas.fundmanagementapp.data.local.entity.MemberEntity
import com.eliyas.fundmanagementapp.domain.model.AccountStatus
import com.eliyas.fundmanagementapp.domain.model.Member
import com.eliyas.fundmanagementapp.domain.model.Role
import com.eliyas.fundmanagementapp.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MemberRepositoryImpl(
    private val memberDao: MemberDao
) : MemberRepository {

    override fun getAllMembers(): Flow<List<Member>> {
        return memberDao.getAllMembers().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getMemberById(id: String): Flow<Member?> {
        return memberDao.getMemberById(id).map { it?.toDomain() }
    }

    override suspend fun insertMember(member: Member) {
        memberDao.insertMember(member.toEntity())
    }

    override suspend fun updateMember(member: Member) {
        memberDao.updateMember(member.toEntity())
    }

    override suspend fun deleteMember(id: String) {
        memberDao.deleteMember(id)
    }

    private fun MemberEntity.toDomain() = Member(
        id = id,
        memberId = memberId,
        fullNameBn = fullNameBn,
        fullNameEn = fullNameEn,
        mobileNumber = mobileNumber,
        altMobileNumber = altMobileNumber,
        email = email,
        designation = designation,
        membershipType = membershipType,
        monthlyContribution = monthlyContribution,
        accountStatus = try { AccountStatus.valueOf(accountStatus) } catch (e: Exception) { AccountStatus.ACTIVE },
        role = try { Role.valueOf(role) } catch (e: Exception) { Role.MEMBER },
        profilePhotoUrl = profilePhotoUrl
    )

    private fun Member.toEntity() = MemberEntity(
        id = id,
        memberId = memberId,
        fullNameBn = fullNameBn,
        fullNameEn = fullNameEn,
        mobileNumber = mobileNumber,
        altMobileNumber = altMobileNumber,
        email = email,
        designation = designation,
        membershipType = membershipType,
        monthlyContribution = monthlyContribution,
        accountStatus = accountStatus.name,
        role = role.name,
        profilePhotoUrl = profilePhotoUrl
    )
}
