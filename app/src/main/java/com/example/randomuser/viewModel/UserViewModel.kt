package com.example.randomuser.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuser.data.User
import com.example.randomuser.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // --- Вот они, наши переменные! ---
    // Они хранят текущие параметры для запроса.
    private var currentGender: String = ""
    private var currentNationalities: String = ""
    // ---------------------------------

    /**
     * Этот метод вызывается из UI (ConfigScreen), когда пользователь нажимает кнопку "Сгенерировать".
     */
    fun startLoadingWithNewSettings(gender: String, nationalities: String) {
        // 1. Сохраняем новые настройки в локальные переменные ViewModel
        currentGender = gender
        currentNationalities = nationalities

        // 3. Запускаем загрузку первой страницы с новыми параметрами
        loadUser()
    }

    fun loadUser() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.getRandomUser(currentGender, currentNationalities)
                if (response.isSuccessful) {
                    _users.value = response.body()?.result?: emptyList()
                } else {
                    _error.value = "Ошибка загрузки пользователя ${response.message()}"
                }
            } catch (e: kotlin.Exception) {
                _error.value = "Ошибка загрузки пользователя ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}