package com.example.randomuser.api

import com.example.randomuser.data.ApiResponse
import com.example.randomuser.data.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/")
    suspend fun getRandomUser(
        @Query("gender") gender: String,
        @Query("nat") nationality: String
    ): ApiResponse
}