package com.eliyas.fundmanagementapp.data.repository

import com.eliyas.fundmanagementapp.data.local.dao.MemberDao
import com.eliyas.fundmanagementapp.domain.model.Contribution
import com.eliyas.fundmanagementapp.domain.model.ContributionStatus
import com.eliyas.fundmanagementapp.domain.repository.ContributionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ContributionRepositoryImpl(
    private val memberDao: MemberDao
) : ContributionRepository {

    override fun getContributionsByMember(memberId: String, year: Int): Flow<List<Contribution>> {
        // Mock 12-month contribution statement generator (backed by local DB or remote sync)
        val sampleContributions = (1..12).map { month ->
            Contribution(
                id = "contrib-${memberId}-$year-$month",
                memberId = memberId,
                month = month,
                year = year,
                requiredAmount = 500.0,
                paidAmount = if (month <= 3) 500.0 else 0.0,
                status = if (month <= 3) ContributionStatus.PAID else ContributionStatus.UNPAID
            )
        }
        return flowOf(sampleContributions)
    }

    override suspend fun updateContribution(contribution: Contribution) {
        // Sync updated contribution state
    }
}
