package com.example.randomuser.repository

import com.example.randomuser.api.ApiService
import com.example.randomuser.data.User
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getRandomUser(gender: String, nat: String): User {
        val response = apiService.getRandomUser(gender, nat)
        return response.results?.firstOrNull()
            ?: throw NoSuchElementException("Сервер вернул пустой список пользователей")
    }
}