package org.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Weather(val city: String, val temp: Int, val condition: String)
