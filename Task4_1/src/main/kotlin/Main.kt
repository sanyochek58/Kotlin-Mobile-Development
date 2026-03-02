package org.example

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.json.Json
import org.example.model.Sales
import org.example.model.User
import org.example.model.Weather
import java.io.File
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun simulateError(task: String){
    if(Random.nextDouble() < 0.2){
        throw RuntimeException("Ошибка загрузки данных ! - в задаче $task")
    }
}

suspend fun loadUsers(path: String): List<User>{
    delay(1800);
    simulateError("USERS")
    val text = File(path).readText()
    val users = Json.decodeFromString<List<User>>(text)
    return users
}

suspend fun loadWeather(path: String): List<Weather>{
    delay(2500);
    simulateError("WEATHER")
    val text = File(path).readText()
    val weather = Json.decodeFromString<List<Weather>>(text)
    return weather
}

suspend fun loadSales(path: String): Map<String, Int> {
    delay(1200)
    simulateError("SALES")
    val text = File(path).readText()
    val sales: Sales = Json.decodeFromString(text)
    return sales.items.associate { it.product to it.qty }
}

fun printUsers(users: List<User>) {
    println("Пользователи:")
    users.forEach { println(" - ${it.id}: ${it.name}") }
}

fun printSales(sales: Map<String, Int>) {
    println("Продажи (qty):")
    sales.forEach { (product, qty) -> println(" - $product: $qty") }
}

fun printWeather(weather: List<Weather>) {
    println("Погода:")
    weather.forEach { println(" - ${it.city}: ${it.temp}°C (${it.condition})") }
}

fun main() {

    val usersPath = "/Users/admin/Desktop/Kotlin-Mobile-Development/Task4_1/src/main/kotlin/data/users.json"
    val weatherPath = "/Users/admin/Desktop/Kotlin-Mobile-Development/Task4_1/src/main/kotlin/data/weather.json"
    val salesPath = "/Users/admin/Desktop/Kotlin-Mobile-Development/Task4_1/src/main/kotlin/data/sales.json"

    val time = measureTimeMillis {
        runBlocking {
            supervisorScope {

                val usersDef = async { runCatching { loadUsers(usersPath) }}
                val weatherDef = async { runCatching { loadWeather(weatherPath) }}
                val salesDef = async { runCatching { loadSales(salesPath) }}

                val usersRes = usersDef.await()
                val weatherRes = weatherDef.await()
                val salesRes = salesDef.await()

                if (usersRes.isFailure || weatherRes.isFailure || salesRes.isFailure) {

                    println(" Произошла ошибка при загрузке данных:")

                    if (usersRes.isFailure) {
                        println(" - USERS: ${usersRes.exceptionOrNull()?.message}")
                    }

                    if (weatherRes.isFailure) {
                        println(" - WEATHER: ${weatherRes.exceptionOrNull()?.message}")
                    }

                    if (salesRes.isFailure) {
                        println(" - SALES: ${salesRes.exceptionOrNull()?.message}")
                    }

                } else {
                    val users = usersRes.getOrThrow()
                    val weather = weatherRes.getOrThrow()
                    val sales = salesRes.getOrThrow()

                    println("Данные загружены!\n")

                    printUsers(users)
                    println()
                    printSales(sales)
                    println()
                    printWeather(weather)
                }
            }
        }
    }
    System.out.println("Общее время работы: ${time} ms")
}
