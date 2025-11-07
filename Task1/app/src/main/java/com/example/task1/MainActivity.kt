package com.example.task1

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task1.ui.theme.Task1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Alexandr",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Задание 1
@Composable
fun Greeting(name: String?, modifier: Modifier = Modifier) {
    if(name.isNullOrBlank()){
        Text(
            text = "Имя не задано !",
            modifier = modifier.padding(start = 40.dp, top = 20.dp, end = 40.dp)
        )
    }
    else {
        Text(
            text = "Hello $name!",
            modifier = modifier.padding(start = 40.dp, top = 20.dp, end = 40.dp)
        )
    }
}

// Задание 2
@Preview(
    name = "portrait",
    device = "spec:parent=Galaxy Nexus,orientation=portrait",
    showSystemUi = true
)
@Composable
fun PortraitGreeting(){
    Task1Theme {
        Greeting("Alex")
    }
}

@Preview(
    name = "landscape",
    device = "spec:parent=Galaxy Nexus,orientation=landscape",
    showSystemUi = true,
    widthDp = 640,
    heightDp = 360
)
@Composable
fun LanscapeGreeting(){
    Task1Theme {
        Greeting("Alice")
    }
}

@Preview(
    name = "round",
    device = "spec:parent=Galaxy Nexus",
    showSystemUi = true,
    widthDp = 200,
    heightDp = 200,
    showBackground = true,

)
@Composable
fun RoundGreeting(){
    Task1Theme {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(androidx.compose.ui.graphics.Color.Yellow)
        ){
            Greeting(null)
        }

    }
}

// Задание 3

@Preview(
    name="Jetpack",
    device = "spec:parent=Galaxy Nexus",
    showSystemUi = true
)
@Composable
fun WorkWithText(){
    Task1Theme {
        Column {
            Text(
                modifier = Modifier.padding(start = 30.dp, top = 30.dp, end = 30.dp),
                text = stringResource(R.string.info_string),
                color = Color.Green,
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = null,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(30.dp))
            Text(
                modifier = Modifier,
                text = stringResource(R.string.info_string),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.size(30.dp))
            Text(
                text = stringResource(R.string.info_string),
                color = Color.Black,
                fontSize = 24.sp,
                style = TextStyle(
                    textDecoration = TextDecoration.Underline,
                    fontStyle = FontStyle.Normal
                ),
                modifier = Modifier
                    .padding(start = 48.dp)
                    .background(Color.Green)
            )
        }
    }
}

// Задание 4

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "ButtonPreview",
    device = "spec:parent=Galaxy Nexus",
    showSystemUi = true
)
@Composable
fun TestScreenButton() {
    Task1Theme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "FirstComposeProject",
                            fontSize = 24.sp,
                            fontStyle = FontStyle.Normal
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Blue,
                        titleContentColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                onClick = {
                    // Обработчик нажатия
                    println("Кнопка нажата!")
                }
            ) {
                Text("Нажми на меня")
            }
        }
    }
}

// Задание 5

@Preview(
    name = "Task1Preview",
    showSystemUi = true
)
@Composable
fun Task1Screen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Gray)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .clip(CircleShape)
                .size(70.dp)
                .background(Color(0xFFFF5722)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "АБ",
                color = Color.Black,
                fontSize = 18.sp
            )
        }
    }
}
