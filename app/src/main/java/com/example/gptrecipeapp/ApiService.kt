package com.example.gptrecipeapp

import com.example.gptrecipeapp.model.GPT
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("v1/chat/completions")
    suspend fun getGptResponse(
        @Body body: GptRequestParam
    ): GPT
}