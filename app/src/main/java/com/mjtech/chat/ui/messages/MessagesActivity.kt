package com.mjtech.chat.ui.messages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mjtech.chat.ui.theme.OpenChatTheme

class MessagesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenChatTheme {
                MessagesScreen()
            }
        }
    }
}

data class Message(val text: String, val isSentByUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen() {
    val messages = remember { mutableStateListOf(
        Message("Olá! Tudo bem?", false), // Recebida
        Message("Tudo ótimo, e com você?", true), // Enviada
        Message("Estou bem também! Que bom!", false), // Recebida
        Message("O que você está fazendo?", true), // Enviada
        Message("Estou codificando em Compose!", false), // Recebida
        Message("Que legal! Me mostra o código?", true) // Enviada
    )}

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nome do Contato") } // Título da conversa
            )
        },
        bottomBar = {
            // Área para digitar e enviar mensagens
            MessageInputSection(onSendMessage = { newMessage ->
                messages.add(Message(newMessage, true)) // Adiciona a nova mensagem como enviada
            })
        }
    ) { paddingValues ->
        // Lista de mensagens
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Importante para não sobrepor a TopBar e BottomBar
                .padding(horizontal = 8.dp),
            reverseLayout = true // Para rolar a partir do final (mensagens mais recentes)
        ) {
            items(messages.reversed()) { message -> // Exibe as mensagens na ordem correta
                MessageBubble(message = message)
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    // Cores inspiradas no WhatsApp
    // Verde claro para ENVIADO (User)
    val sentColor = Color(0xFF4D6D98)
    // Branco/Cinza claro para RECEBIDO (Other)
    val receivedColor = Color(0xFFFFFFFF)

    val backgroundColor = if (message.isSentByUser) sentColor else receivedColor
    val textColor = if (message.isSentByUser) Color.White else Color.Black

    // Define o alinhamento da Row (direita para enviado, esquerda para recebido)
    val arrangement = if (message.isSentByUser) Arrangement.End else Arrangement.Start

    // Define a forma dos cantos para simular a 'ponta'
    // Se a mensagem foi ENVIADA: o canto inferior direito é reto (0.dp)
    val shape = if (message.isSentByUser) {
        // Balão ENVIADO: Ponto na parte inferior direita
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 4.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp // Canto menor para simular a ponta
        )
    } else {
        // Balão RECEBIDO: Ponto na parte inferior esquerda
        RoundedCornerShape(
            topStart = 4.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp, // Canto menor para simular a ponta
            bottomEnd = 16.dp
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp), // Espaçamento entre as mensagens
        horizontalArrangement = arrangement
    ) {
        // Usamos Card ou Surface. Card já vem com elevação (sombra suave)
        Card(
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            // Adiciona uma pequena elevação (sombra) para destacar o balão
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .widthIn(max = 300.dp) // Limita a largura máxima do balão
        ) {
            Text(
                text = message.text,
                color = textColor, // Garante que o texto seja preto em ambos
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}
@Composable
fun MessageInputSection(onSendMessage: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            label = { Text("Digite sua mensagem...") },
            modifier = Modifier.weight(1f), // Ocupa o máximo de espaço possível
            maxLines = 5 // Limita o número de linhas para a mensagem
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                if (messageText.isNotBlank()) {
                    onSendMessage(messageText)
                    messageText = "" // Limpa o campo após enviar
                }
            },
            enabled = messageText.isNotBlank() // Botão só fica ativo se houver texto
        ) {
            Text("Enviar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMessagesScreen() {
    OpenChatTheme {
        MessagesScreen()
    }
}