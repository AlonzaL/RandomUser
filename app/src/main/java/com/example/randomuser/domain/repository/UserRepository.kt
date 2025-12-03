package com.example.randomuser.domain.repository

import com.example.randomuser.data.api.ApiService
import com.example.randomuser.data.api.RetrofitClient
import com.example.randomuser.data.db.UserDao
import com.example.randomuser.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun fetchAndSaveUser(gender: String, nat: String) {
        val response = RetrofitClient.userApiService.getRandomUser(gender, nat)
        val user = response.results?.firstOrNull()
            ?: throw NoSuchElementException("Сервер вернул пустой список")
        userDao.insertUser(user)
    }

    fun getUsersFromDb(): Flow<List<User>> {
        return userDao.getAllUsers()
    }
}