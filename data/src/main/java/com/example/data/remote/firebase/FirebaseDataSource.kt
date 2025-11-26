package com.example.data.remote.firebase

import android.util.Log
import com.example.data.remote.dto.GPT
import com.example.data.remote.dto.GptRequestParam
import com.google.firebase.Firebase
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.functions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDataSource @Inject constructor() {

    private val functions: FirebaseFunctions = Firebase.functions

    suspend fun getGptResponse(request: GptRequestParam): GPT {
        return try {
            val data = hashMapOf(
                "messages" to request.messages.map { message ->
                    mapOf(
                        "role" to message.role,
                        "content" to message.content
                    )
                }
            )
            val result = functions
                .getHttpsCallable("chatWithOpenAI")
                .call(data)
                .await()
            val response = result.getData() as? Map<*, *>
                ?: throw Exception("Invalid response format")

            val message = response["message"] as? String
                ?: throw Exception("No message in response")

            val isComplete = response["isComplete"] as? Boolean ?: true

            GPT(
                choices = listOf(
                    com.example.data.remote.dto.Choice(
                        message = com.example.data.remote.dto.Message(
                            role = "assistant",
                            content = message
                        ),
                        finish_reason = if (isComplete) "stop" else "length"
                    )
                )
            )
        } catch (e: Exception) {
            throw Exception("Firebase Functions 호출 실패: ${e.message}")
        }
    }
}