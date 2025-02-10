package com.example.reto_11.data

import com.example.reto_11.models.CompletionRequest
import com.example.reto_11.models.CompletionResponse
import javax.inject.Inject

class OpenAiRepository @Inject constructor(
    private val dataSource: OpenAiDataSource
) {
    suspend fun getMathAnswer(equation: CompletionRequest): CompletionResponse {
        return dataSource.getMathAnswerWithRetry(equation)
    }
}
