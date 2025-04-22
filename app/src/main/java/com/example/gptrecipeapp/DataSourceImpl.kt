package com.example.gptrecipeapp

class DataSourceImpl(private val apiService: ApiService) : DataSource {
    override suspend fun getGptResponse(body: GptRequestParam): GPTResponse {
        return apiService.getGptResponse(body)
    }
}