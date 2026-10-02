package com.eliyas.fundmanagementapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eliyas.fundmanagementapp.domain.model.Contribution
import com.eliyas.fundmanagementapp.domain.model.ContributionStatus
import com.eliyas.fundmanagementapp.domain.usecase.GetMemberStatementUseCase
import com.eliyas.fundmanagementapp.domain.usecase.MemberStatementSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MemberDashboardUiState {
    object Loading : MemberDashboardUiState()
    data class Success(val summary: MemberStatementSummary) : MemberDashboardUiState()
    data class Error(val message: String) : MemberDashboardUiState()
}

class MemberViewModel(
    private val getMemberStatementUseCase: GetMemberStatementUseCase = GetMemberStatementUseCase()
) : ViewModel() {

    private val _dashboardState = MutableStateFlow<MemberDashboardUiState>(MemberDashboardUiState.Loading)
    val dashboardState: StateFlow<MemberDashboardUiState> = _dashboardState.asStateFlow()

    fun loadMemberStatement(memberId: String, year: Int = 2026) {
        viewModelScope.launch {
            _dashboardState.value = MemberDashboardUiState.Loading

            // Mock 12-month sample contributions for current year
            val sampleContributions = (1..12).map { month ->
                Contribution(
                    id = "c-$month",
                    memberId = memberId,
                    month = month,
                    year = year,
                    requiredAmount = 500.0,
                    paidAmount = if (month <= 4) 500.0 else if (month == 5) 200.0 else 0.0,
                    status = when {
                        month <= 4 -> ContributionStatus.PAID
                        month == 5 -> ContributionStatus.PARTIAL
                        else -> ContributionStatus.UNPAID
                    }
                )
            }

            val summary = getMemberStatementUseCase(
                memberId = memberId,
                year = year,
                contributions = sampleContributions
            )

            _dashboardState.value = MemberDashboardUiState.Success(summary)
        }
    }
}
