package com.eliyas.fundmanagementapp.domain.usecase

import com.eliyas.fundmanagementapp.domain.model.Contribution
import com.eliyas.fundmanagementapp.domain.model.ContributionStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PartialPaymentUseCaseTest {

    private lateinit var partialPaymentUseCase: PartialPaymentUseCase

    @Before
    fun setUp() {
        partialPaymentUseCase = PartialPaymentUseCase()
    }

    @Test
    fun `accumulates partial payment correctly and sets status to PARTIAL`() {
        val initialContribution = Contribution(
            id = "c-1",
            memberId = "m-101",
            month = 1,
            year = 2026,
            requiredAmount = 500.0,
            paidAmount = 100.0,
            status = ContributionStatus.PARTIAL
        )

        val result = partialPaymentUseCase(initialContribution, partialAmount = 200.0)

        assertEquals(300.0, result.updatedContribution.paidAmount, 0.001)
        assertEquals(200.0, result.remainingDue, 0.001)
        assertEquals(ContributionStatus.PARTIAL, result.updatedContribution.status)
        assertFalse(result.isFullyPaid)
    }

    @Test
    fun `accumulates partial payment to reach full requirement and sets status to PAID`() {
        val initialContribution = Contribution(
            id = "c-1",
            memberId = "m-101",
            month = 1,
            year = 2026,
            requiredAmount = 500.0,
            paidAmount = 300.0,
            status = ContributionStatus.PARTIAL
        )

        val result = partialPaymentUseCase(initialContribution, partialAmount = 200.0)

        assertEquals(500.0, result.updatedContribution.paidAmount, 0.001)
        assertEquals(0.0, result.remainingDue, 0.001)
        assertEquals(ContributionStatus.PAID, result.updatedContribution.status)
        assertTrue(result.isFullyPaid)
    }
}
