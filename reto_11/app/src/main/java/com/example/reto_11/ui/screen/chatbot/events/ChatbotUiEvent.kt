package com.example.reto_11.ui.screen.chatbot.events

import com.example.reto_11.ui.screen.chatbot.Message

sealed interface ChatbotUiEvent {
    data class SendMessage(val message: Message): ChatbotUiEvent
}
