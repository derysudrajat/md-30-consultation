package id.derysudrajat.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.derysudrajat.storyapp.repo.StoryRepository
import id.derysudrajat.storyapp.repo.local.LocalStore
import id.derysudrajat.storyapp.repo.remote.RemoteDataSource
import id.derysudrajat.storyapp.repo.remote.network.StoryService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun provideRetrofitClient(): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofitStoryApi(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideStoryService(retrofit: Retrofit): StoryService =
        retrofit.create(StoryService::class.java)

    @Provides
    fun provideRemoteDataSource(service: StoryService): RemoteDataSource = RemoteDataSource(service)

    @Provides
    fun provideStoryRepository(
        remoteDataSource: RemoteDataSource,
    ): StoryRepository = StoryRepository(remoteDataSource)

    val Context.userStore: DataStore<Preferences> by preferencesDataStore(name = "user")

    @Provides
    fun provideLocalStore(@ApplicationContext context: Context) = LocalStore(context)

}