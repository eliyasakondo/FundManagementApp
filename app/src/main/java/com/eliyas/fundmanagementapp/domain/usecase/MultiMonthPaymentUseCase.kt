package com.eliyas.fundmanagementapp.domain.usecase

import com.eliyas.fundmanagementapp.domain.model.Contribution
import com.eliyas.fundmanagementapp.domain.model.ContributionStatus

data class MultiMonthAllocation(
    val contribution: Contribution,
    val allocatedAmount: Double
)

data class MultiMonthPaymentResult(
    val updatedContributions: List<Contribution>,
    val allocations: List<MultiMonthAllocation>,
    val surplusDonation: Double,
    val remainingUnallocated: Double
)

class MultiMonthPaymentUseCase {

    /**
     * Distributes a lump sum payment across an ordered list of monthly contributions.
     * Fills required contributions sequentially from earliest month to future months.
     * Any remaining amount after covering all listed contributions goes to surplus donation.
     */
    operator fun invoke(
        contributions: List<Contribution>,
        lumpSumAmount: Double
    ): MultiMonthPaymentResult {
        require(lumpSumAmount > 0.0) { "Lump sum amount must be positive" }

        val sortedContributions = contributions.sortedWith(compareBy({ it.year }, { it.month }))
        var remainingPool = lumpSumAmount

        val updatedList = mutableListOf<Contribution>()
        val allocationsList = mutableListOf<MultiMonthAllocation>()

        for (item in sortedContributions) {
            if (remainingPool <= 0.0) {
                updatedList.add(item)
                continue
            }

            val due = (item.requiredAmount - item.paidAmount).coerceAtLeast(0.0)

            if (due == 0.0) {
                // Already fully paid
                updatedList.add(item)
                continue
            }

            if (remainingPool >= due) {
                // Fully cover this month's remaining due
                val allocated = due
                remainingPool -= due
                val updatedItem = item.copy(
                    paidAmount = item.requiredAmount,
                    status = ContributionStatus.PAID
                )
                updatedList.add(updatedItem)
                allocationsList.add(MultiMonthAllocation(updatedItem, allocated))
            } else {
                // Partially cover this month
                val allocated = remainingPool
                val newPaid = item.paidAmount + allocated
                remainingPool = 0.0
                val updatedItem = item.copy(
                    paidAmount = newPaid,
                    status = ContributionStatus.PARTIAL
                )
                updatedList.add(updatedItem)
                allocationsList.add(MultiMonthAllocation(updatedItem, allocated))
            }
        }

        return MultiMonthPaymentResult(
            updatedContributions = updatedList,
            allocations = allocationsList,
            surplusDonation = remainingPool,
            remainingUnallocated = 0.0
        )
    }
}
