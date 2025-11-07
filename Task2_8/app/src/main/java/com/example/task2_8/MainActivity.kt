package com.example.task2_8

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task2_8.ui.theme.Task2_8Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task2_8Theme {
                    WaterTracker()
                }
            }
        }
    }
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:parent=pixel_9_pro"
)
@Composable
fun WaterTracker() {
    var waterCount by remember { mutableStateOf(100) }
    var successTargetDay by remember { mutableStateOf(0) }
    var target by remember { mutableStateOf(false) }

    val primaryColor = Color.Blue
    val secondaryColor = Color.DarkGray
    val successColor = Color.Green

    Task2_8Theme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1000.dp).padding(0.dp, 100.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Трекер воды",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = "$waterCount мл",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = secondaryColor
                )

                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = "Цель: 1500 мл",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        waterCount += 250
                        if(waterCount >= 1500 && !target){
                            successTargetDay++
                            target = true
                        }
                              },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text(
                        text = "+250 мл",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = {
                        waterCount = 0
                        target = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(
                        text = "Новый день",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                Column (
                    modifier = Modifier.padding(16.dp)
                ){
                    Text(
                        text = "Стаитистика",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Дней с нормой воды 1500мл: ",
                        fontSize = 16.sp,
                        color = secondaryColor
                    )
                    Text(
                        text = "$successTargetDay",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = if(successTargetDay > 0) successColor else secondaryColor
                    )

                    if(waterCount >= 1500){
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Цель достигнута !!!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = successColor
                        )
                    }
                }
            }
        }
    }
}
