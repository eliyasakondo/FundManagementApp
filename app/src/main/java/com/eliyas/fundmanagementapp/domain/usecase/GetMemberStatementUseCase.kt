package com.eliyas.fundmanagementapp.domain.usecase

import com.eliyas.fundmanagementapp.domain.model.Contribution
import com.eliyas.fundmanagementapp.domain.model.ContributionStatus

data class MemberStatementSummary(
    val memberId: String,
    val year: Int,
    val totalRequired: Double,
    val totalPaid: Double,
    val totalOverdue: Double,
    val fullyPaidMonthsCount: Int,
    val partialMonthsCount: Int,
    val unpaidMonthsCount: Int,
    val contributions: List<Contribution>
)

class GetMemberStatementUseCase {

    /**
     * Generates a annual statement summary for a specific member.
     */
    operator fun invoke(
        memberId: String,
        year: Int,
        contributions: List<Contribution>
    ): MemberStatementSummary {
        val memberContributions = contributions
            .filter { it.memberId == memberId && it.year == year }
            .sortedBy { it.month }

        val totalRequired = memberContributions.sumOf { it.requiredAmount }
        val totalPaid = memberContributions.sumOf { it.paidAmount }
        val totalOverdue = memberContributions
            .filter { it.status == ContributionStatus.UNPAID || it.status == ContributionStatus.OVERDUE || it.status == ContributionStatus.PARTIAL }
            .sumOf { (it.requiredAmount - it.paidAmount).coerceAtLeast(0.0) }

        val fullyPaidCount = memberContributions.count { it.status == ContributionStatus.PAID || it.status == ContributionStatus.ADVANCE }
        val partialCount = memberContributions.count { it.status == ContributionStatus.PARTIAL }
        val unpaidCount = memberContributions.count { it.status == ContributionStatus.UNPAID || it.status == ContributionStatus.OVERDUE }

        return MemberStatementSummary(
            memberId = memberId,
            year = year,
            totalRequired = totalRequired,
            totalPaid = totalPaid,
            totalOverdue = totalOverdue,
            fullyPaidMonthsCount = fullyPaidCount,
            partialMonthsCount = partialCount,
            unpaidMonthsCount = unpaidCount,
            contributions = memberContributions
        )
    }
}
