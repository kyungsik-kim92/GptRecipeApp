package com.example.gptrecipeapp

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiService {
    @POST("v1/chat/completions")
    suspend fun getGptResponse(
        @Body body: GptRequestParam
    ): GPTResponse

    companion object {
        fun create(): ApiService {

            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(NetworkConstant.SERVER_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        private fun provideOkHttpClient(
            context: Context,
            httpLoggingInterceptor: HttpLoggingInterceptor
        ): OkHttpClient {
            with(OkHttpClient().newBuilder()) {
                cache(Cache(context.cacheDir, (5 * 1024 * 1024).toLong()))
                connectTimeout(60, TimeUnit.SECONDS)
                readTimeout(60, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
                addInterceptor {
                    it.proceed(
                        it.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer ${NetworkConstant.GPT_TOKEN}")
                            .method(it.request().method, it.request().body)
                            .build()
                    )
                }
                addInterceptor(httpLoggingInterceptor)
                return build()
            }
        }
    }
}