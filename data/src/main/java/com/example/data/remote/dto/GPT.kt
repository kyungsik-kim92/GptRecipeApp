package com.example.data.remote.dto

data class GPT(
    val choices: List<Choice> = emptyList()
)

data class Choice(
    val message: Message = Message(),
    val finish_reason: String? = null
)

data class Message(
    val role: String? = null,
    val content: String? = null
)