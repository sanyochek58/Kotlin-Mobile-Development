package com.example.task2

import com.example.task2.R
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.task2.ui.theme.Task2Theme
import kotlinx.coroutines.sync.Mutex


class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"

    // Функции жизненного цикла приложения
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
            Log.d(TAG, "Функция OnCreate()")
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Функция OnResume()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Функция OnStop()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "Функция OnPause")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "Функция OnStart()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Функция OnDestroy()")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun GreetingPreview() {
//    Task2Theme {
//        Greeting("Android")
//    }
//}


// Задача 6

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:parent=Galaxy Nexus, orientation=portrait"
)
@Composable
fun workCircle(){
    Task2Theme {
        Box(
            modifier = Modifier
                .padding(0.dp, 25.dp)
                .size(240.dp,120.dp)
                .background(Color.Black),
            contentAlignment = Alignment.TopEnd
        ){
            Image(
                modifier = Modifier,
                painter = painterResource(R.drawable.circle),
                contentDescription = "Circle"
            )
        }

    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    device = "spec:parent=Galaxy Nexus, orientation=portrait"
)
@Composable
fun modifyWorkCircle(){
    Task2Theme {
        Box(
            modifier = Modifier
                .padding(0.dp, 25.dp)
                .size(240.dp,120.dp)
                .background(Color.Blue),
            contentAlignment = Alignment.Center
        ){
            Image(
                modifier = Modifier.width(400.dp).fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                painter = painterResource(R.drawable.circle),
                colorFilter = ColorFilter.tint(Color(0xFF8B00FF))
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:parent=Galaxy Nexus, orientation=portrait"
)
@Composable
fun workWithColumn1(){
    Task2Theme {
        Column(
            modifier = Modifier.padding(0.dp, 25.dp)
        ){
            Text(
                modifier = Modifier,
                text = "Имя: Евгений"
            )
            Text(
                modifier = Modifier,
                text = "Отчество: Андреевич"
            )
            Text(
                modifier = Modifier,
                text = "Фамилия: Лукашин"
            )
            Text(
                modifier = Modifier,
                text = "Мобильный телефон: +7 495 495 95 95"
            )
            Text(
                modifier = Modifier,
                text = "г. Москва, 3-я улица Строителей д. 25, кв. 12"
            )
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:parent=Galaxy Nexus, orientation=portrait"
)
@Composable
fun workWithColumn2(){
    Task2Theme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Первый блок
                Column(
                    modifier = Modifier
                        .padding(25.dp)
                        .background(color = Color.Gray)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Имя: Евгений")
                    Text("Отчество: Андреевич")
                    Text("Фамилия: Лукашин")
                    Text("Мобильный телефон: +7 495 495 95 95")
                    Text("Адрес: г. Москва, 3-я улица Строителей д. 25, кв. 12")
                }

                // Второй блок
                Column(
                    modifier = Modifier
                        .padding(25.dp)
                        .background(color = Color.Gray)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Имя: Василий")
                    Text("Отчество: Егорович")
                    Text("Фамилия: Кузьмин")
                    Text("Мобильный телефон: - ")
                    Text("Адрес: г. Москва, 3-я улица Строителей д. 25, кв. 12")
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:parent=Galaxy Nexus, orientation=portrait"
)
@Composable
fun workWithColumn3() {
    Task2Theme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(25.dp)
                        .background(color = Color.Gray)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .padding(25.dp)
                            .weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text("Имя: Евгений")
                        Text("Отчество: Андреевич")
                        Text("Фамилия: Лукашин")
                        Text("Мобильный телефон: +7 495 495 95 95")
                        Text("Адрес: г. Москва, 3-я улица Строителей д. 25, кв. 12")
                    }

                    Image(
                        painter = painterResource(R.drawable.star_on),
                        contentDescription = "Star",
                        modifier = Modifier.size(40.dp).padding(end = 16.dp)
                    )
                }
            }
        }
    }
}