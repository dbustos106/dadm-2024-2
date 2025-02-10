package com.example.reto_11.data

import com.example.reto_11.models.CompletionRequest
import com.example.reto_11.models.CompletionResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAiApi {
    companion object {
        const val BASE_URL = "https://openrouter.ai/api/v1/"
        const val API_KEY = "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"
    }
    @POST("chat/completions")
    suspend fun getMathAnswer(
        @Body completionRequest: CompletionRequest
    ): CompletionResponse
}
