package com.example.randomuser.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuser.data.User
import com.example.randomuser.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

sealed class NavigationEvent {
    object NavigateToUserList : NavigationEvent()
}

sealed interface UserListUiState {
    data object Loading : UserListUiState // Состояние загрузки
    data class Success(val users: List<User>) : UserListUiState // Успех, содержит данные
    data class Error(val message: String) : UserListUiState // Ошибка, содержит сообщение
    data object Idle : UserListUiState // Начальное/нейтральное состояние
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _usersList = MutableStateFlow<List<User>>(emptyList())

    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Idle)
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    // Навигация остается без изменений
    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()


    /**
     * Этот метод вызывается из UI (ConfigScreen), когда пользователь нажимает кнопку "Сгенерировать".
     */

    fun loadUser(gender: String, nat: String) {
        Log.d("ViewModelDebug", "1. Метод generateAndNavigate вызван") // <-- ЛОГ №1
        viewModelScope.launch {
            _uiState.value = UserListUiState.Loading
            try {
                val response = repository.getRandomUser(gender, nat)
                // --- ЛОГИРОВАНИЕ ДАННЫХ ---
                Log.d("ApiDebug", "2. УСПЕХ! Получены данные: $response")

                _usersList.value = listOf(response) + _usersList.value

                // 4. Теперь обновляем UI, передавая ему полный, накопленный список из "копилки"
                _uiState.value = UserListUiState.Success(_usersList.value)

                _navigationEvent.send(NavigationEvent.NavigateToUserList)
            } catch (e: kotlin.Exception) {
                _uiState.value = UserListUiState.Error("Ошибка загрузки пользователя ${e.message}")
            }
        }
    }
}