package com.example.randomuser.data.api

import com.example.randomuser.data.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/")
    suspend fun getRandomUser(
        @Query("gender") gender: String,
        @Query("nat") nationality: String
    ): ApiResponse
}