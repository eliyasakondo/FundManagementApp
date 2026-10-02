package com.eliyas.fundmanagementapp.domain.usecase

import com.eliyas.fundmanagementapp.domain.model.Contribution
import com.eliyas.fundmanagementapp.domain.model.ContributionStatus
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MultiMonthPaymentUseCaseTest {

    private lateinit var multiMonthPaymentUseCase: MultiMonthPaymentUseCase

    @Before
    fun setUp() {
        multiMonthPaymentUseCase = MultiMonthPaymentUseCase()
    }

    @Test
    fun `distributes lump sum evenly across three consecutive unpaid months`() {
        val month1 = Contribution("c-1", "m-101", month = 1, year = 2026, requiredAmount = 500.0, paidAmount = 0.0, status = ContributionStatus.UNPAID)
        val month2 = Contribution("c-2", "m-101", month = 2, year = 2026, requiredAmount = 500.0, paidAmount = 0.0, status = ContributionStatus.UNPAID)
        val month3 = Contribution("c-3", "m-101", month = 3, year = 2026, requiredAmount = 500.0, paidAmount = 0.0, status = ContributionStatus.UNPAID)

        val result = multiMonthPaymentUseCase(listOf(month1, month2, month3), lumpSumAmount = 1500.0)

        assertEquals(0.0, result.surplusDonation, 0.001)
        assertEquals(3, result.allocations.size)
        assertEquals(ContributionStatus.PAID, result.updatedContributions[0].status)
        assertEquals(ContributionStatus.PAID, result.updatedContributions[1].status)
        assertEquals(ContributionStatus.PAID, result.updatedContributions[2].status)
    }

    @Test
    fun `distributes lump sum with partial allocation on last month and surplus extra donation`() {
        val month1 = Contribution("c-1", "m-101", month = 1, year = 2026, requiredAmount = 500.0, paidAmount = 0.0, status = ContributionStatus.UNPAID)
        val month2 = Contribution("c-2", "m-101", month = 2, year = 2026, requiredAmount = 500.0, paidAmount = 0.0, status = ContributionStatus.UNPAID)

        val result = multiMonthPaymentUseCase(listOf(month1, month2), lumpSumAmount = 1200.0)

        assertEquals(200.0, result.surplusDonation, 0.001)
        assertEquals(ContributionStatus.PAID, result.updatedContributions[0].status)
        assertEquals(ContributionStatus.PAID, result.updatedContributions[1].status)
    }
}
