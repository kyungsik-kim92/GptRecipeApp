package com.example.gptrecipeapp

interface DataSource {
    suspend fun getGptResponse(
        body: GptRequestParam
    ): GPTResponse
}