package com.example.task3_6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task3_6.ui.theme.Task3_6Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task3_6Theme {
                val windowSizeClass = calculateWindowSizeClass(this)

                adaptiveApp(
                    windowSizeClass.widthSizeClass
                )
            }
        }
    }
}

@Composable
fun adaptiveApp(
    windowSize: WindowWidthSizeClass,
){
    when(windowSize){
        WindowWidthSizeClass.Compact -> @Composable {
            OneColumnLayout()
        }
        WindowWidthSizeClass.Medium -> {

        }
        WindowWidthSizeClass.Expanded -> {
            TwoColumnLayout()
        }
        else -> {

        }
    }
}

@Composable
fun OneColumnLayout(modifier: Modifier = Modifier){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Compact",
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Одна колонка: контент под друг другом")
    }
}

@Composable
fun TwoColumnLayout(){
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Список", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Здесь условный список элементов")
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Детали", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Здесь подробная информация о выбранном элементе")
            }
        }
    }
}
