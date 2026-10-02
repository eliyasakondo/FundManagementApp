package com.eliyas.fundmanagementapp.domain.usecase

import com.eliyas.fundmanagementapp.domain.model.Contribution
import com.eliyas.fundmanagementapp.domain.model.ContributionStatus
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProcessPaymentUseCaseTest {

    private lateinit var processPaymentUseCase: ProcessPaymentUseCase

    @Before
    fun setUp() {
        processPaymentUseCase = ProcessPaymentUseCase()
    }

    @Test
    fun `when received amount equals required amount then contribution is fully paid and surplus is zero`() {
        val contribution = Contribution(
            id = "c-1",
            memberId = "m-101",
            month = 1,
            year = 2026,
            requiredAmount = 500.0,
            paidAmount = 0.0,
            status = ContributionStatus.UNPAID
        )

        val result = processPaymentUseCase(contribution, receivedAmount = 500.0)

        assertEquals(500.0, result.contributionPortion, 0.001)
        assertEquals(0.0, result.surplusDonationPortion, 0.001)
        assertEquals(500.0, result.newPaidAmount, 0.001)
        assertEquals(ContributionStatus.PAID, result.newStatus)
    }

    @Test
    fun `when received amount exceeds remaining due then surplus is routed to extra donation`() {
        val contribution = Contribution(
            id = "c-1",
            memberId = "m-101",
            month = 1,
            year = 2026,
            requiredAmount = 500.0,
            paidAmount = 0.0,
            status = ContributionStatus.UNPAID
        )

        val result = processPaymentUseCase(contribution, receivedAmount = 800.0)

        assertEquals(500.0, result.contributionPortion, 0.001)
        assertEquals(300.0, result.surplusDonationPortion, 0.001)
        assertEquals(500.0, result.newPaidAmount, 0.001)
        assertEquals(ContributionStatus.PAID, result.newStatus)
    }

    @Test
    fun `when received amount is less than remaining due then status becomes PARTIAL`() {
        val contribution = Contribution(
            id = "c-1",
            memberId = "m-101",
            month = 1,
            year = 2026,
            requiredAmount = 500.0,
            paidAmount = 0.0,
            status = ContributionStatus.UNPAID
        )

        val result = processPaymentUseCase(contribution, receivedAmount = 300.0)

        assertEquals(300.0, result.contributionPortion, 0.001)
        assertEquals(0.0, result.surplusDonationPortion, 0.001)
        assertEquals(300.0, result.newPaidAmount, 0.001)
        assertEquals(ContributionStatus.PARTIAL, result.newStatus)
    }
}
