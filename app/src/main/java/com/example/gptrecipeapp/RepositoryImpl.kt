package com.example.gptrecipeapp

import com.example.gptrecipeapp.model.GPT

class RepositoryImpl(private val dataSource: DataSource) : Repository {
    override suspend fun getGptResponse(body: GptRequestParam): GPTResponse {
        return dataSource.getGptResponse(body)
    }
}