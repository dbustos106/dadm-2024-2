package com.example.happy_birthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.happy_birthday.ui.theme.Happy_birthdayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Happy_birthdayTheme {
                Scaffold(
                    //topBar = { TopAppBar(title = { Text("Mi App") }) },
                    /*floatingActionButton = {
                        FloatingActionButton(onClick = { /* Acción */ }) {
                            Icon(Icons.Filled.Add, contentDescription = "Add")
                        }
                    },*/
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    GreetingText(
                        message = "Hola mundo!",
                        from = "From Julian",
                        modifier = Modifier.padding(innerPadding).padding(top = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun GreetingText(message: String, from: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = message,
            fontSize = 100.sp,
            lineHeight = 116.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            text = from,
            fontSize = 36.sp,
            modifier = Modifier
                .padding(16.dp)
                .align(alignment = Alignment.End)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Happy_birthdayTheme {
        GreetingText(message = "Hola mundo!", from = "From Julian")
    }
}