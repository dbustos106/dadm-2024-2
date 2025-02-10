package com.example.reto_11.ui.screen.chatbot.events

sealed interface ChatbotViewModelEvent {
    data class Success(val message: String) : ChatbotViewModelEvent
    data class Error(val message: String) : ChatbotViewModelEvent
}
