package com.example.reto_11.ui.screen.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reto_11.ui.screen.chatbot.events.ChatbotUiEvent

@Composable
fun ChatbotScreen(
    chatViewModel: ChatbotViewModel = hiltViewModel()
) {
    val uiState by chatViewModel.uiState.collectAsState()

    val messages = uiState.messages
    var inputText by rememberSaveable { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(5.dp)
            ) {
                Text(
                    text = "Wall-e GPT",
                    fontSize = 20.sp,
                    color = Color.Blue,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))

                Row {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(6.dp)
                            .background(color = Color.Green, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Online",
                        color = Color.Blue,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shadowElevation = 6.dp,
                color = Color.White,
                shape = RoundedCornerShape(30.dp),
            ) {
                TextField(
                    modifier = Modifier.background(color = Color.White),
                    value = inputText,
                    onValueChange = { value -> inputText = value },
                    placeholder = {
                        Text(
                            text = "Write your message",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    trailingIcon = {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    if (inputText.isNotEmpty()) {
                                        chatViewModel.onEvent(ChatbotUiEvent.SendMessage(
                                            Message(role = "user", content = inputText)
                                        ))
                                        inputText = ""
                                    }
                                },
                            imageVector = Icons.Filled.Send,
                            contentDescription = "Enviar mensaje"
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.White,
                        cursorColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        focusedIndicatorColor = Color.Red,
                        focusedLabelColor = Color.DarkGray
                    )
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
                .padding(bottom = 40.dp, start = 10.dp, end = 10.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
                items(messages) { message ->
                    when (message.role) {
                        "user" -> {
                            Surface(
                                modifier = Modifier.align(Alignment.End),
                                color = Color.Blue,
                                shape = RoundedCornerShape(
                                    topStart = 25.dp,
                                    bottomEnd = 25.dp,
                                    bottomStart = 25.dp
                                )
                            ) {
                                Text(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(horizontal = 24.dp, vertical = 18.dp),
                                    text = message.content,
                                    textAlign = TextAlign.End,
                                    style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                                )
                            }
                        }

                        "assistant" -> {
                            Row(
                                modifier = Modifier
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .align(Alignment.Bottom),
                                    shape = CircleShape,
                                    color = Color.White,
                                    shadowElevation = 4.dp
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 6.dp)
                                            .size(18.dp),
                                        imageVector = Icons.Filled.Chat,
                                        contentDescription = "Ícono de chatbot"
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 24.dp),
                                    shape = RoundedCornerShape(
                                        topStart = 25.dp,
                                        topEnd = 25.dp,
                                        bottomEnd = 25.dp
                                    ),
                                    color = Color.Gray
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = 14.dp,
                                            vertical = 24.dp
                                        ),
                                        text = message.content,
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            color = Color(
                                                0xFF505050
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
