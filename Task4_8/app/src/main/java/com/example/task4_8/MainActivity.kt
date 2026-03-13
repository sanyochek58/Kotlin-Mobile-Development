package com.example.task4_8

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
import androidx.lifecycle.observe
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.impl.WorkManagerImpl
import com.example.task4_8.ui.theme.Task4_8Theme
import com.example.task4_8.workers.CompressWorker
import com.example.task4_8.workers.UploadWorker
import com.example.task4_8.workers.WatermarkWorker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoScreen()
        }
    }
}

@Composable
fun PhotoScreen(){
    val context = LocalContext.current
    val workManager = remember { WorkManager.getInstance(context) }

    var status by remember { mutableStateOf("Нажмите кнопку, чтобы начать") }
    var isWorking by remember { mutableStateOf(false) }
    var resultFile by remember { mutableStateOf("") }

    val workInfo by workManager
        .getWorkInfosByTagLiveData("photo_chain")
        .observeAsState()

    LaunchedEffect(workInfo) {
        val infos = workInfo ?: return@LaunchedEffect
        when {
            infos.any{it.state == WorkInfo.State.RUNNING } -> {
                isWorking = true
                val running = infos.indexOfFirst {it.state == WorkInfo.State.RUNNING }
                status = when(running){
                    0 -> "Сжимаем фото ..."
                    1 -> "Добавляем водяной знак ..."
                    2 -> "Загружаем в облако ..."
                    else -> "Обработка ..."
                }
            }
            infos.all { it.state == WorkInfo.State.SUCCEEDED } && infos.isNotEmpty() -> {
                isWorking = false
                resultFile = infos.lastOrNull()?.outputData?.getString("file") ?: ""
                status = "Готово! Фото загружено!"
            }
            infos.any{ it.state == WorkInfo.State.FAILED } -> {
                isWorking = false
                status = "Ошибка!!!"
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
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        if(isWorking){
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(24.dp))

        if( resultFile.isNotEmpty()){
            Text(text = "Файл: $resultFile", fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                startPhotoChain(workManager)
                resultFile = ""
            },
            enabled = !isWorking
        ) {
            Text("Начать обработку и загрузку")
        }
    }
}

fun startPhotoChain(workManager: WorkManager){

    val worker1 = OneTimeWorkRequestBuilder<CompressWorker>().addTag("photo_chain").build()
    val worker2 = OneTimeWorkRequestBuilder<WatermarkWorker>().addTag("photo_chain").build()
    val worker3 = OneTimeWorkRequestBuilder<UploadWorker>().addTag("photo_chain").build()

    workManager.beginUniqueWork(
        "photo_chain",
        ExistingWorkPolicy.REPLACE,
        worker1
    )
        .then(worker2)
        .then(worker3)
        .enqueue()
}

@Composable
@Preview(
    showBackground = true
)
fun PhotoScreenPreview(){
    Task4_8Theme { PhotoScreen() }
}