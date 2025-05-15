package com.example.gptrecipeapp

import com.example.gptrecipeapp.model.GPT

class RepositoryImpl(private val apiService: ApiService) : Repository {
    override suspend fun getGptResponse(body: GptRequestParam): GPT {
        return apiService.getGptResponse(body)
    }
}