package com.example.gptrecipeapp

interface Repository {
    suspend fun getGptResponse(
        body: GptRequestParam
    ): GPTResponse
}