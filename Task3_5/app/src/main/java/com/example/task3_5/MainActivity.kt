package com.example.task3_5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.task3_5.navig.DetailScreen
import com.example.task3_5.navig.MainScreen
import com.example.task3_5.ui.theme.Task3_5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task3_5Theme {
                AppNavHost()
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:parent=pixel_9_pro"
)
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable<MainScreen> {
                MainScreen(
                    onGoToDetail = { navController.navigate(DetailScreen) }
                )
            }

            composable<DetailScreen> {
                DetailScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun MainScreen(onGoToDetail: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onGoToDetail
        ) {
            Text(text = "Go to Detail")
        }
    }
}

@Composable
fun DetailScreen(onBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DetailScreen",
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onBack
            ) {
                Text(text = "Back")
            }
        }
    }
}
