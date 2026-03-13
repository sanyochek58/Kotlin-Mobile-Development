package com.example.task4_10

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.core.content.ContextCompat
import com.example.task4_10.ui.theme.Task4_10Theme
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task4_10Theme {
                LocationScreen()
            }
        }
    }
}

@Composable
fun LocationScreen() {
    val context = LocalContext.current

    var address by remember { mutableStateOf("Нажмите кнопку") }
    var coordinates by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val fusedClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fineGranted || coarseGranted) {
            getLocation(
                context,
                fusedClient,
                onLoading = { isLoading = it },
                onResult = { addr, coords ->
                    address = addr
                    coordinates = coords
                }
            )
        } else {
            address = "Нет разрешения на геолокацию"
            coordinates = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = address,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        if (coordinates.isNotEmpty()) {
            Text(text = coordinates, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = {
                val fineGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                val coarseGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (fineGranted || coarseGranted) {
                    getLocation(
                        context,
                        fusedClient,
                        onLoading = { isLoading = it },
                        onResult = { addr, coords ->
                            address = addr
                            coordinates = coords
                        }
                    )
                } else {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            },
            enabled = !isLoading
        ) {
            Text("Получить мой адрес")
        }
    }
}

fun getLocation(
    context: Context,
    fusedClient: FusedLocationProviderClient,
    onLoading: (Boolean) -> Unit,
    onResult: (String, String) -> Unit
) {
    val fineGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val coarseGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (!fineGranted && !coarseGranted) {
        onResult("Нет разрешения на геолокацию", "")
        return
    }

    onLoading(true)

    val request = CurrentLocationRequest.Builder()
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .setMaxUpdateAgeMillis(10000L)
        .build()

    try {
        fusedClient.getCurrentLocation(request, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lng = location.longitude

                    if (Geocoder.isPresent()) {
                        val geocoder = Geocoder(context)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            geocoder.getFromLocation(
                                lat,
                                lng,
                                1,
                                object : Geocoder.GeocodeListener {
                                    override fun onGeocode(addresses: List<Address>) {
                                        onLoading(false)

                                        val addr = addresses.firstOrNull()
                                        val result = buildString {
                                            addr?.thoroughfare?.let { append("$it, ") }
                                            addr?.locality?.let { append("$it, ") }
                                            addr?.countryName?.let { append("$it, ") }
                                        }.ifEmpty { "Адрес не найден!" }

                                        onResult(
                                            result,
                                            "lat: ${"%.4f".format(lat)}, lng: ${"%.4f".format(lng)}"
                                        )
                                    }

                                    override fun onError(errorMessage: String?) {
                                        onLoading(false)
                                        onResult("Ошибка геокодирования", "")
                                    }
                                }
                            )
                        } else {
                            thread {
                                try {
                                    @Suppress("DEPRECATION")
                                    val addresses = geocoder.getFromLocation(lat, lng, 1)
                                    val addr = addresses?.firstOrNull()

                                    val result = buildString {
                                        addr?.thoroughfare?.let { append("$it, ") }
                                        addr?.locality?.let { append("$it, ") }
                                        addr?.countryName?.let { append("$it, ") }
                                    }.ifEmpty { "Адрес не найден!" }

                                    (context as? ComponentActivity)?.runOnUiThread {
                                        onLoading(false)
                                        onResult(
                                            result,
                                            "lat: ${"%.4f".format(lat)}, lng: ${"%.4f".format(lng)}"
                                        )
                                    }
                                } catch (e: Exception) {
                                    (context as? ComponentActivity)?.runOnUiThread {
                                        onLoading(false)
                                        onResult("Ошибка геокодирования: ${e.message}", "")
                                    }
                                }
                            }
                        }
                    } else {
                        onLoading(false)
                        onResult("Геокодер недоступен!", "")
                    }
                } else {
                    onLoading(false)
                    onResult("Локация недоступна!", "")
                }
            }
            .addOnFailureListener {
                onLoading(false)
                onResult("Ошибка получения локации: ${it.message}", "")
            }
    } catch (e: SecurityException) {
        onLoading(false)
        onResult("Нет разрешения на доступ к геолокации", "")
    }
}

@Preview(showBackground = true)
@Composable
fun LocationScreenPreview() {
    Task4_10Theme {
        LocationScreen()
    }
}