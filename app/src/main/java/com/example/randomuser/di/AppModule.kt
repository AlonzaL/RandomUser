package com.example.randomuser.di

import android.content.Context
import com.example.randomuser.data.api.ApiService
import com.example.randomuser.data.api.RetrofitClient
import com.example.randomuser.data.db.MainDb
import com.example.randomuser.data.db.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return RetrofitClient.userApiService
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ): MainDb {
        return MainDb.createDb(context)
    }

    @Provides
    @Singleton
    fun provideUserDao(db: MainDb): UserDao {
        return db.userDao()
    }
}