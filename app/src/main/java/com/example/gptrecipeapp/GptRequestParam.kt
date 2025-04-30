package com.example.gptrecipeapp

data class GptRequestParam(
    val model: String = "gpt-3.5-turbo",
    val messages: List<MessageRequestParam> = ArrayList()
)

data class MessageRequestParam(
    val role: String,
    val content: String
)