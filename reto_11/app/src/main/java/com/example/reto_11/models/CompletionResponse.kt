package com.example.reto_11.models

import com.example.reto_11.ui.screen.chatbot.Message
import com.google.gson.annotations.SerializedName

data class CompletionResponse(

    @SerializedName("id")
    val id: String,

    @SerializedName("object")
    val `object`: String,

    @SerializedName("created")
    val created: Int,

    @SerializedName("model")
    val model: String,

    @SerializedName("choices")
    val choices: List<Choice>,

) {
    data class Choice(

        @SerializedName("message")
        val message: Message,

        @SerializedName("finish_reason")
        val finishReason: String,

        @SerializedName("index")
        val index: Int
    )
}
