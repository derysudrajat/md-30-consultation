package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.repo.UserRepository
import com.example.githubuser.repo.local.LocalDataSource
import com.example.githubuser.repo.local.room.FavoriteDao
import com.example.githubuser.repo.local.room.GithubDatabase
import com.example.githubuser.repo.network.RemoteDataSource
import com.example.githubuser.repo.network.service.GithubService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object RepoModules {

    @Provides
    fun provideClient(): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideGithubService(retrofit: Retrofit): GithubService {
        return retrofit.create(GithubService::class.java)
    }

    @Provides
    fun provideRemoteDataSource(service: GithubService): RemoteDataSource =
        RemoteDataSource(service)

    @Provides
    fun provideGithubDatabase(@ApplicationContext context: Context): GithubDatabase =
        GithubDatabase.getInstance(context)

    @Provides
    fun provideFavoriteDao(githubDatabase: GithubDatabase): FavoriteDao =
        githubDatabase.favoriteDao()

    @Provides
    fun provideLocalDataSource(favoriteDao: FavoriteDao): LocalDataSource =
        LocalDataSource(favoriteDao)

    @Provides
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): UserRepository = UserRepository(remoteDataSource, localDataSource)
}