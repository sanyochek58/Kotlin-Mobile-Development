package com.example.task4_9

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.task4_9.ui.theme.Task4_9Theme
import com.example.task4_9.workers.CityWeatherWorker
import com.example.task4_9.workers.ReportWorker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherScreen()
        }
    }
}

@Composable
fun WeatherScreen(){
    val context = LocalContext.current
    val workManager = remember { WorkManager.getInstance(context) }

    var status by remember { mutableStateOf("Нажмите чтоб загрузить погоду") }
    var isWorking by remember { mutableStateOf(false) }
    var avgTemp by remember { mutableStateOf("") }

    val workInfo by workManager
        .getWorkInfosByTagLiveData("weather_chain")
        .observeAsState()

    LaunchedEffect(workInfo) {
        val infos = workInfo ?: return@LaunchedEffect
        if (infos.isEmpty()) return@LaunchedEffect

        when{
            infos.any{it.state == WorkInfo.State.RUNNING } -> {
                isWorking = true
                val cityWorkers = infos.dropLast(1)
                val done = cityWorkers.count{ it.state == WorkInfo.State.SUCCEEDED }
                val total = cityWorkers.size

                status = when {
                    done == 0 -> "Загружаем погоду для $total городов..."
                    done < total -> "Готово $done из $total городов..."
                    else -> "Все данные получены, формируем отчёт"
                }
            }
            infos.all{ it.state == WorkInfo.State.SUCCEEDED } -> {
                isWorking = false
                val temp = infos.last().outputData.getInt("avg_temp", 0)
                avgTemp = "$temp"
                status = "Отчёт готов!"
            }
            infos.any{ it.state == WorkInfo.State.FAILED } -> {
                isWorking = false
                status = "Ошибка!!!"
            }
            infos.all{ it.state == WorkInfo.State.ENQUEUED } -> {
                isWorking = true
                status = "Загружаю данные для 3 городов"
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = status,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        if(isWorking){
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(24.dp))

        if(avgTemp.isNotEmpty()){
            Text(
                text = "Средняя температура: $avgTemp",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            val prefs = context.getSharedPreferences("weather", Context.MODE_PRIVATE)
            listOf("Москва", "Лондон", "Нью-Йорк").forEach { city ->
                val data = prefs.getString(city, "нет данных")
                Text(text = "$city: $data", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                avgTemp = ""
                startWeatherWork(workManager, context)
            },
            enabled = !isWorking
        ) {
            Text("Собрать прогноз!")
        }
    }
}

fun startWeatherWork(workManager: WorkManager, context: Context){

    context.getSharedPreferences("weather", Context.MODE_PRIVATE)
        .edit()
        .clear()
        .apply()

    val moscow = OneTimeWorkRequestBuilder<CityWeatherWorker>()
        .setInputData(workDataOf("city" to "Москва"))
        .addTag("weather_chain")
        .build()

    val london = OneTimeWorkRequestBuilder<CityWeatherWorker>()
        .setInputData(workDataOf("city" to "Лондон"))
        .addTag("weather_chain")
        .build()

    val newYork = OneTimeWorkRequestBuilder<CityWeatherWorker>()
        .setInputData(workDataOf("city" to "Нью-Йорк"))
        .addTag("weather_chain")
        .build()

    val final = OneTimeWorkRequestBuilder<ReportWorker>()
        .addTag("weather_chain")
        .build()

    workManager.beginUniqueWork(
        "weather_chain",
        ExistingWorkPolicy.REPLACE,
        listOf(moscow, london, newYork)
    )
        .then(final)
        .enqueue()
}