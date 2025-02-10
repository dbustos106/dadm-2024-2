package com.example.reto_11.di

import com.example.reto_11.data.OpenAiApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): OpenAiApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(OpenAiApi.BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer ${OpenAiApi.API_KEY}")
                            .addHeader("HTTP-Referer", "https://tu-sitio.com") // Ajusta con tu URL
                            .addHeader("X-Title", "Tu App") // Ajusta con el nombre de tu app
                            .addHeader("Content-Type", "application/json")
                            .build()
                        chain.proceed(newRequest)
                    }
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(OpenAiApi::class.java)
    }

}
