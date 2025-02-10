package com.example.reto_11.data

import com.example.reto_11.models.CompletionRequest
import com.example.reto_11.models.CompletionResponse
import com.example.reto_11.ui.screen.chatbot.Message
import javax.inject.Inject

class OpenAiDataSource @Inject constructor(
    private val openAiApi: OpenAiApi
) {

    suspend fun getMathAnswerWithRetry(equation: CompletionRequest): CompletionResponse {
        try {
            return openAiApi.getMathAnswer(equation)
        } catch (e: Exception) {
            return CompletionResponse(
                id = "",
                `object` = "",
                created = -1,
                model = "",
                choices = listOf(
                    CompletionResponse.Choice(
                        message = Message(role = "assistant", content = "API no disponible"),
                        finishReason = "",
                        index = -1
                    )
                )
            )
        }
    }

}
