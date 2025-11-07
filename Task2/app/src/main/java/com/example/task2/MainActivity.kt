package com.example.task2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
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