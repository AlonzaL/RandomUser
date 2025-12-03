package com.example.randomuser.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuser.data.model.User
import com.example.randomuser.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NavigationEvent {
    object NavigateToUserList : NavigationEvent()
}

sealed interface UserListUiState {
    data object Loading : UserListUiState
    data class Success(val users: List<User>) : UserListUiState
    data class Error(val message: String) : UserListUiState
    data object Idle : UserListUiState
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Idle)
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.getUsersFromDb().collectLatest { users ->
                if (users.isNotEmpty()) {
                    _uiState.value = UserListUiState.Success(users)
                } else {
                    _uiState.value = UserListUiState.Idle
                }
            }
        }
    }

    fun loadUser(gender: String, nat: String) {
        viewModelScope.launch {
            _uiState.value = UserListUiState.Loading
            try {
                repository.fetchAndSaveUser(gender, nat)
                _navigationEvent.send(NavigationEvent.NavigateToUserList)
            } catch (e: Exception) {
                _uiState.value = UserListUiState.Error("Ошибка сети: ${e.message}")
            }
        }
    }

    fun selectUser(userUuid: String) {
        val currentState = _uiState.value
        if (currentState is UserListUiState.Success) {
            val user = currentState.users.find { it.login?.uuid == userUuid }
            _selectedUser.value = user
        }
    }

    fun clearSelectedUser() {
        _selectedUser.value = null
    }
}