package com.example.reto_11.ui.screen.chatbot

data class ChatbotUiState(
    val messages: List<Message> = emptyList()
)

data class Message(
    val role: String,
    val content: String
)
