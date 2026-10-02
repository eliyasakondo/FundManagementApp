package com.eliyas.fundmanagementapp.domain.repository

import com.eliyas.fundmanagementapp.domain.model.Contribution
import kotlinx.coroutines.flow.Flow

interface ContributionRepository {
    fun getContributionsByMember(memberId: String, year: Int): Flow<List<Contribution>>
    suspend fun updateContribution(contribution: Contribution)
}
