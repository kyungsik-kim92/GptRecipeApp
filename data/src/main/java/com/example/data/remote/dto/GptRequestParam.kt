package com.example.data.remote.dto

data class GptRequestParam(
    val model: String = "gpt-3.5-turbo",
    val messages: List<MessageRequestParam> = emptyList()
)

data class MessageRequestParam(
    val role: String,
    val content: String
)