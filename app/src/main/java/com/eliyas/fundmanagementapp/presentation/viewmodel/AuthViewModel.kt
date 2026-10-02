package com.eliyas.fundmanagementapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eliyas.fundmanagementapp.domain.model.Member
import com.eliyas.fundmanagementapp.domain.model.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val member: Member) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(identifier: String, password: String, isBangla: Boolean) {
        viewModelScope.launch {
            if (identifier.isBlank() || password.isBlank()) {
                val errorMsg = if (isBangla) "মোবাইল নম্বর এবং পাসওয়ার্ড দিন" else "Please enter mobile number and password"
                _uiState.value = AuthUiState.Error(errorMsg)
                return@launch
            }

            _uiState.value = AuthUiState.Loading

            // Mock authentication validation (Will connect to Supabase Auth SDK)
            if (password == "wrong") {
                val genericError = if (isBangla) "মোবাইল নম্বর অথবা পাসওয়ার্ড সঠিক নয়" else "Invalid mobile number or password"
                _uiState.value = AuthUiState.Error(genericError)
            } else {
                val mockRole = when {
                    identifier.contains("admin") -> Role.ADMIN
                    identifier.contains("manager") -> Role.MANAGER
                    else -> Role.MEMBER
                }
                val mockMember = Member(
                    id = "auth-101",
                    memberId = "M-101",
                    fullNameBn = "মোঃ রফিকুল ইসলাম",
                    fullNameEn = "Md. Rafiqul Islam",
                    mobileNumber = identifier,
                    role = mockRole
                )
                _uiState.value = AuthUiState.Success(mockMember)
            }
        }
    }

    fun logout() {
        _uiState.value = AuthUiState.Idle
    }
}
