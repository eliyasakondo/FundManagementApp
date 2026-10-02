package com.eliyas.fundmanagementapp.domain.usecase

import com.eliyas.fundmanagementapp.domain.model.Contribution
import com.eliyas.fundmanagementapp.domain.model.ContributionStatus

data class PartialPaymentResult(
    val updatedContribution: Contribution,
    val remainingDue: Double,
    val isFullyPaid: Boolean
)

class PartialPaymentUseCase {

    /**
     * Accumulates partial payment amounts into an existing contribution record.
     * Updates status to PARTIAL if paid amount < required amount, or PAID if paid amount >= required amount.
     */
    operator fun invoke(
        contribution: Contribution,
        partialAmount: Double
    ): PartialPaymentResult {
        require(partialAmount > 0.0) { "Partial payment amount must be positive" }

        val newPaidTotal = contribution.paidAmount + partialAmount
        val required = contribution.requiredAmount

        val (finalPaid, newStatus) = if (newPaidTotal >= required) {
            required to ContributionStatus.PAID
        } else {
            newPaidTotal to ContributionStatus.PARTIAL
        }

        val updatedContribution = contribution.copy(
            paidAmount = finalPaid,
            status = newStatus
        )

        val remainingDue = (required - finalPaid).coerceAtLeast(0.0)

        return PartialPaymentResult(
            updatedContribution = updatedContribution,
            remainingDue = remainingDue,
            isFullyPaid = newStatus == ContributionStatus.PAID
        )
    }
}
