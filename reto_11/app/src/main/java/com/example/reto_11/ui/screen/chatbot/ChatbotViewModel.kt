package com.example.reto_11.ui.screen.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reto_11.data.OpenAiRepository
import com.example.reto_11.models.CompletionRequest
import com.example.reto_11.ui.screen.chatbot.events.ChatbotUiEvent
import com.example.reto_11.ui.screen.chatbot.events.ChatbotViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val openAiRepository: OpenAiRepository
) : ViewModel() {

    // CultivoList UI state
    private val _uiState = MutableStateFlow(ChatbotUiState())
    val uiState = _uiState.asStateFlow()

    // EnterpriseList viewModel events
    private val _viewModelEvent = MutableSharedFlow<ChatbotViewModelEvent>(replay = 0)
    val viewModelEvent: SharedFlow<ChatbotViewModelEvent> = _viewModelEvent.asSharedFlow()

    /**
     * Handle various UI events triggered bt the user.
     * @param event The UI event to handle.
     */
    fun onEvent(event: ChatbotUiEvent) {
        when (event) {
            is ChatbotUiEvent.SendMessage -> {
                sendMessage(event.message)
            }
        }
    }

    private fun sendMessage(message: Message) {
        viewModelScope.launch {

            val prompt = CompletionRequest(
                model = "meta-llama/llama-3.2-3b-instruct:free",
                messages = _uiState.value.messages + message,
            )

            _uiState.update {
                it.copy(messages = _uiState.value.messages + message)
            }

            _uiState.update {
                val response = openAiRepository.getMathAnswer(prompt)
                val choice = response.choices?.firstOrNull()

                if (choice != null) {
                    it.copy(messages = _uiState.value.messages + choice.message)
                } else {
                    it.copy(
                        messages = _uiState.value.messages + Message(
                            "assistant",
                            "Error: No message returned from API"
                        )
                    )
                }
            }
        }
    }

}
