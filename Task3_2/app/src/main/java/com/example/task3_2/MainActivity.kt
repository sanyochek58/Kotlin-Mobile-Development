package com.example.task3_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.task3_2.ui.theme.onPrimaryDark
import com.example.task3_2.ui.theme.errorDark
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task3_2.ui.theme.Task3_2Theme
import com.example.task3_2.ui.theme.onErrorContainerLight
import com.example.task3_2.ui.theme.onPrimaryLight
import com.example.task3_2.ui.theme.onTertiaryDarkHighContrast
import com.example.task3_2.ui.theme.primaryDark
import com.example.task3_2.ui.theme.shapes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task3_2Theme {
                ThemeDemoScreen()
            }
        }
    }
}

@Composable
@Preview
fun ThemeDemoScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(errorDark)
            .padding(top = 15.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "New Theme",
                color = onPrimaryDark,
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = shapes.medium
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Петр Петров", fontSize = 24.sp)
                    Text(text = "---", fontSize = 24.sp)
                    Text(text = "+7-999-38-27", fontSize = 24.sp)
                }
            }
            Button(
                modifier = modifier,
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    contentColor = onTertiaryDarkHighContrast,
                    containerColor = onPrimaryLight
                )
            ) {
                Text(text = "Нажать", color = onErrorContainerLight)
            }
            Button(
                onClick = { /* TODO: что-нибудь сделать */ },
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "Кнопка в новой теме")
            }
        }
    }
}
