package com.eliyas.fundmanagementapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eliyas.fundmanagementapp.domain.model.Contribution
import com.eliyas.fundmanagementapp.domain.model.ContributionStatus
import com.eliyas.fundmanagementapp.domain.usecase.PaymentCalculationResult
import com.eliyas.fundmanagementapp.domain.usecase.ProcessPaymentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AdminPaymentUiState {
    object Idle : AdminPaymentUiState()
    object Processing : AdminPaymentUiState()
    data class Success(val result: PaymentCalculationResult) : AdminPaymentUiState()
    data class Error(val message: String) : AdminPaymentUiState()
}

class AdminViewModel(
    private val processPaymentUseCase: ProcessPaymentUseCase = ProcessPaymentUseCase()
) : ViewModel() {

    private val _paymentState = MutableStateFlow<AdminPaymentUiState>(AdminPaymentUiState.Idle)
    val paymentState: StateFlow<AdminPaymentUiState> = _paymentState.asStateFlow()

    fun recordPayment(
        currentContribution: Contribution,
        receivedAmount: Double
    ) {
        viewModelScope.launch {
            _paymentState.value = AdminPaymentUiState.Processing

            try {
                val calculation = processPaymentUseCase(
                    currentContribution = currentContribution,
                    receivedAmount = receivedAmount
                )
                _paymentState.value = AdminPaymentUiState.Success(calculation)
            } catch (e: Exception) {
                _paymentState.value = AdminPaymentUiState.Error(e.message ?: "Failed to record payment")
            }
        }
    }

    fun resetState() {
        _paymentState.value = AdminPaymentUiState.Idle
    }
}
