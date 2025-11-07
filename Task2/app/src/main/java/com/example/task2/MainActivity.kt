package com.example.task2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.task2.ui.theme.Task2Theme

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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    Task2Theme {
        Greeting("Android")
    }
}