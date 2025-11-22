package com.example.randomuser.repository

import com.example.randomuser.api.ApiService
import com.example.randomuser.data.UserResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getRandomUser(gender: String, nat: String): Response<UserResponse> {
        return apiService.getRandomUser(gender, nat)
    }
}