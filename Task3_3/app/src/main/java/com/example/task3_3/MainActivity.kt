package com.example.task3_3

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
import com.example.task3_3.ui.theme.Task3_3Theme

class MainActivity : ComponentActivity() {

    val TAG: String = "MAINACTIVITY_LOG: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task3_3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onStart() {
        Log.d(TAG, "function - onStart()")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "function - onResume()")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "function - onPause()")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "function - onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "function - onDestroy()")
        super.onDestroy()
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Task3_3Theme {
        Greeting("Android")
    }
}