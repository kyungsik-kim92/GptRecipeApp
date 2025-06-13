package com.example.data.database.network

import com.example.data.remote.dto.GPT
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.data.remote.dto.GptRequestParam as GptRequestParam1

interface ApiService {
    @POST("v1/chat/completions")
    suspend fun getGptResponse(
        @Body body: GptRequestParam1
    ): GPT
}