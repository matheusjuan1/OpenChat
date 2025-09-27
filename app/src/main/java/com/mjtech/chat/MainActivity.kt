package com.mjtech.chat

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mjtech.chat.ui.login.LoginScreen
import com.mjtech.chat.ui.messages.MessagesActivity
import com.mjtech.chat.ui.theme.OpenChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenChatTheme {
                LoginScreen(
                    onLoginClicked = {
                        Intent(this, MessagesActivity::class.java).also {
                            startActivity(it)
                        }
                    },
                    onRegisterClicked = {
                        // Lógica de registro
                    }
                )
            }
        }
    }
}