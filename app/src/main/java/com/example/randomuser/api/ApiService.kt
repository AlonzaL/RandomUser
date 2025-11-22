package com.example.randomuser.api

import com.example.randomuser.data.User
import com.example.randomuser.data.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/")
    suspend fun getRandomUser(
        @Query("gender") gender: String,
        @Query("nat") nationality: String
    ): Response<UserResponse>
}