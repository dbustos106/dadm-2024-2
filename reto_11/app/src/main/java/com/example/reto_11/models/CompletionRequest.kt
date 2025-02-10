package com.example.reto_11.models

import com.example.reto_11.ui.screen.chatbot.Message

data class CompletionRequest(
    val model: String,
    val messages: List<Message>,
)
