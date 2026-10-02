package com.eliyas.fundmanagementapp.domain.usecase

import com.eliyas.fundmanagementapp.domain.model.Contribution
import com.eliyas.fundmanagementapp.domain.model.ContributionStatus

data class PaymentCalculationResult(
    val contributionPortion: Double,
    val surplusDonationPortion: Double,
    val newPaidAmount: Double,
    val newStatus: ContributionStatus
)

class ProcessPaymentUseCase {

    /**
     * Calculates the exact split between monthly contribution requirement and surplus donation.
     */
    operator fun invoke(
        currentContribution: Contribution,
        receivedAmount: Double
    ): PaymentCalculationResult {
        require(receivedAmount >= 0.0) { "Received payment amount cannot be negative" }

        val remainingRequired = (currentContribution.requiredAmount - currentContribution.paidAmount).coerceAtLeast(0.0)

        return if (receivedAmount <= remainingRequired) {
            // Payment is less than or equal to remaining required contribution amount
            val newPaid = currentContribution.paidAmount + receivedAmount
            val newStatus = if (newPaid >= currentContribution.requiredAmount) {
                ContributionStatus.PAID
            } else {
                ContributionStatus.PARTIAL
            }
            PaymentCalculationResult(
                contributionPortion = receivedAmount,
                surplusDonationPortion = 0.0,
                newPaidAmount = newPaid,
                newStatus = newStatus
            )
        } else {
            // Payment exceeds remaining contribution -> surplus goes to Extra Donation fund
            val contributionPortion = remainingRequired
            val surplusPortion = receivedAmount - remainingRequired
            PaymentCalculationResult(
                contributionPortion = contributionPortion,
                surplusDonationPortion = surplusPortion,
                newPaidAmount = currentContribution.requiredAmount,
                newStatus = ContributionStatus.PAID
            )
        }
    }
}
