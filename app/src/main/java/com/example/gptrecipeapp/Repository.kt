package com.example.gptrecipeapp

import com.example.gptrecipeapp.model.GPT

interface Repository {
    suspend fun getGptResponse(
        body: GptRequestParam
    ): GPT
}